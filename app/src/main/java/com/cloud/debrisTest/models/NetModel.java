package com.cloud.debrisTest.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.cloud.debrisTest.BR;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class NetModel extends BaseObservable {

    private String netdata = "";

    @Bindable
    public String getNetdata() {
        return netdata;
    }

    public void setNetdata(String netdata) {
        this.netdata = netdata;
        notifyPropertyChanged(BR.netdata);
    }
}
