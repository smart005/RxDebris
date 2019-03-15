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
public class OnH5WebViewListener extends JavascriptMethods {

    //添加代理属性
    public void addUserAgent(List<String> userAgents) {
        //agent 追加自定义代理
    }

    //返回html中标题
    public void onTitle(String title) {
        //title 获取到网页的标题
    }

    //url回调监听
    public boolean onUrlListener(String url) {
        //如果要做url拦截可在这里处理
        //返回true此链接不作渲染处理,false继续渲染;
        return false;
    }

    //webview加载完成后回调
    public void onLoaded(WebView view, boolean success, int errorCode, String description, String url) {
        //load completed h5加载完成后回调
    }
}
