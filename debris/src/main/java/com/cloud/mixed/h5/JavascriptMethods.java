package com.cloud.mixed.h5;

import com.cloud.ebus.EBus;
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
    //js方法检测当前重试次数
    private int currJsFunCheckRetryCount = 0;

    public JavascriptMethods() {
        //default
    }

    public JavascriptMethods(JavascriptMethods methods) {
        //methods为子类的超类对象
        this.methods = methods;
    }

    public void setCurrJsFunCheckRetryCount(int currJsFunCheckRetryCount) {
        this.currJsFunCheckRetryCount = currJsFunCheckRetryCount;
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
        //h5文本选择后回调
        //需要调用binding.h5Test.getSelectText();方法才能回调
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
        //h5通过scheme调用native回调
        //调用方式:
        //1.url?scheme=[url encode编码后的scheme]或以第二种方式;
        //2.android调用cl_cloud_group_jsm.nativeSchemeCall(encodeURIComponent(schemeUrl));
        //ios调用window.webkit.messageHandlers.nativeSchemeCall.postMessage(encodeURIComponent(schemeUrl));
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
        //打开的链接若是apk、rar则会回调此方法
        if (methods != null) {
            methods.download(url, name);
        }
    }

    /**
     * 检测js方法是否存在回调
     *
     * @param funName    js方法名
     * @param isExist    true-存在;false-不存在;
     * @param retryCount 重试次数
     */
    @android.webkit.JavascriptInterface
    @JavascriptInterface
    public void b20b390b1e974f4a92d54f0fb6c9d26f(String funName, boolean isExist, int retryCount) {
        if (methods == null) {
            return;
        }
        if (isExist) {
            methods.onCheckJsFunctionCall(funName, true);
        } else {
            if (retryCount > 0 && currJsFunCheckRetryCount < retryCount) {
                currJsFunCheckRetryCount++;
                EBus.getInstance().post("js_function_check_1185193980", funName, currJsFunCheckRetryCount, retryCount);
            } else {
                methods.onCheckJsFunctionCall(funName, false);
            }
        }
    }

    /**
     * tel call
     *
     * @param tel web format (tel:xxxxxxxx)
     */
    public void onCallTel(String tel) {
        //拨打电话功能需要在头部加上<meta name="format-detection" content="telephone=yes"/>
    }

    /**
     * sms call
     *
     * @param sms web format (sms:xxxxxxxx)
     */
    public void onCallSms(String sms) {
        //拨打电话功能需要在头部加上<meta name="format-detection" content="telephone=yes"/>
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

    /**
     * 检测js方法是否存在回调
     *
     * @param funName js方法名
     * @param isExist true-存在;false-不存在;
     */
    public void onCheckJsFunctionCall(String funName, boolean isExist) {

    }
}
