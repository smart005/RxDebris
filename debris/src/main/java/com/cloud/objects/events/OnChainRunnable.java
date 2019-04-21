package com.cloud.objects.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/18
 * Description:链式可执行接口
 * Modifier:
 * ModifyContent:
 */
public abstract class OnChainRunnable<R, T> {

    public void start() {
        //start
    }

    public abstract R run(T t, Object... extras);

    public void finish() {
        //finish
    }
}
