package com.cloud.debris.presets;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-06
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseViewModel<T> extends ViewModel {

    /**
     * 用于设置和更新数据集
     */
    private MutableLiveData<T> currentData;

    /**
     * 获取当前数据管理对象
     *
     * @return MutableLiveData<T>
     */
    public MutableLiveData<T> getCurrentData() {
        if (currentData == null) {
            currentData = new MutableLiveData<T>();
        }
        return currentData;
    }
}
