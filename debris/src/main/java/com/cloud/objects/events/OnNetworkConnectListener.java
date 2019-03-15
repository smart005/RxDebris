package com.cloud.objects.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:网络连接监听
 * Modifier:
 * ModifyContent:
 */
public interface OnNetworkConnectListener {
    /**
     * 是否已连接网络
     *
     * @return true-连接;false-未连接;
     */
    public boolean isConnected();
}
