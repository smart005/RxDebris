package com.cloud.mixed.h5;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.cloud.cache.DerivedCache;
import com.cloud.mixed.abstracts.OnBridgeAbstract;
import com.cloud.mixed.h5.events.OnWebActivityListener;
import com.cloud.mixed.h5.events.OnWebViewListener;
import com.cloud.mixed.h5.events.OnWebViewPartCycle;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.ConvertUtils;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-05-19
 * Description: x5 WebView
 * Modifier:
 * ModifyContent:
 */
class X5Webview extends WebView implements OnWebViewPartCycle {

    private OnBridgeAbstract onBridgeAbstract;
    private OnWebActivityListener onWebActivityListener;
    //web view call
    private OnWebViewListener onWebViewListener;
    private boolean isAutoPlayAudioVideo;
    private boolean isLoadFinish;

    public X5Webview(Context context, OnBridgeAbstract bridgeAbstract, OnWebActivityListener webActivityListener, OnWebViewListener onWebViewListener) {
        super(context);
        this.onBridgeAbstract = bridgeAbstract;
        this.onWebActivityListener = webActivityListener;
        this.onWebViewListener = onWebViewListener;
        this.isLoadFinish = false;
        String clsName = context.getClass().getName();
        String apvkey = String.format("%s_isAutoPlayAudioVideo", clsName);
        isAutoPlayAudioVideo = DerivedCache.getInstance().getBoolean(apvkey);
        initSetting();
        initListener();
    }

    private void initSetting() {
        try {
            this.requestFocus();
            this.setVerticalScrollbarOverlay(false);
            this.setHorizontalScrollbarOverlay(false);
            this.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            this.removeJavascriptInterface("searchBoxJavaBridge_");
            this.removeJavascriptInterface("accessibilityTra");
            this.removeJavascriptInterface("accessibility");

            WebSettings settings = this.getSettings();
            if (settings != null) {
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                //是否可访问本地文件，默认值true
                settings.setAllowFileAccess(true);
                //NARROW_COLUMNS
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                settings.setSupportZoom(false);
                settings.setBuiltInZoomControls(false);
                settings.setUseWideViewPort(true);
                settings.setSupportMultipleWindows(true);
                settings.setLoadWithOverviewMode(true);
                settings.setAppCacheEnabled(true);
                settings.setDatabaseEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setGeolocationEnabled(true);
                settings.setNeedInitialFocus(true);
                settings.setAppCacheMaxSize(Long.MAX_VALUE);
                settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
                settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
                settings.setDefaultTextEncodingName("utf-8");
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                //是否可访问Content Provider的资源，默认值true
                settings.setAllowContentAccess(true);
                //允许webview对文件的操作
                settings.setAllowUniversalAccessFromFileURLs(true);
                settings.setAllowFileAccessFromFileURLs(true);
                settings.setBlockNetworkImage(false);
                settings.setLoadsImagesAutomatically(true);
                settings.setSavePassword(true);
                settings.setTextZoom(100);
                //add new user agent
                if (onBridgeAbstract != null) {
                    List<String> userAgents = new ArrayList<String>();
                    onBridgeAbstract.addUserAgent(userAgents);
                    //重新设置user agent
                    if (!ObjectJudge.isNullOrEmpty(userAgents)) {
                        String join = ConvertUtils.toJoin(userAgents, ";");
                        String agentString = settings.getUserAgentString();
                        settings.setUserAgent(String.format("%s;%s", join, agentString));
                    }
                }
                if (isAutoPlayAudioVideo) {
                    settings.setMediaPlaybackRequiresUserGesture(false);
                }
                if (onWebActivityListener != null) {
                    onWebActivityListener.onSettingModified(settings);
                }
                //http和https混合使用
                //MIXED_CONTENT_NEVER_ALLOW：Webview不允许一个安全的站点（https）去加载非安全的站点内容（http）,比如，https网页内容的图片是http链接。强烈建议App使用这种模式，因为这样更安全。
                //MIXED_CONTENT_ALWAYS_ALLOW：在这种模式下，WebView是可以在一个安全的站点（Https）里加载非安全的站点内容（Http）,这是WebView最不安全的操作模式，尽可能地不要使用这种模式。
                //MIXED_CONTENT_COMPATIBILITY_MODE：在这种模式下，当涉及到混合式内容时，WebView会尝试去兼容最新Web浏览器的风格。一些不安全的内容（Http）能被加载到一个安全的站点上（Https），而其他类型的内容将会被阻塞。这些内容的类型是被允许加载还是被阻塞可能会随着版本的不同而改变，并没有明确的定义。这种模式主要用于在App里面不能控制内容的渲染，但是又希望在一个安全的环境下运行。
                //在Android5.0以下，默认是采用的MIXED_CONTENT_ALWAYS_ALLOW模式，即总是允许WebView同时加载Https和Http；
                //而从Android5.0开始，默认用MIXED_CONTENT_NEVER_ALLOW模式，即总是不允许WebView同时加载Https和Http。
                if (Build.VERSION.SDK_INT >= 21) {
                    settings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
                }
            }
            this.setClickable(true);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private void initListener() {
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                return onWebViewListener.shouldOverrideUrlLoading(webView, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                return onWebViewListener.shouldOverrideUrlLoading(webView, webResourceRequest);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                onWebViewListener.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap favicon) {
                onWebViewListener.onPageStarted(webView, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                isLoadFinish = true;
                if (isAutoPlayAudioVideo) {
                    view.loadUrl("javascript:document.querySelector('video').play();");
                }
                onWebViewListener.onPageFinished(view, url);
            }
        });
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return onWebViewListener.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return onWebViewListener.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return onWebViewListener.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                onWebViewListener.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                onWebViewListener.onReceivedTitle(view, title);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
                callback.invoke(origin, true, false);
            }

            //<3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                onWebViewListener.openFileChooser(uploadMsg);
            }

            //>=3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                onWebViewListener.openFileChooser(uploadMsg, acceptType);
            }

            //>=4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                onWebViewListener.openFileChooser(valueCallback, acceptType, capture);
            }

            //>=5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                return onWebViewListener.onShowFileChooser(webView, uploadMsg, fileChooserParams);
            }
        });
        this.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {

            }
        });
    }

    @Override
    public void onDestory() {
        this.destroy();
    }

    public void setAutoPlayAudioVideo(boolean isAutoPlayAudioVideo) {
        this.isAutoPlayAudioVideo = isAutoPlayAudioVideo;
        if (this.isLoadFinish) {
            if (isAutoPlayAudioVideo && Build.VERSION.SDK_INT >= 17) {
                WebSettings settings = getSettings();
                settings.setMediaPlaybackRequiresUserGesture(false);
            }
            this.loadUrl("javascript:document.querySelector('video').play();");
        }
    }
}
