package com.cloud.objects.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/16
 * Description:
 * Modifier:
 * ModifyContent:
 */
public abstract class ObservableComponent<Param, Params> {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Params[] params = null;
    private String key = "";
    private Object extras = null;

    /**
     * subscribe
     *
     * @return
     */
    protected abstract Param subscribeWith(Params... params);

    /**
     * onNext
     *
     * @param param
     * @param params
     */
    protected void nextWith(Param param, Params... params) {

    }

    /**
     * onNext
     *
     * @param param
     * @param key
     * @param params
     */
    protected void nextWith(Param param, String key, Params... params) {

    }

    /**
     * complete
     *
     * @param isError   true-已回调onError方法；false-已正常处理完成;
     * @param throwable
     * @param params
     */
    protected void completeWith(boolean isError, Throwable throwable, Params... params) {

    }

    /**
     * 回调时返回,用于区分
     *
     * @param key 请求标识
     */
    public void setKey(String key) {
        this.key = key;
    }

    public <T> T getExtras() {
        return (T) extras;
    }

    public void setExtras(Object extras) {
        this.extras = extras;
    }

    /**
     * 构建并执行observable
     *
     * @param params 输入参数在subscribeWith参数中返回
     */
    public void build(Params... params) {
        this.params = params;
        Observable<Param> observable = Observable.create(new ObservableOnSubscribe<Param>() {

            @Override
            public void subscribe(ObservableEmitter<Param> e) throws Exception {
                e.onNext(subscribeWith(ObservableComponent.this.params));
                e.onComplete();
            }
        });
        DisposableObserver<Param> disposableObserver = new DisposableObserver<Param>() {
            @Override
            public void onNext(Param param) {
                nextWith(param, ObservableComponent.this.params);
                nextWith(param, key, ObservableComponent.this.params);
            }

            @Override
            public void onError(Throwable e) {
                completeWith(true, e, ObservableComponent.this.params);
            }

            @Override
            public void onComplete() {
                completeWith(false, null, ObservableComponent.this.params);
            }
        };
        observable.subscribeOn(
                Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }
}
