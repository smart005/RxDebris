package com.cloud.objects.observable;

import io.reactivex.observers.DisposableObserver;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/22
 * Description:只对success处理回调onNext
 * Modifier:
 * ModifyContent:
 */
public abstract class DisposableSimpleObserver<T> extends DisposableObserver<T> {

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
