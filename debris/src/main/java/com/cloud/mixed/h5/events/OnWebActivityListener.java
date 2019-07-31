package com.cloud.mixed.h5.events;

import com.tencent.smtt.sdk.WebSettings;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-31
 * Description:web view for activity call
 * Modifier:
 * ModifyContent:
 */
public interface OnWebActivityListener {

    //获取到网页的标题
    public void onTitle(String title);

    //setting相关配置修饰
    public void onSettingModified(WebSettings settings);

    //setting相关配置修饰
    public void onSettingModified(android.webkit.WebSettings settings);
}
