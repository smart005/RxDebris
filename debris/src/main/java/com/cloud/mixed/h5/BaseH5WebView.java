package com.cloud.mixed.h5;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
class BaseH5WebView extends BaseWebLoad {

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
    protected void onLoadFinished(WebView webView, boolean success, int errorCode, String description, String url) {
        OnH5WebViewListener listener = getWebListener();
        if (listener == null || TextUtils.isEmpty(url) || url.contains("javascript:")) {
            return;
        }
        listener.onLoaded(webView, success, errorCode, description, url);
    }
}
