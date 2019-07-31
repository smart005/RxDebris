package com.cloud.mixed.h5.events;

import com.cloud.mixed.h5.H5WebView;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-31
 * Description:
 * Modifier:
 * ModifyContent:
 */
public interface OnHybridListener {

    //添加代理属性
    public void addUserAgent(List<String> userAgents);

    //如果要做url拦截可在这里处理
    //返回true此链接不作渲染处理,false继续渲染;
    public boolean onUrlListener(H5WebView webView, String url);

    //webview加载完成后回调
    public void onLoaded(boolean success, int errorCode, String description, String url);

    /**
     * 检测js方法是否存在回调
     *
     * @param funName js方法名
     * @param isExist true-存在;false-不存在;
     */
    public void onCheckJsFunctionCall(String funName, boolean isExist);

    /**
     * h5回调native接口
     * 调用H5InteractionAPIUtils.getAPIMethod
     *
     * @param extras api参数{key-value}
     */
    public void getAPIMethod(String extras);

    /**
     * h5选择文本颜色
     *
     * @param selectText 选中的文本
     */
    public void getSelectText(String selectText);

    /**
     * native scheme call
     *
     * @param scheme 传入的scheme
     */
    public void nativeSchemeCall(String scheme);

    /**
     * 文件下载(支持url,javascript方式调用)
     *
     * @param url  下载链接
     * @param name 下载名称
     */
    public void download(String url, String name);

    /**
     * tel call
     *
     * @param tel web format (tel:xxxxxxxx)
     */
    public void onCallTel(String tel);

    /**
     * sms call
     *
     * @param sms web format (sms:xxxxxxxx)
     */
    public void onCallSms(String sms);
}
