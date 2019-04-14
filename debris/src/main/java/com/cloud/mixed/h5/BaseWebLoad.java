package com.cloud.mixed.h5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cloud.debris.R;
import com.cloud.dialogs.BaseMessageBox;
import com.cloud.dialogs.enums.DialogButtonsEnum;
import com.cloud.dialogs.enums.MsgBoxClickButtonEnum;
import com.cloud.images.beans.SelectImageProperties;
import com.cloud.images.figureset.ImageSelectDialog;
import com.cloud.mixed.RxMixed;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RuleParams;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.PixelUtils;
import com.cloud.objects.utils.ValidUtils;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseWebLoad extends WebView {

    private ProgressBar progressBar = null;
    private boolean isParseError = false;
    //初始是否已重载url
    private boolean isOverriedUrl = false;
    private ValueCallback<Uri> uploadMsg;
    private ValueCallback<Uri[]> sdk5UploadMsg;

    public BaseWebLoad(Context context) {
        super(context);
        onPreCreated(context);
        init();
        initListener();
    }

    public BaseWebLoad(Context context, AttributeSet attrs) {
        super(context, attrs);
        onPreCreated(context);
        init();
        initListener();
    }

    /**
     * 获取监听对象
     *
     * @param <L>
     * @return
     */
    protected <L extends OnH5WebViewListener> L getWebListener() {
        L listener = (L) RxMixed.getInstance().getH5Listener();
        return listener;
    }

    /**
     * 初始化webview窗口
     *
     * @param activity 当前h5窗口
     */
    public void initializa(Activity activity) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (window != null) {
            window.setFormat(PixelFormat.TRANSLUCENT);
            if (Build.VERSION.SDK_INT >= 11) {
                window.setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                );
            }
        }
    }

    private void init() {
        try {
            RelativeLayout.LayoutParams vparam = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, PixelUtils.dip2px(getContext(), 3));
            progressBar = new ProgressBar(getContext());
            progressBar.setMax(100);
            progressBar.setProgress(0);
//            progressBar.setScrollBarStyle(android.R.attr.progressBarStyleHorizontal);
            progressBar.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            progressBar.setLayoutParams(vparam);
            progressBar.setVisibility(GONE);
            Drawable mdrawable = progressBar.getResources().getDrawable(R.drawable.cl_progressbar_style);
            progressBar.setProgressDrawable(mdrawable);
            this.addView(progressBar);
            initSetting();
            //设置当前webview和父容器的layerType解决页面加载空白
            this.setLayerType(LAYER_TYPE_SOFTWARE, null);
            ViewParent parent = this.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.setLayerType(LAYER_TYPE_HARDWARE, null);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
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
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                settings.setSupportZoom(true);
                settings.setBuiltInZoomControls(true);
                settings.setUseWideViewPort(true);
                settings.setSupportMultipleWindows(true);
                settings.setLoadWithOverviewMode(true);
                settings.setAppCacheEnabled(true);
                settings.setDatabaseEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setGeolocationEnabled(true);
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
                File database = this.getContext().getDir("database", Context.MODE_PRIVATE);
                if (database != null) {
                    settings.setDatabasePath(database.getPath());
                }
                File geolocation = this.getContext().getDir("geolocation", Context.MODE_APPEND);
                if (geolocation != null) {
                    settings.setGeolocationDatabasePath(geolocation.getPath());
                }
                File appcache = this.getContext().getDir("appcache", Context.MODE_PRIVATE);
                if (appcache != null) {
                    settings.setAppCachePath(appcache.getPath());
                }
                settings.setBlockNetworkImage(false);
                settings.setLoadsImagesAutomatically(true);
                settings.setSavePassword(true);
                //add new user agent
                OnH5WebViewListener webListener = getWebListener();
                if (webListener != null) {
                    List<String> userAgents = new ArrayList<String>();
                    webListener.addUserAgent(userAgents);
                    //重新设置user agent
                    if (!ObjectJudge.isNullOrEmpty(userAgents)) {
                        String join = ConvertUtils.toJoin(userAgents, ";");
                        String agentString = settings.getUserAgentString();
                        settings.setUserAgent(String.format("%s;%s", join, agentString));
                    }
                }
            }
            if (RxMixed.getInstance().isInitedX5()) {
                CookieSyncManager.createInstance(getContext());
                CookieSyncManager.getInstance().sync();
            } else {
                android.webkit.CookieSyncManager.createInstance(getContext());
                android.webkit.CookieSyncManager.getInstance().sync();
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
                isOverriedUrl = true;
                return onOverrideUrlLoading(webView, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                //7.0以上执行
                isOverriedUrl = true;
                return onOverrideUrlLoading(webView, webResourceRequest.getUrl().toString());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                isParseError = true;
                onLoadFinished(view, false, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override
            public void onPageStarted(WebView webView, String url, Bitmap favicon) {
                progressBar.setVisibility(VISIBLE);
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                if (!isParseError) {
                    if (!isOverriedUrl) {
                        //解决首次加载时shouldOverrideUrlLoading不回调
                        //网上有些说重新调用loadUrl,但重新调用会有一定的性能问题
                        onOverrideUrlLoading(webView, url);
                    }
                    onLoadFinished(webView, true, 0, "", url);
                }
                progressBar.setProgress(0);
                progressBar.setVisibility(GONE);
            }
        });
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {

                mbox.setContentGravity(Gravity.LEFT);
                mbox.setContent(message);
                mbox.setShowTitle(false);
                mbox.setTarget("ON_JS_CONFIRM_TARGET", result);
                mbox.show(view.getContext(), DialogButtonsEnum.ConfirmCancel);
                return true;
            }

            private View createPromptEditView(Context context, String defaultText) {
                LinearLayout ll = new LinearLayout(context);
                LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        PixelUtils.dip2px(context, 32));
                EditText editText = new EditText(context);
                editText.setLayoutParams(llparam);
                editText.setPadding(2, 1, 2, 1);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                editText.setTextColor(Color.parseColor("#323232"));
                editText.setHintTextColor(Color.parseColor("#999999"));
                editText.setText(defaultText);
                ll.addView(editText);
                return ll;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                mbox.setContentGravity(Gravity.LEFT);
                mbox.setShowTitle(true);
                mbox.setTitle(message);
                mbox.setContentView(createPromptEditView(view.getContext(), defaultValue));
                Object[] extras = {result, defaultValue};
                mbox.setTarget("ON_JS_PROMPT_TARGET", extras);
                mbox.show(view.getContext(), DialogButtonsEnum.ConfirmCancel);
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                mbox.setContentGravity(Gravity.CENTER);
                mbox.setContent(message);
                mbox.setShowTitle(false);
                mbox.setTarget("ON_JS_ALERT_TARGET", result);
                mbox.show(view.getContext(), DialogButtonsEnum.Confirm);
                result.cancel();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                progressBar.postInvalidate();
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                BaseWebLoad.this.onReceivedTitle(title);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
                callback.invoke(origin, true, false);
            }

            //扩展浏览器上传文件
            //3.0++版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                if (BaseWebLoad.this.uploadMsg != null) {
                    finishFileUpload();
                }
                BaseWebLoad.this.uploadMsg = uploadMsg;
                OnH5ImageSelectedListener selectedListener = RxMixed.getInstance().getOnH5ImageSelectedListener();
                if (selectedListener == null) {
                    finishFileUpload();
                    return;
                }
                selectedListener.openFileChooserImpl(uploadMsg, null);
            }

            //3.0--版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                if (BaseWebLoad.this.uploadMsg != null) {
                    finishFileUpload();
                }
                BaseWebLoad.this.uploadMsg = uploadMsg;
                OnH5ImageSelectedListener selectedListener = RxMixed.getInstance().getOnH5ImageSelectedListener();
                if (selectedListener == null) {
                    finishFileUpload();
                    return;
                }
                selectedListener.openFileChooserImpl(uploadMsg, null);
            }

            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                if (BaseWebLoad.this.sdk5UploadMsg != null || BaseWebLoad.this.uploadMsg != null) {
                    finishFileUpload();
                }
                BaseWebLoad.this.uploadMsg = valueCallback;
                OnH5ImageSelectedListener selectedListener = RxMixed.getInstance().getOnH5ImageSelectedListener();
                if (selectedListener == null) {
                    //监听null时结束上传
                    finishFileUpload();
                    return;
                }
                selectedListener.openFileChooserImpl(valueCallback, null);
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                if (BaseWebLoad.this.uploadMsg != null || BaseWebLoad.this.sdk5UploadMsg != null) {
                    finishFileUpload();
                }
                BaseWebLoad.this.sdk5UploadMsg = uploadMsg;
                OnH5ImageSelectedListener selectedListener = RxMixed.getInstance().getOnH5ImageSelectedListener();
                if (selectedListener != null) {
                    selectedListener.openFileChooserImpl(null, uploadMsg);
                }
                return true;
            }
        });
        this.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {

            }
        });
    }

    /**
     * 上传图片
     *
     * @param selectImageProperties 已选择的图片
     */
    public void uploadFiles(List<SelectImageProperties> selectImageProperties) {
        if (ObjectJudge.isNullOrEmpty(selectImageProperties)) {
            finishFileUpload();
            return;
        }
        try {
            //7.0之前手机只能逐张上传图片
            SelectImageProperties properties = selectImageProperties.get(0);
            Uri uri = Uri.parse(properties.getImagePath());
            if (uploadMsg != null) {
                uploadMsg.onReceiveValue(uri);
                uploadMsg = null;
                //回调function $_cl_upload_native_file(path){}js方法方便h5处理
                this.loadUrl("javascript:window.cl_upload_native_file('" + properties.getImagePath() + "');");
            } else if (sdk5UploadMsg != null) {
                Uri[] uris = new Uri[1];
                uris[0] = uri;
                sdk5UploadMsg.onReceiveValue(uris);
                sdk5UploadMsg = null;
                //回调function $_cl_upload_native_file(path){}js方法方便h5处理
                this.loadUrl("javascript:window.cl_upload_native_file('" + properties.getImagePath() + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //结束后需要重置上传，否则h5调用native回调只能执行一次
            finishFileUpload();
        }
    }

    /**
     * 完成或取消文件上传
     */
    private void finishFileUpload() {
        if (uploadMsg != null) {
            uploadMsg.onReceiveValue(null);
        } else if (sdk5UploadMsg != null) {
            sdk5UploadMsg.onReceiveValue(null);
        }
        uploadMsg = null;
        sdk5UploadMsg = null;
    }

    private BaseMessageBox mbox = new BaseMessageBox() {
        @Override
        public boolean onItemClickListener(View v, MsgBoxClickButtonEnum mcbenum, String target, Object extraData) {
            if (TextUtils.equals(target, "ON_JS_CONFIRM_TARGET")) {
                JsResult result = (JsResult) extraData;
                if (result == null) {
                    if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                        result.confirm();
                    } else {
                        result.cancel();
                    }
                }
            } else if (TextUtils.equals(target, "ON_JS_PROMPT_TARGET")) {
                if (extraData == null) {
                    dismiss();
                } else {
                    if (extraData instanceof Object[]) {
                        Object[] extras = (Object[]) extraData;
                        if (extras == null || extras.length != 2) {
                            dismiss();
                        } else {
                            JsPromptResult promptResult = (JsPromptResult) extras[0];
                            String defaultText = String.valueOf(extras[1]);
                            if (promptResult != null && !TextUtils.isEmpty(defaultText)) {
                                if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                                    promptResult.confirm(defaultText);
                                } else {
                                    promptResult.cancel();
                                }
                            } else {
                                dismiss();
                            }
                        }
                    } else {
                        dismiss();
                    }
                }
            } else if (TextUtils.equals(target, "ON_JS_ALERT_TARGET")) {
                if (extraData == null) {
                    dismiss();
                } else {
                    JsResult result = (JsResult) extraData;
                    result.confirm();
                }
            } else {
                dismiss();
            }
            return true;
        }
    };

    /**
     * 销毁webviewz
     */
    public void onDestroy() {
        mbox.dismiss();
        this.destroy();
    }

    /**
     * 在webview初始化前回调
     *
     * @param context 当前上下文
     */
    protected void onPreCreated(Context context) {

    }

    /**
     * 对WebViewClient的onOverrideUrlLoading进行重写
     *
     * @param webView 当前webview对象
     * @param url     要加载的url
     * @return
     */
    protected boolean onOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    /**
     * webview加载完成后回调
     *
     * @param webView     当前webview对象
     * @param success     true-加载成功;false-加载失败;
     * @param errorCode   success=false时返回的错误码
     * @param description success=false时返回的错误信息
     * @param failingUrl  success=false时加载失败的url
     */
    protected void onLoadFinished(WebView webView, boolean success, int errorCode, String description, String failingUrl) {

    }

    /**
     * 已接收到webview标题的回调
     *
     * @param title webview标题
     */
    protected void onReceivedTitle(String title) {

    }

    /**
     * 获取额外头数据
     *
     * @return
     */
    protected Map<String, String> getExtraHeaders(
            Map<String, String> extraHeaders) {
        return extraHeaders;
    }

    /**
     * 以post方式加载url和提交数据
     *
     * @param url      要加载的url
     * @param postData 附带的数据
     */
    public void postUrl(String url, HashMap<String, String> postData) {
        try {
            isParseError = false;
            String data = "";
            if (!ObjectJudge.isNullOrEmpty(postData)) {
                Iterator<Map.Entry<String, String>> iter = postData
                        .entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
                            .next();
                    sb.append(String.format("%s=%s&", entry.getKey(),
                            URLEncoder.encode(entry.getValue(), "utf-8")));
                }
                if (sb.length() > 0) {
                    data = sb.substring(0, sb.length() - 1);
                }
            }
            if (!TextUtils.isEmpty(data)) {
                this.postUrl(url, data.getBytes());
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 以post方式加载url和提交数据
     *
     * @param url 要加载的url
     */
    public void post(String url) {
        //第二个类型传换成HashMap，解决与webview.postUrl(String url,byte[] bytes)冲突
        postUrl(url, (HashMap<String, String>) null);
    }

    /**
     * 加载url
     *
     * @param url          完整url
     * @param extraHeaders 附带的头数据
     */
    public void load(String url, Map<String, String> extraHeaders) {
        try {
            isParseError = false;
            if (extraHeaders == null) {
                extraHeaders = new HashMap<String, String>();
            }
            Map<String, String> headersdata = getExtraHeaders(extraHeaders);
            if (headersdata == null) {
                super.loadDataWithBaseURL(url, "", "text/html", "utf-8", "");
            } else {
                super.loadUrl(url, headersdata);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 加载url
     *
     * @param url 完整url
     */
    public void load(String url) {
        loadUrl(url, null);
    }

    /**
     * 加载html数据
     *
     * @param htmlContent html数据
     */
    public void loadData(String htmlContent) {
        try {
            isParseError = false;
            if (htmlContent.contains("<html>")) {
                super.loadDataWithBaseURL("", htmlContent, "text/html", "utf-8", "");
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("<!DOCTYPE html>");
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<meta charset=\"utf-8\"/>");
                sb.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\"/>");
                sb.append("<style type=\"text/css\">body,div,ul,li {padding: 0;margin: 0;display: block;}");
                sb.append("img{max-width:100% !important; width:auto; height:auto;}</style>");
                sb.append("</head>");
                sb.append("<body>");
                sb.append(htmlContent);
                sb.append("</body>");
                sb.append("</html>");
                sb.append("<script type=\"text/javascript\">window.onload = function() {var imgs = document.getElementsByTagName('img');for(var i in imgs) {imgs[i].style.maxWidth = '100% !important';}};</script>");
                super.loadDataWithBaseURL("", sb.toString(), "text/html", "utf-8", "");
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 回退web或结束当前窗口
     *
     * @param activity
     * @param flag                   true直接结束当前窗口,false先回退再结束
     * @param finishOrGoBackListener 关闭h5页面或回退时回调
     */
    public void finishOrGoBack(Activity activity, boolean flag, OnFinishOrGoBackListener finishOrGoBackListener) {
        try {
            if (flag) {
                activity.finish();
            } else {
                boolean canGoBack = this.canGoBack();
                if (canGoBack) {
                    this.goBack();
                    if (finishOrGoBackListener != null) {
                        finishOrGoBackListener.onFinishOrGoBack(canGoBack);
                    }
                } else {
                    if (finishOrGoBackListener != null) {
                        finishOrGoBackListener.onFinishOrGoBack(false);
                    } else {
                        activity.finish();
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 回退web或结束当前窗口
     *
     * @param activity
     */
    public void finishOrGoBack(Activity activity) {
        finishOrGoBack(activity, true, null);
    }

    /**
     * 背影颜色与当前颜色是否匹配
     *
     * @param bgcolor   背影颜色
     * @param thisColor 当前颜色
     * @return true-匹配;false-不匹配;
     */
    protected boolean isMatchThisColor(String bgcolor, String thisColor) {
        try {
            if (TextUtils.isEmpty(bgcolor)) {
                return false;
            }
            String pattern = String.format(RuleParams.MatchTagBetweenContent.getValue(), "\\(", "\\)");
            String text = ValidUtils.matche(pattern, bgcolor);
            String mcolor = "";
            if (TextUtils.isEmpty(text)) {
                mcolor = bgcolor;
            } else {
                int r = 0, g = 0, b = 0;
                String[] rgbs = text.split(",");
                if (rgbs.length == 3) {
                    r = ConvertUtils.toInt(rgbs[0]);
                    g = ConvertUtils.toInt(rgbs[1]);
                    b = ConvertUtils.toInt(rgbs[2]);
                    mcolor = ConvertUtils.toRGBHex(r, g, b);
                } else if (rgbs.length == 4) {
                    if (TextUtils.equals(rgbs[0], "0")) {
                        mcolor = thisColor;
                    }
                }
            }
            boolean isWhite = false;
            if (!TextUtils.isEmpty(mcolor)) {
                mcolor = mcolor.toLowerCase();
                if (TextUtils.equals(mcolor, thisColor)) {
                    isWhite = true;
                }
            }
            return isWhite;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取选择的文本
     */
    public void getSelectText() {
        this.loadUrl("javascript:window.cl_cloud_group_jsm.getSelectText(window.getSelection?window.getSelection().toString():document.selection.createRange().text);");
    }

    /**
     * 需在Activity的onActivityResult回调
     *
     * @param activity    activity
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            //页面被取消操作
            //结束后需要重置上传，否则h5调用native回调只能执行一次
            finishFileUpload();
        }
        imageSelectDialog.onActivityResult(activity, requestCode, resultCode, data);
    }

    private ImageSelectDialog imageSelectDialog = new ImageSelectDialog() {
        @Override
        protected void onSelectCompleted(List<SelectImageProperties> selectImageProperties, Object extra) {
            uploadFiles(selectImageProperties);
        }
    };

    /**
     * 选择本地图片
     *
     * @param activity FragmentActivity
     */
    public void selectLocalImages(final FragmentActivity activity) {
        HandlerManager.getInstance().post(new RunnableParamsN<Object>() {
            @Override
            public void run(Object... objects) {
                //选择后图片最大压缩大小
                imageSelectDialog.setMaxFileSize(1024);
                //最多选择图片数量
                imageSelectDialog.setMaxSelectNumber(1);
                //是否显示拍照选项
                imageSelectDialog.setShowTakingPictures(true);
                //显示图片选择
                imageSelectDialog.show(activity);
            }
        });
    }
}
