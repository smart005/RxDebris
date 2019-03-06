package com.cloud.mixed.h5;

import com.tencent.smtt.sdk.WebView;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/2/24
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class OnH5WebViewListener extends JavascriptMethods {

    //添加代理属性
    public abstract void addUserAgent(List<String> userAgents);

    //返回html中标题
    public abstract void onTitle(String title);

    //url回调监听
    public abstract boolean onUrlListener(String url);

    //webview加载完成后回调
    public abstract void onLoaded(WebView view, boolean success, int errorCode, String description, String url);
}
