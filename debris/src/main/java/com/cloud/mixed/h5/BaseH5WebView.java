package com.cloud.mixed.h5;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cloud.ebus.SubscribeEBus;

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

    public BaseH5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onReceivedTitle(String title) {
        OnH5WebViewListener listener = getWebListener();
        if (listener != null) {
            listener.onTitle(title);
        }
    }

    @Override
    protected void onLoadFinished(boolean success, int errorCode, String description, String url) {
        OnH5WebViewListener listener = getWebListener();
        if (listener == null ||
                TextUtils.isEmpty(url) ||
                url.contains("javascript:") ||
                TextUtils.equals(url, "about:blank")) {
            return;
        }
        listener.onLoaded(success, errorCode, description, url);
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
        OnH5WebViewListener listener = getWebListener();
        if (listener == null) {
            return;
        }
        listener.setCurrJsFunCheckRetryCount(currRetryCount);
        isExistJsFunction(jsFunName, retryCount);
    }
}
