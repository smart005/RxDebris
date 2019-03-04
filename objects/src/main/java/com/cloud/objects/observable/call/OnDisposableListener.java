package com.cloud.objects.observable.call;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/22
 * Description:rxjava消耗事件
 * Modifier:
 * ModifyContent:
 */
public interface OnDisposableListener<Result> {
    /**
     * 消耗回调处理
     *
     * @param result 从订阅onNext返回结果
     */
    public void accept(Result result);
}
