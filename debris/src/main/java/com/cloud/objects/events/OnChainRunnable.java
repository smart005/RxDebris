package com.cloud.objects.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/18
 * Description:链式可执行接口
 * Modifier:
 * ModifyContent:
 */
public interface OnChainRunnable<R, T> {

    public R run(T t, Object... extras);
}
