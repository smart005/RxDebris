package com.cloud.images.glide;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/4
 * Description:file callback
 * Modifier:
 * ModifyContent:
 */
public abstract class GFileCallback<T> {
    /**
     * 处理完后将对象返回内部作信息存储
     *
     * @param t glide存储对象
     */
    public abstract void call(T t);
}
