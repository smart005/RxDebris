package com.cloud.nets.events;

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
     * @param t          返回数据对象
     * @param isLastCall 在缓存数据回调或请求回调无论是否同时存在的情况若最后一次回调则isLastCall==true,反之为false;
     * @param extras     扩展参数
     */
    public abstract void onSuccessful(T t, boolean isLastCall, Object... extras);

    /**
     * 请求失败回调
     *
     * @param t 数据
     * @param extras 扩展参数
     */
    public void onError(T t,Object... extras) {
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
