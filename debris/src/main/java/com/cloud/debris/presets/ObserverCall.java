package com.cloud.debris.presets;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-11-01
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ObserverCall<T> implements Observer<T> {

    private MutableLiveData liveDataCall;

    public ObserverCall() {

    }

    public ObserverCall(MutableLiveData liveDataCall) {
        this.liveDataCall = liveDataCall;
    }

    @Override
    public void onChanged(T t) {
        if (liveDataCall != null) {
            liveDataCall.removeObserver(this);
        }
    }
}
