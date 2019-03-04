package com.cloud.objects.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/8
 * Description:api请求基础地址配置回调
 * Modifier:
 * ModifyContent:
 */
public interface OnRequestApiUrl {
    /**
     * base url回调方法
     *
     * @param apiUrlTypeName 获取base url类型名称(根据此名称判断返回对应的base url)
     * @return
     */
    public String onBaseUrl(String apiUrlTypeName);
}
