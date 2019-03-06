package com.cloud.nets.events;

import com.cloud.nets.properties.OkRxConfigParams;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/30
 * Description:http请求配置参数监听
 * Modifier:
 * ModifyContent:
 */
public interface OnConfigParamsListener {
    /**
     * 通过此回调返回自定义配置参数
     *
     * @param configParams 默认配置参数
     * @return 自定义配置参数
     */
    public OkRxConfigParams onConfigParamsCall(OkRxConfigParams configParams);
}
