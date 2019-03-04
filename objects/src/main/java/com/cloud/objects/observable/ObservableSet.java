package com.cloud.objects.observable;

import android.support.annotation.NonNull;

import com.cloud.objects.observable.call.OnDisposableListener;
import com.cloud.objects.observable.call.OnSubscribeListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/22
 * Description:rxjava订阅集;后期相关订阅在当前类实现;
 * Modifier:
 * ModifyContent:
 */
public class ObservableSet {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * 结构rxjava简单任务处理(只有next或accept[成功]回调)
     *
     * @param source   耗时任务处理监听(在io线程)
     * @param nextCall 结果任务处理线程(在mainThread线程)
     * @param <R>      source.next()数据返回类型
     */
    public <R> void buildEasyTask(@NonNull final OnSubscribeListener<R> source, final OnDisposableListener<R> nextCall) {
        if (nextCall == null) {
            //如果没有回调处理请使用ThreadPoolUtils
            return;
        }
        Observable<R> observable = Observable.create(new ObservableOnSubscribe<R>() {

            @Override
            public void subscribe(ObservableEmitter<R> e) throws Exception {
                if (source != null) {
                    e.onNext(source.next());
                }
                e.onComplete();
            }
        });
        DisposableSimpleObserver<R> disposableSimpleObserver = new DisposableSimpleObserver<R>() {
            @Override
            public void onNext(R param) {
                if (nextCall == null) {
                    return;
                }
                nextCall.accept(param);
            }
        };
        observable.subscribeOn(
                Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(disposableSimpleObserver);
        mCompositeDisposable.add(disposableSimpleObserver);
    }
}
