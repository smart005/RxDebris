package com.cloud.nets.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:网络校验监听
 * Modifier:
 * ModifyContent:
 */
public interface OnRequestNetCheckListener {

    /**
     * 网络校验
     *
     * @return true-可用且已连接上;false-不可用;
     */
    public boolean onNetConnectCheck();
}
