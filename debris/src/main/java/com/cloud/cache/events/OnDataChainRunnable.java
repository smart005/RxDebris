package com.cloud.cache.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/19
 * Description:数据链
 * Modifier:
 * ModifyContent:
 */
public abstract class OnDataChainRunnable<R, T> {

    public abstract R run(T t);

    public void complete(R r, Object... extras) {

    }
}
