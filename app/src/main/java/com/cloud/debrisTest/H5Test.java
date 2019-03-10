package com.cloud.debrisTest;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cloud.components.themes.OnThemeViewKeyListener;
import com.cloud.debris.BaseFragmentActivity;
import com.cloud.debris.utils.RedirectUtils;
import com.cloud.debrisTest.databinding.H5ViewBinding;
import com.cloud.mixed.RxMixed;
import com.cloud.mixed.h5.JavascriptInterface;
import com.cloud.mixed.h5.OnH5ImageSelectedListener;
import com.cloud.mixed.h5.OnH5WebViewListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/5
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class H5Test extends BaseFragmentActivity implements OnThemeViewKeyListener, OnH5ImageSelectedListener {

    private H5ViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxMixed.getInstance().registerH5Listener(calls);
        binding = DataBindingUtil.setContentView(this, R.layout.h5_view);
        RxMixed.getInstance().setOnH5ImageSelectedListener(this);
        binding.headTtv.setOnThemeViewKeyListener(this);

        binding.h5Test.bindInterface("mibao");
//        binding.h5Test.load("http://www.slcore.com:201");
        binding.h5Test.load("http://www.slcore.com:201/upload_image.html");
    }

    @Override
    public void onKeyListener(View view, int id) {
        if (id == R.id.return_itv) {
            binding.h5Test.getSelectText();
        }
    }

    public OnH5WebViewListener calls = new OnH5WebViewListener() {
        @Override
        public void addUserAgent(List<String> userAgents) {
            //追加自定义代理
        }

        @Override
        public void onTitle(String title) {
            //获取到网页的标题
            binding.headTtv.setTitle(title);
        }

        @Override
        public boolean onUrlListener(String url) {
            //如果要做url拦截可在这里处理
            //返回true此链接不作渲染处理,false继续渲染;
            return false;
        }

        @android.webkit.JavascriptInterface
        @JavascriptInterface
        public String getToken() {
            //这里获取native登录的令牌
            return null;
        }

        @Override
        public void onLoaded(WebView view, boolean success, int errorCode, String description, String url) {
            //h5加载完成后回调
        }

        @Override
        public void nativeSchemeCall(String scheme) {
            //h5通过scheme调用native回调
            //调用方式:
            //1.url?scheme=[url encode编码后的scheme]或以第二种方式;
            //2.android调用cl_cloud_group_jsm.nativeSchemeCall(encodeURIComponent(schemeUrl));
            //ios调用window.webkit.messageHandlers.nativeSchemeCall.postMessage(encodeURIComponent(schemeUrl));
        }

        @Override
        public void getSelectText(String selectText) {
            //h5文本选择后回调
            //需要调用binding.h5Test.getSelectText();方法才能回调
        }

        @Override
        public void onCallTel(String tel) {
            //拨打电话功能需要在头部加上<meta name="format-detection" content="telephone=yes"/>
            RedirectUtils.startTel(getActivity(), tel);
        }

        @Override
        public void onCallSms(String sms) {
            //拨打电话功能需要在头部加上<meta name="format-detection" content="telephone=yes"/>
            RedirectUtils.startSms(getActivity(), sms);
        }

        @Override
        public void download(String url, String name) {
            //打开的链接若是apk、rar则会回调此方法
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //h5选择图片后回调并上传
        binding.h5Test.onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void openFileChooserImpl(ValueCallback<Uri> uploadMsg, ValueCallback<Uri[]> sdk5UploadMsg) {
        //h5选择图片回调
        binding.h5Test.selectLocalImages(getActivity());
    }
}
