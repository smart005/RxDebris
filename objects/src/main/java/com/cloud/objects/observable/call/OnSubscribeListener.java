package com.cloud.objects.observable.call;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/22
 * Description:rxjava订阅回调处理
 * Modifier:
 * ModifyContent:
 */
public interface OnSubscribeListener<R> {

    /**
     * 一般处理耗时任务
     *
     * @return 任务处理后的结果
     */
    public R next();
}
