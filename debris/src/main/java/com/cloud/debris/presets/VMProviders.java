package com.cloud.debris.presets;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-06
 * Description:view model providers
 * Modifier:
 * ModifyContent:
 */
public class VMProviders<VM extends BaseViewModel<T>, T> implements OnCycleLiveDataListener {

    private OnViewModelPresetsChanged<T> onViewModelPresetsChanged;
    private VM model;

    /**
     * 设置model改变监听
     *
     * @param presetsChanged 预设改变监听
     */
    public void setOnViewModelPresetsChanged(OnViewModelPresetsChanged<T> presetsChanged) {
        this.onViewModelPresetsChanged = presetsChanged;
    }

    /**
     * 获取或绑定model观察
     *
     * @param fragmentActivity fragmentActivity
     * @param modelClass       model type class
     */
    public void bindObserve(FragmentActivity fragmentActivity, Class<VM> modelClass) {
        if (fragmentActivity == null || modelClass == null) {
            return;
        }
        Application application = fragmentActivity.getApplication();
        if (application == null) {
            return;
        }
        model = new ViewModelProvider(fragmentActivity, new ViewModelProvider.AndroidViewModelFactory(application)).get(modelClass);
        CycleLiveData<T> cycleLiveData = new CycleLiveData<T>();
        cycleLiveData.setOnCycleLiveDataListener(this);
        cycleLiveData.observe(fragmentActivity, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                //在生命周期发生改变时触发
                if (onViewModelPresetsChanged == null) {
                    return;
                }
                onViewModelPresetsChanged.onModelChanged(t);
            }
        });
        //添加观察事件
        MutableLiveData<T> currentData = model.getCurrentData();
        if (currentData == null) {
            return;
        }
        currentData.observe(fragmentActivity, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (onViewModelPresetsChanged == null) {
                    return;
                }
                onViewModelPresetsChanged.onModelChanged(t);
            }
        });
    }

    /**
     * 获取或绑定model观察
     *
     * @param fragment   fragment
     * @param modelClass model type class
     */
    public void bindObserve(Fragment fragment, Class<VM> modelClass, Application application) {
        if (fragment == null || modelClass == null || application == null) {
            return;
        }
        model = new ViewModelProvider(fragment, new ViewModelProvider.AndroidViewModelFactory(application)).get(modelClass);
        //添加观察事件
        MutableLiveData<T> currentData = model.getCurrentData();
        if (currentData == null) {
            return;
        }
        CycleLiveData<T> cycleLiveData = new CycleLiveData<T>();
        cycleLiveData.setOnCycleLiveDataListener(this);
        cycleLiveData.observe(fragment, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                //在生命周期发生改变时触发
                if (onViewModelPresetsChanged == null) {
                    return;
                }
                onViewModelPresetsChanged.onModelChanged(t);
            }
        });
        currentData.observe(fragment, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (onViewModelPresetsChanged == null) {
                    return;
                }
                onViewModelPresetsChanged.onModelChanged(t);
            }
        });
    }

    /**
     * 获取数据model
     *
     * @return
     */
    public VM getModel() {
        return this.model;
    }

    /**
     * 更新数据项
     *
     * @param liveDataModel liveData数据
     */
    public void setValue(T liveDataModel) {
        MutableLiveData<T> currentData = model.getCurrentData();
        if (currentData == null) {
            return;
        }
        currentData.setValue(liveDataModel);
    }

    @Override
    public void onBindTrigger() {
        //在onResume触发时回调
    }

    @Override
    public void onUnBindTrigger() {
        //在页面非活跃状态时触发
    }
}
