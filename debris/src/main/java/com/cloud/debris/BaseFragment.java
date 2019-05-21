package com.cloud.debris;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cloud.ebus.EBus;
import com.cloud.objects.bases.BundleData;
import com.cloud.objects.events.OnSupperProperties;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/26
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseFragment extends Fragment implements OnSupperProperties {

    private BundleData bundleData = null;

    protected void onAttached(Context context) {
        //fragment第一个生命周期
        bundleData = new BundleData(getArguments());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttached(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //sdk < 23时回调此方法(除v4下)
        //目前此方法可忽略
        if (Build.VERSION.SDK_INT < 23) {
            onAttached(activity);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        EBus.getInstance().registered(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EBus.getInstance().unregister(this);
    }

    @Override
    public Bundle getBundle() {
        return bundleData.getBundle();
    }

    @Override
    public String getStringBundle(Bundle bundle, String key, String defaultValue) {
        return bundleData.getStringBundle(bundle, key, defaultValue);
    }

    @Override
    public String getStringBundle(String key, String defaultValue) {
        return bundleData.getStringBundle(key, defaultValue);
    }

    @Override
    public String getStringBundle(String key) {
        return bundleData.getStringBundle(key);
    }

    @Override
    public int getIntBundle(Bundle bundle, String key, int defaultValue) {
        return bundleData.getIntBundle(bundle, key, defaultValue);
    }

    @Override
    public int getIntBundle(String key, int defaultValue) {
        return bundleData.getIntBundle(key, defaultValue);
    }

    @Override
    public int getIntBundle(String key) {
        return bundleData.getIntBundle(key);
    }

    @Override
    public boolean getBooleanBundle(Bundle bundle, String key, boolean defaultValue) {
        return bundleData.getBooleanBundle(bundle, key, defaultValue);
    }

    @Override
    public boolean getBooleanBundle(String key, boolean defaultValue) {
        return bundleData.getBooleanBundle(key, defaultValue);
    }

    @Override
    public boolean getBooleanBundle(String key) {
        return bundleData.getBooleanBundle(key);
    }

    @Override
    public Object getObjectBundle(Bundle bundle, String key, Object defaultValue) {
        return bundleData.getObjectBundle(bundle, key, defaultValue);
    }

    @Override
    public Object getObjectBundle(String key, Object defaultValue) {
        return bundleData.getObjectBundle(key, defaultValue);
    }

    @Override
    public Object getObjectBundle(String key) {
        return bundleData.getObjectBundle(key);
    }

    @Override
    public float getFloatBundle(Bundle bundle, String key, float defaultValue) {
        return bundleData.getFloatBundle(bundle, key, defaultValue);
    }

    @Override
    public float getFloatBundle(String key, float defaultValue) {
        return bundleData.getFloatBundle(key, defaultValue);
    }

    @Override
    public float getFloatBundle(String key) {
        return bundleData.getFloatBundle(key);
    }

    @Override
    public double getDoubleBundle(Bundle bundle, String key, double defaultValue) {
        return bundleData.getDoubleBundle(bundle, key, defaultValue);
    }

    @Override
    public double getDoubleBundle(String key, double defaultValue) {
        return bundleData.getDoubleBundle(key, defaultValue);
    }

    @Override
    public double getDoubleBundle(String key) {
        return bundleData.getDoubleBundle(key);
    }

    @Override
    public long getLongBundle(Bundle bundle, String key, long defaultValue) {
        return bundleData.getLongBundle(bundle, key, defaultValue);
    }

    @Override
    public long getLongBundle(String key, long defaultValue) {
        return bundleData.getLongBundle(key, defaultValue);
    }

    @Override
    public long getLongBundle(String key) {
        return bundleData.getLongBundle(key);
    }

    @Override
    public <T> T getSerializableBundle(String key, T defaultValue) {
        return bundleData.getSerializableBundle(key, defaultValue);
    }

    @Override
    public <T> T getSerializableBundle(String key) {
        return bundleData.getSerializableBundle(key);
    }

    @Override
    public <T> T getParcelableBundle(String key, T defaultValue) {
        return bundleData.getParcelableBundle(key, defaultValue);
    }

    @Override
    public <T> T getParcelableBundle(String key) {
        return bundleData.getParcelableBundle(key);
    }

    @Override
    public String getClassAction() {
        return bundleData.getClassAction(this.getClass());
    }
}
