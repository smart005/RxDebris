package com.cloud.nets.events;

import com.cloud.nets.enums.DataType;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class OnSuccessfulListener<T> {

    /**
     * 接口回调
     *
     * @param t        返回数据对象
     * @param dataType 返回数据类型
     * @param extras   扩展参数
     */
    public abstract void onSuccessful(T t, DataType dataType, Object... extras);

    /**
     * 请求失败回调
     *
     * @param t      数据
     * @param extras 扩展参数
     */
    public void onError(T t, Object... extras) {
        //失败回调
    }

    /**
     * 请求失败回调
     *
     * @param extras 扩展参数
     */
    public void onError(Object... extras) {
        //失败回调
    }

    /**
     * 请求完成回调
     *
     * @param extras 扩展参数
     */
    public void onCompleted(Object... extras) {
        //完成回调
    }
}
