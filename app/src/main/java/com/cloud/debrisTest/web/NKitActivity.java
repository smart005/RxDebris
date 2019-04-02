package com.cloud.debrisTest.web;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloud.debris.BaseActivity;
import com.cloud.debrisTest.R;
import com.cloud.debrisTest.databinding.NkitViewBinding;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.logs.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/22
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class NKitActivity extends BaseActivity {

    private NkitViewBinding binding;
    private boolean isReloadHtml = false;
    private String historyUrl = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.nkit_view);
        binding.setHandler(this);
        WebSettings settings = binding.nkitWv.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        //sdk 4.4即19之前生效
        binding.nkitWv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.nkitWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Logger.info("resource url=", url);
            }

//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                return super.shouldInterceptRequest(view, request);
//            }

            //https://developer.android.google.cn/reference/android/webkit/WebViewClient?hl=zh-cn
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (TextUtils.equals(url, "about:blank")) {
                    return;
                }
                if (!TextUtils.equals(historyUrl, url)) {
                    isReloadHtml = false;
                    historyUrl = url;
                }
                if (!isReloadHtml) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.nkitWv.loadUrl("javascript:debris.getHtmlContent(document.documentElement.outerHTML);");
                        }
                    }, 200);
                }
            }
        });
        binding.nkitWv.addJavascriptInterface(new JSObject(), "debris");
        historyUrl = "http://wx.szldj.gov.cn/SearchOnline/index.html";
        binding.nkitWv.loadUrl(historyUrl);
//        binding.nkitWv.loadUrl("http://wx.szldj.gov.cn/SearchOnline/service/guidanceList/guidanceList.html?id=2296632&title=基本医疗保险");
//        binding.nkitWv.loadUrl("http://192.168.31.52:8020/web_replace/index.html");
    }

    public class JSObject {

        @JavascriptInterface
        @com.cloud.mixed.h5.JavascriptInterface
        public void getHtmlContent(String html) {
            //取出body中onload调用的函数
            String readyFun = "";
            String pattern = "<body[^<>]*?\\sonload=['\"]?(.*?)['\"]?(\\s.*?)?>";
            List<String> match = match(html, "body", "onload");
            if (!ObjectJudge.isNullOrEmpty(match)) {
                readyFun = match.get(0);
            }
            //加载适配页面脚本
            StringBuilder builder = new StringBuilder();
            builder.append("<script>function _pageLoad() {");
            builder.append("initpage();");
            builder.append("window.resize(function() {");
            builder.append("initpage();})");
            builder.append(" \n function initpage() {");
            builder.append("var view_width = document.getElementsByTagName('html')[0].getBoundingClientRect().width;");
            builder.append("var _html = document.getElementsByTagName('html')[0];");
            builder.append("view_width > 640 ? _html.style.fontSize = 640 / 16 + 'px' : _html.style.fontSize = view_width / 16 + 'px';");
            builder.append("}}</script>");
            if (TextUtils.isEmpty(readyFun)) {
                int bodyIndex = html.indexOf("<body");
                html = html.replace("<body", "<body onload=\"_pageLoad();\" ");
            } else {
                if (!readyFun.contains("_pageLoad") && !readyFun.contains("_pageInit")) {
                    builder.append("function _pageInit(){_pageLoad();").append(readyFun).append("}");
                    html = html.replace(readyFun, "_pageInit();");
                }
            }
            builder.append(html);
            final String finalHtml = builder.toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isReloadHtml = true;
                    binding.nkitWv.loadDataWithBaseURL(historyUrl, finalHtml, "text/html", "utf-8", "");
                }
            });
        }
    }

    public static List<String> match(String source, String element, String attr) {
        List<String> result = new ArrayList<String>();
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }

    public void onChangeFontClick(View view) {
        binding.nkitWv.loadUrl("javascript:document.getElementsByTagName('html')[0].style.fontSize=16px;");
    }

    public void onGetHtmlClick(View view) {
        binding.nkitWv.loadUrl("javascript:debris.getHtmlContent(document.documentElement.outerHTML);");
    }
}