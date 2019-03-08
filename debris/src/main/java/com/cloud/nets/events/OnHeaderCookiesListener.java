package com.cloud.nets.events;

import java.util.Map;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:设置cookie监听
 * Modifier:
 * ModifyContent:
 */
public interface OnHeaderCookiesListener {

    /**
     * http请求cookies追加回调
     *
     * @return 新的cookies信息
     */
    public Map<String, String> onCookiesCall();
}
