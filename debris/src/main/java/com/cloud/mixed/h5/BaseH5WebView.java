package com.cloud.mixed.h5;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cloud.ebus.EBus;
import com.cloud.ebus.SubscribeEBus;
import com.cloud.mixed.abstracts.OnBridgeAbstract;
import com.cloud.mixed.annotations.JavascriptInterface;
import com.cloud.mixed.h5.events.OnWebActivityListener;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
class BaseH5WebView extends BaseWebLoad {

    //js检测失败重试次数
    private int checkJsRetryCount = 0;
    protected JavascriptMethods javascriptMethods = new JavascriptMethods();

    public BaseH5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onReceivedTitle(String title) {
        OnWebActivityListener activityListener = getOnWebActivityListener();
        if (activityListener != null) {
            activityListener.onTitle(title);
        }
    }

    @Override
    protected void onLoadFinished(boolean success, int errorCode, String description, String url) {
        OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
        if (bridgeAbstract == null ||
                TextUtils.isEmpty(url) ||
                url.contains("javascript:") ||
                TextUtils.equals(url, "about:blank")) {
            return;
        }
        bridgeAbstract.onLoaded(success, errorCode, description, url);
    }

    /**
     * 检测js方法是否存在
     *
     * @param jsFunName  js方法名
     * @param retryCount 检测失败后重试次数(每次重试均延迟2秒)
     */
    public void isExistJsFunction(String jsFunName, int retryCount) {
        if (TextUtils.isEmpty(jsFunName)) {
            return;
        }
        if (retryCount < 0) {
            retryCount = 0;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("javascript: setTimeout(function() {");
        builder.append("if(typeof window.").append(jsFunName).append(" === 'function') {");
        builder.append("window.cl_cloud_group_jsm.b20b390b1e974f4a92d54f0fb6c9d26f('");
        builder.append(jsFunName).append("', true, ").append(retryCount).append(")");
        builder.append("} else {");
        builder.append("window.cl_cloud_group_jsm.b20b390b1e974f4a92d54f0fb6c9d26f('")
                .append(jsFunName).append("', false, ").append(retryCount).append(")");
        builder.append("}}, 0);");
        super.load(builder.toString());
    }

    @SubscribeEBus(receiveKey = "js_function_check_1185193980")
    public void reCheckJsFunction(String jsFunName, int currRetryCount, int retryCount) {
        javascriptMethods.setCurrJsFunCheckRetryCount(currRetryCount);
        isExistJsFunction(jsFunName, retryCount);
    }

    public class JavascriptMethods {

        //js方法检测当前重试次数
        private int currJsFunCheckRetryCount = 0;

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
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract != null) {
                bridgeAbstract.getAPIMethod(extras);
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
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract != null) {
                bridgeAbstract.getSelectText(selectText);
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
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract != null) {
                bridgeAbstract.nativeSchemeCall(scheme);
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
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract != null) {
                bridgeAbstract.download(url, name);
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
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract == null) {
                return;
            }
            if (isExist) {
                bridgeAbstract.onCheckJsFunctionCall(funName, true);
            } else {
                if (retryCount > 0 && currJsFunCheckRetryCount < retryCount) {
                    currJsFunCheckRetryCount++;
                    EBus.getInstance().post("js_function_check_1185193980", funName, currJsFunCheckRetryCount, retryCount);
                } else {
                    bridgeAbstract.onCheckJsFunctionCall(funName, false);
                }
            }
        }
    }
}
