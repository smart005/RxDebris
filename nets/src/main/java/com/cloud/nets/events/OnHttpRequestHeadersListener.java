package com.cloud.nets.events;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/16
 * Description:请求头监听
 * Modifier:
 * ModifyContent:
 */
public interface OnHttpRequestHeadersListener {
    /**
     * 接口请求头回调
     *
     * @param apiUnique api定义唯一标识符(在接口定义时会指定)
     * @param headers   请求头数据
     */
    public void onRequestHeaders(String apiUnique, HashMap<String, String> headers);
}
