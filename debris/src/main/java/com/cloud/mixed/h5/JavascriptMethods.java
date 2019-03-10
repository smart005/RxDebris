package com.cloud.mixed.h5;

import com.tencent.smtt.sdk.WebView;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/22
 * Description:
 * register js add @android.webkit.JavascriptInterface and @JavascriptInterface
 * Modifier:
 * ModifyContent:
 */
public class JavascriptMethods {

    private JavascriptMethods methods = null;

    public JavascriptMethods() {
        //default
    }

    public JavascriptMethods(JavascriptMethods methods) {
        //methods为子类的超类对象
        this.methods = methods;
    }

    /**
     * h5回调native接口
     * 调用H5InteractionAPIUtils.getAPIMethod
     *
     * @param extras api参数{key-value}
     */
    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void getAPIMethod(String extras) {
        //web调用本地api方法回调
        if (methods != null) {
            methods.getAPIMethod(extras);
        }
    }

    /**
     * h5选择文本颜色
     *
     * @param selectText 选中的文本
     */
    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void getSelectText(String selectText) {
        //在web选择文本后回调
        if (methods != null) {
            methods.getSelectText(selectText);
        }
    }

    /**
     * native scheme call
     *
     * @param scheme 传入的scheme
     */
    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void nativeSchemeCall(String scheme) {
        if (methods != null) {
            methods.nativeSchemeCall(scheme);
        }
    }

    /**
     * 下载回调
     *
     * @param url  下载url
     * @param name 下载显示名称
     */
    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void download(String url, String name) {
        if (methods != null) {
            methods.download(url, name);
        }
    }

    /**
     * tel call
     *
     * @param tel web format (tel:xxxxxxxx)
     */
    public void onCallTel(String tel) {

    }

    /**
     * sms call
     *
     * @param sms web format (sms:xxxxxxxx)
     */
    public void onCallSms(String sms) {

    }

    /**
     * web confirm prompt call
     *
     * @param view    current web view
     * @param url     call url
     * @param message prompt message
     * @return true event interrupt,false with custom confirm deal with
     */
    public boolean onJsConfirm(WebView view, String url, String message) {
        return false;
    }
}
