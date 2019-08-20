package com.cloud.debris.presets;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStoreOwner;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-06
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class LiveDataUtils {

    /**
     * liveData数据类型转换
     *
     * @param liveData liveData
     * @param func     数据处理
     * @param <T>      数据类型
     * @param <R>      返回数据类型
     * @return LiveData
     */
    public static <T, R> LiveData<R> to(LiveData<T> liveData, Function<T, R> func) {
        LiveData<R> result = Transformations.map(liveData, func);
        return result;
    }

    /**
     * liveData数据类型转换
     *
     * @param liveData liveData
     * @param func     数据处理
     * @param <T>      数据类型
     * @param <R>      返回数据类型
     * @return LiveData
     */
    public static <T, R> LiveData<R> switchMap(LiveData<T> liveData, Function<T, LiveData<R>> func) {
        LiveData<R> result = Transformations.switchMap(liveData, func);
        return result;
    }

    /**
     * 获取view model对象
     *
     * @param storeOwner     storeOwner
     * @param application    application
     * @param viewModelClass viewModelClass
     * @param <T>            view model类型
     * @return view model
     */
    public static <T extends ViewModel> T bindViewModel(ViewModelStoreOwner storeOwner, Application application, Class<T> viewModelClass) {
        return new ViewModelProvider(storeOwner, new ViewModelProvider.AndroidViewModelFactory(application)).get(viewModelClass);
    }
}
