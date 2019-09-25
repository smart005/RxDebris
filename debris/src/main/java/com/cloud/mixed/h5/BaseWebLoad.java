package com.cloud.mixed.h5;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cloud.coms.dialogs.BaseMessageBox;
import com.cloud.coms.dialogs.enums.DialogButtonsEnum;
import com.cloud.coms.dialogs.enums.MsgBoxClickButtonEnum;
import com.cloud.dataprocessor.events.OnScriptRegisterBox;
import com.cloud.debris.R;
import com.cloud.debris.bundle.RedirectUtils;
import com.cloud.ebus.EBus;
import com.cloud.images.beans.SelectImageProperties;
import com.cloud.images.figureset.ImageSelectDialog;
import com.cloud.mixed.abstracts.OnBridgeAbstract;
import com.cloud.mixed.annotations.HybridBridges;
import com.cloud.mixed.annotations.HybridLogicBridge;
import com.cloud.mixed.h5.events.OnFinishOrGoBackListener;
import com.cloud.mixed.h5.events.OnH5ImageSelectedListener;
import com.cloud.mixed.h5.events.OnWebActivityListener;
import com.cloud.mixed.h5.events.OnWebCookieListener;
import com.cloud.mixed.h5.events.OnWebViewListener;
import com.cloud.mixed.h5.events.OnWebViewPartCycle;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.enums.RuleParams;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.CrashUtils;
import com.cloud.objects.logs.Logger;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.PixelUtils;
import com.cloud.objects.utils.ValidUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/17
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseWebLoad extends RelativeLayout implements OnWebViewListener {

    private ProgressBar progressBar = null;
    private boolean isParseError = false;
    //初始是否已重载url
    private boolean isOverriedUrl = false;
    private ValueCallback<Uri> uploadMsg;
    private ValueCallback<Uri[]> sdk5UploadMsg;
    //是否加载成功
    private boolean isLoadSuccess = false;
    //是否x5浏览器
    private boolean isX5 = true;
    private OnWebViewPartCycle onWebViewPartCycle;
    private X5Webview x5Webview;
    private WKWebview webview;
    private OnH5ImageSelectedListener onH5ImageSelectedListener;
    private OnBridgeAbstract onBridgeAbstract;
    private OnWebActivityListener onWebActivityListener;
    //bridgeNames
    protected HashMap<String, OnScriptRegisterBox> bridgeRegisterMap = new HashMap<>();
    //bridge objects
    protected HybridBridges declaredAnnotation;
    /**
     * 是否选择原图(默认true)
     */
    private boolean isOriginalImage = true;

    public BaseWebLoad(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.H5WebView);
        isX5 = a.getBoolean(R.styleable.H5WebView_wv_isX5, true);
        a.recycle();
        bindBridgeAnnotation(context);
        initBridgeEvents(context);
        initActivityListener(context);
        init();
        EBus.getInstance().registered(this);
    }

    private void initActivityListener(Context context) {
        if (!(context instanceof OnWebActivityListener)) {
            return;
        }
        onWebActivityListener = (OnWebActivityListener) context;
    }

    public OnBridgeAbstract getOnBridgeAbstract() {
        return this.onBridgeAbstract;
    }

    protected OnWebActivityListener getOnWebActivityListener() {
        return this.onWebActivityListener;
    }

    private void bindBridgeAnnotation(Context context) {
        if (declaredAnnotation != null) {
            return;
        }
        Class<? extends Context> contextClass = context.getClass();
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            declaredAnnotation = contextClass.getDeclaredAnnotation(HybridBridges.class);
        } else {
            Annotation[] declaredAnnotations = contextClass.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (annotation instanceof HybridBridges) {
                    declaredAnnotation = (HybridBridges) annotation;
                    break;
                }
            }
        }
    }

    private void initBridgeEvents(Context context) {
        if (onBridgeAbstract == null) {
            if (declaredAnnotation == null) {
                return;
            }
            HybridLogicBridge[] values = declaredAnnotation.values();
            for (HybridLogicBridge logicBridge : values) {
                if (logicBridge.isBasisBridge()) {
                    Object callObject = JsonUtils.newNull(logicBridge.bridgeClass());
                    if (!(callObject instanceof OnBridgeAbstract)) {
                        return;
                    }
                    onBridgeAbstract = (OnBridgeAbstract) callObject;
                    break;
                }
            }
        }
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
            window.setFlags(
                    android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            );
        }
    }

    private void init() {
        try {
            RelativeLayout.LayoutParams wvparam = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            //添加webview
            if (isX5) {
                X5Webview webview = new X5Webview(getContext(), onBridgeAbstract, onWebActivityListener, this);
                this.onWebViewPartCycle = webview;
                this.x5Webview = webview;
                this.addView(webview, wvparam);
            } else {
                WKWebview webview = new WKWebview(getContext(), onBridgeAbstract, onWebActivityListener, this);
                this.onWebViewPartCycle = webview;
                this.webview = webview;
                this.addView(webview, wvparam);
            }
            RelativeLayout.LayoutParams vparam = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, PixelUtils.dip2px(3));
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
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 设置启动硬件加速
     * (默认5.0以上6.0以下禁用，6.0以上启用)
     * if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
     * super.setEnableHardwareAcceleration(false);
     * } else if (Build.VERSION.SDK_INT >= 23) {
     * super.setEnableHardwareAcceleration(true);
     * }
     *
     * @param enable true-启用硬件加速,false-禁用硬件加速;
     */
    public void setEnableHardwareAcceleration(boolean enable) {
        try {
            if (enable) {
                if (isX5) {
                    if (x5Webview != null) {
                        x5Webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    }
                } else {
                    if (webview != null) {
                        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    }
                }
            } else {
                if (isX5) {
                    if (x5Webview != null) {
                        x5Webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }
                } else {
                    if (webview != null) {
                        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }
                }
            }
        } catch (Exception e) {
            Logger.warn("rxlog", CrashUtils.getCrashInfo(e));
        }
    }

    /**
     * 绑定cookies
     *
     * @param listener cookie相关属性构建监听
     */
    public void bindCookies(OnWebCookieListener listener) {
        if (listener == null) {
            return;
        }
        try {
            HashMap<String, String> cookies = listener.onWebCookies();
            if (ObjectJudge.isNullOrEmpty(cookies)) {
                return;
            }
            if (isX5) {
                CookieSyncManager.createInstance(getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                for (Map.Entry<String, String> entry : cookies.entrySet()) {
                    cookieManager.setCookie(entry.getKey(), entry.getValue());
                }
                CookieSyncManager.getInstance().sync();
            } else {
                android.webkit.CookieSyncManager.createInstance(getContext());
                android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
                for (Map.Entry<String, String> entry : cookies.entrySet()) {
                    cookieManager.setCookie(entry.getKey(), entry.getValue());
                }
                android.webkit.CookieSyncManager.getInstance().sync();
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(Object view, String url) {
        isOverriedUrl = true;
        View webView = isX5 ? ((com.tencent.smtt.sdk.WebView) view) : ((android.webkit.WebView) view);
        boolean b = onOverrideUrlLoading(webView, url);
        if (!b) {
            isLoadSuccess = false;
        }
        return b;
    }

    @Override
    public boolean shouldOverrideUrlLoading(Object view, Object webResourceRequest) {
        //7.0以上执行
        isOverriedUrl = true;
        View webView = isX5 ? ((com.tencent.smtt.sdk.WebView) view) : ((android.webkit.WebView) view);
        String url;
        if (isX5) {
            com.tencent.smtt.export.external.interfaces.WebResourceRequest request = (com.tencent.smtt.export.external.interfaces.WebResourceRequest) webResourceRequest;
            url = request.getUrl().toString();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                android.webkit.WebResourceRequest request = (android.webkit.WebResourceRequest) webResourceRequest;
                url = request.getUrl().toString();
            } else {
                url = ((android.webkit.WebView) view).getUrl();
            }
        }
        return onOverrideUrlLoading(webView, url);
    }

    @Override
    public void onReceivedError(Object view, int errorCode, String description, String failingUrl) {
        isParseError = true;
        onLoadFinished(false, errorCode, description, failingUrl);
    }

    @Override
    public void onPageStarted(Object view, String url, Bitmap favicon) {
        progressBar.setVisibility(VISIBLE);
    }

    /**
     * 设置图片选择监听
     *
     * @param listener OnH5ImageSelectedListener
     */
    public void setOnH5ImageSelectedListener(OnH5ImageSelectedListener listener) {
        this.onH5ImageSelectedListener = listener;
    }

    @Override
    public void onPageFinished(Object view, String url) {
        if (!isParseError) {
            View webView = isX5 ? ((com.tencent.smtt.sdk.WebView) view) : ((android.webkit.WebView) view);
            if (!isOverriedUrl) {
                //解决首次加载时shouldOverrideUrlLoading不回调
                //网上有些说重新调用loadUrl,但重新调用会有一定的性能问题
                onOverrideUrlLoading(webView, url);
            }
            isLoadSuccess = true;
            onLoadFinished(true, 0, "", url);
        }
        progressBar.setProgress(0);
        progressBar.setVisibility(GONE);
    }

    @Override
    public boolean onJsConfirm(Object view, String url, String message, Object result) {
        mbox.setContentGravity(Gravity.LEFT);
        mbox.setContent(message);
        mbox.setShowTitle(false);
        mbox.setShowClose(false);
        mbox.setTarget("ON_JS_CONFIRM_TARGET", result);
        mbox.show(getContext(), DialogButtonsEnum.ConfirmCancel);
        return true;
    }

    private View createPromptEditView(Context context, String defaultText) {
        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                PixelUtils.dip2px(32));
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
    public boolean onJsPrompt(Object view, String url, String message, String defaultValue, Object result) {
        mbox.setContentGravity(Gravity.LEFT);
        mbox.setShowTitle(true);
        mbox.setShowClose(false);
        mbox.setTitle(message);
        mbox.setContentView(createPromptEditView(getContext(), defaultValue));
        Object[] extras = {result, defaultValue};
        mbox.setTarget("ON_JS_PROMPT_TARGET", extras);
        mbox.show(getContext(), DialogButtonsEnum.ConfirmCancel);
        return true;
    }

    @Override
    public boolean onJsAlert(Object view, String url, String message, Object result) {
        mbox.setContentGravity(Gravity.CENTER);
        mbox.setContent(message);
        mbox.setShowTitle(false);
        mbox.setShowClose(false);
        mbox.setTarget("ON_JS_ALERT_TARGET", result);
        mbox.show(getContext(), DialogButtonsEnum.Confirm);
        return true;
    }

    @Override
    public void onProgressChanged(Object view, int newProgress) {
        progressBar.setProgress(newProgress);
        progressBar.postInvalidate();
    }

    @Override
    public void onReceivedTitle(Object view, String title) {
        BaseWebLoad.this.onReceivedTitle(title);
    }

    @Override
    public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg) {
        if (BaseWebLoad.this.uploadMsg != null) {
            finishFileUpload();
        }
        BaseWebLoad.this.uploadMsg = uploadMsg;
        if (onH5ImageSelectedListener == null) {
            finishFileUpload();
            return;
        }
        onH5ImageSelectedListener.openFileChooserImpl(uploadMsg, null);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (BaseWebLoad.this.uploadMsg != null) {
            finishFileUpload();
        }
        BaseWebLoad.this.uploadMsg = uploadMsg;
        if (onH5ImageSelectedListener == null) {
            finishFileUpload();
            return;
        }
        onH5ImageSelectedListener.openFileChooserImpl(uploadMsg, null);
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        if (BaseWebLoad.this.sdk5UploadMsg != null || BaseWebLoad.this.uploadMsg != null) {
            finishFileUpload();
        }
        BaseWebLoad.this.uploadMsg = valueCallback;
        if (onH5ImageSelectedListener == null) {
            //监听null时结束上传
            finishFileUpload();
            return;
        }
        onH5ImageSelectedListener.openFileChooserImpl(valueCallback, null);
    }

    @Override
    public boolean onShowFileChooser(Object view, ValueCallback<Uri[]> uploadMsg, Object fileChooserParams) {
        if (BaseWebLoad.this.uploadMsg != null || BaseWebLoad.this.sdk5UploadMsg != null) {
            finishFileUpload();
        }
        BaseWebLoad.this.sdk5UploadMsg = uploadMsg;
        if (onH5ImageSelectedListener != null) {
            onH5ImageSelectedListener.openFileChooserImpl(null, uploadMsg);
        }
        return true;
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
            File file = new File(properties.getImagePath());
            Uri uri = Uri.fromFile(file);
            if (uploadMsg != null) {
                uploadMsg.onReceiveValue(uri);
                uploadMsg = null;
                //回调function $_cl_upload_native_file(path){}js方法方便h5处理
                //this.loadUrl("javascript:window.cl_upload_native_file('" + properties.getImagePath() + "');");
            } else if (sdk5UploadMsg != null) {
                Uri[] uris = new Uri[1];
                uris[0] = uri;
                sdk5UploadMsg.onReceiveValue(uris);
                sdk5UploadMsg = null;
                //回调function $_cl_upload_native_file(path){}js方法方便h5处理
                //this.loadUrl("javascript:window.cl_upload_native_file('" + properties.getImagePath() + "');");
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
                if (result != null) {
                    if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                        result.confirm();
                    } else {
                        result.cancel();
                    }
                }
            } else if (TextUtils.equals(target, "ON_JS_PROMPT_TARGET")) {
                if (extraData != null) {
                    if (extraData instanceof Object[]) {
                        Object[] extras = (Object[]) extraData;
                        if (extras != null && extras.length == 2) {
                            JsPromptResult promptResult = (JsPromptResult) extras[0];
                            String defaultText = String.valueOf(extras[1]);
                            if (promptResult != null && !TextUtils.isEmpty(defaultText)) {
                                if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                                    promptResult.confirm(defaultText);
                                } else {
                                    promptResult.cancel();
                                }
                            }
                        }
                    }
                }
            } else if (TextUtils.equals(target, "ON_JS_ALERT_TARGET")) {
                if (extraData != null) {
                    JsResult result = (JsResult) extraData;
                    result.confirm();
                }
            } else if (TextUtils.equals(target, "album_permission")) {
                if (mcbenum == MsgBoxClickButtonEnum.Confirm) {
                    if (extraData instanceof Activity) {
                        Activity activity = (Activity) extraData;
                        RedirectUtils.startAppSettings(activity);
                    }
                }
            }
            return true;
        }
    };

    /**
     * 销毁webview
     */
    public void destroy() {
        try {
            mbox.dismiss();
            if (onWebViewPartCycle != null) {
                onWebViewPartCycle.onDestory();
            }
            EBus.getInstance().unregister(this);
            for (Map.Entry<String, OnScriptRegisterBox> entry : bridgeRegisterMap.entrySet()) {
                try {
                    if (isX5) {
                        x5Webview.removeJavascriptInterface(entry.getKey());
                    } else {
                        webview.removeJavascriptInterface(entry.getKey());
                    }
                } catch (Exception e) {
                    Logger.warn(e.getMessage());
                }
            }
            bridgeRegisterMap.clear();
            if (isX5) {
                if (x5Webview != null) {
                    x5Webview.setVisibility(View.GONE);
                }
            } else {
                if (webview != null) {
                    webview.setVisibility(View.GONE);
                }
            }
            //webview延迟1秒销毁,解决Receiver not registered: android.widget.ZoomButtonsController bug
            HandlerManager.getInstance().postDelayed(new RunnableParamsN<Object>() {
                @Override
                public void run(Object... objects) {
                    if (isX5) {
                        if (x5Webview != null) {
                            x5Webview.onDestory();
                        }
                    } else {
                        if (webview != null) {
                            webview.onDestory();
                        }
                    }
                }
            }, 500);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * 对WebViewClient的onOverrideUrlLoading进行重写
     *
     * @param view 当前webview对象
     * @param url  要加载的url
     * @return
     */
    protected boolean onOverrideUrlLoading(View view, String url) {
        return false;
    }

    /**
     * webview加载完成后回调
     *
     * @param success     true-加载成功;false-加载失败;
     * @param errorCode   success=false时返回的错误码
     * @param description success=false时返回的错误信息
     * @param failingUrl  success=false时加载失败的url
     */
    protected void onLoadFinished(boolean success, int errorCode, String description, String failingUrl) {

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
                if (isX5) {
                    x5Webview.postUrl(url, data.getBytes());
                } else {
                    webview.postUrl(url, data.getBytes());
                }
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
                if (isX5) {
                    x5Webview.loadDataWithBaseURL(url, "", "text/html", "utf-8", "");
                } else {
                    webview.loadDataWithBaseURL(url, "", "text/html", "utf-8", "");
                }
            } else {
                if (isX5) {
                    x5Webview.loadUrl(url, headersdata);
                } else {
                    webview.loadUrl(url, headersdata);
                }
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
        if (isX5) {
            x5Webview.loadUrl(url, null);
        } else {
            webview.loadUrl(url, null);
        }
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
                if (isX5) {
                    x5Webview.loadDataWithBaseURL("", htmlContent, "text/html", "utf-8", "");
                } else {
                    webview.loadDataWithBaseURL("", htmlContent, "text/html", "utf-8", "");
                }
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("<!DOCTYPE html>");
                sb.append("<html>");
                sb.append("<head>");
                sb.append("<meta charset=\"utf-8\"/>");
                sb.append("<meta name=\"viewport\" content=\"width=320,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\"/>");
                sb.append("<style type=\"text/css\">body,div,ul,li {padding: 0;margin: 0;display: block;}");
                sb.append("img{max-width:100% !important; width:auto; height:auto;}</style>");
                sb.append("</head>");
                sb.append("<body>");
                sb.append(htmlContent);
                sb.append("</body>");
                sb.append("</html>");
                sb.append("<script type=\"text/javascript\">");
                sb.append("window.onload = function() {");
                sb.append("var maxwidth = document.body.clientWidth;");
                sb.append("var imgs = document.getElementsByTagName('img');");
                sb.append("for(var i in imgs) {");
                sb.append("imgs[i].style.width = maxwidth+'px';");
                sb.append("}");
                sb.append("var sections = document.getElementsByTagName('section');");
                sb.append("for(var i in sections) {");
                sb.append("sections[i].style.width = maxwidth+'px';");
                sb.append("}");
                sb.append("document.body.style.width=maxwidth;};</script>");
                if (isX5) {
                    x5Webview.loadDataWithBaseURL("", sb.toString(), "text/html", "utf-8", "");
                } else {
                    webview.loadDataWithBaseURL("", sb.toString(), "text/html", "utf-8", "");
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
     * @param flag                   true直接结束当前窗口,false先回退再结束
     * @param finishOrGoBackListener 关闭h5页面或回退时回调
     */
    public void finishOrGoBack(Activity activity, boolean flag, OnFinishOrGoBackListener finishOrGoBackListener) {
        try {
            if (flag) {
                activity.finish();
            } else {
                boolean canGoBack = isX5 ? x5Webview.canGoBack() : webview.canGoBack();
                String url = (isX5 ? x5Webview.getUrl() : webview.getUrl());
                if (canGoBack) {
                    if (isX5) {
                        x5Webview.goBack();
                    } else {
                        webview.goBack();
                    }
                    if (finishOrGoBackListener != null) {
                        finishOrGoBackListener.onFinishOrGoBack(canGoBack, url);
                    }
                } else {
                    if (finishOrGoBackListener != null) {
                        finishOrGoBackListener.onFinishOrGoBack(false, url);
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
        String script = "javascript:window.cl_cloud_group_jsm.getSelectText(window.getSelection?window.getSelection().toString():document.selection.createRange().text);";
        if (isX5) {
            x5Webview.loadUrl(script);
        } else {
            webview.loadUrl(script);
        }
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
     * 设置是否选择原图
     *
     * @param originalImage true-原图,反之false;
     */
    public void setOriginalImage(boolean originalImage) {
        isOriginalImage = originalImage;
    }

    /**
     * 选择本地图片
     *
     * @param activity FragmentActivity
     */
    public void selectLocalImages(final FragmentActivity activity) {
        HandlerManager.getInstance().post(new RunnableParamsN<Object>() {
            @Override
            public void run(Object... objects) {
                RxPermissions permissions = new RxPermissions(activity);
                Disposable subscribe = permissions.request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean success) {
                                if (success) {
                                    imageSelectDialog.setOriginalImage(isOriginalImage);
                                    if (!isOriginalImage) {
                                        //选择后图片最大压缩大小
                                        imageSelectDialog.setMaxFileSize(2048);
                                        //图片尺寸不做限制
                                        imageSelectDialog.setMaxImageWidth(1000000);
                                        imageSelectDialog.setMaxImageHeight(1000000);
                                    }
                                    //最多选择图片数量
                                    imageSelectDialog.setMaxSelectNumber(1);
                                    //是否显示拍照选项
                                    imageSelectDialog.setShowTakingPictures(true);
                                    //显示图片选择
                                    imageSelectDialog.show(activity);
                                } else {
                                    mbox.setShowTitle(false);
                                    mbox.setShowClose(false);
                                    mbox.setCancelable(false);
                                    mbox.setContent("未打开相册和拍照权限,请跳转到应用详情页面打开权限.");
                                    mbox.setTarget("album_permission", activity);
                                    mbox.show(activity, DialogButtonsEnum.ConfirmCancel);
                                    finishFileUpload();
                                }
                            }
                        });
            }
        });
    }

    /**
     * 是否加载成功
     *
     * @return true-页面已成功加载;反之false;
     */
    public boolean isLoadSuccess() {
        return this.isLoadSuccess;
    }

    @SuppressLint("JavascriptInterface")
    protected void addJavascriptInterface(Object javascriptObject, String javascriptInterfaceName) {
        if (isX5) {
            x5Webview.addJavascriptInterface(javascriptObject, javascriptInterfaceName);
        } else {
            webview.addJavascriptInterface(javascriptObject, javascriptInterfaceName);
        }
    }

    /**
     * 设置自动播放视频
     *
     * @param isAutoPlayAudioVideo true-自动播放;false-默认不播放;
     */
    public void setAutoPlayAudioVideo(boolean isAutoPlayAudioVideo) {
        if (isX5) {
            x5Webview.setAutoPlayAudioVideo(isAutoPlayAudioVideo);
        } else {
            webview.setAutoPlayAudioVideo(isAutoPlayAudioVideo);
        }
    }
}
