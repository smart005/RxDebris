package com.cloud.debris.presets;

import android.arch.lifecycle.LiveData;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-06
 * Description:用于在页面活跃状态时同步数据处理,降低资源的重复使用;(如在屏幕切换、前后台切换、从其它应用切回时触发)
 * Modifier:
 * ModifyContent:
 */
public class CycleLiveData<T> extends LiveData<T> {

    private OnCycleLiveDataListener onCycleLiveDataListener;

    public void setOnCycleLiveDataListener(OnCycleLiveDataListener cycleLiveDataListener) {
        this.onCycleLiveDataListener = cycleLiveDataListener;
    }

    @Override
    protected void onActive() {
        if (onCycleLiveDataListener != null) {
            onCycleLiveDataListener.onBindTrigger();
        }
    }

    @Override
    protected void onInactive() {
        if (onCycleLiveDataListener != null) {
            onCycleLiveDataListener.onUnBindTrigger();
        }
    }
}
