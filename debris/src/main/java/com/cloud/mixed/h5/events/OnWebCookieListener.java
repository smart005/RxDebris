package com.cloud.mixed.h5.events;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-12
 * Description:cookie设置监听
 * Modifier:
 * ModifyContent:
 */
public interface OnWebCookieListener {

    /**
     * cookie处理
     *
     * @return cookies[key-value]
     */
    public HashMap<String, String> onWebCookies();
}
