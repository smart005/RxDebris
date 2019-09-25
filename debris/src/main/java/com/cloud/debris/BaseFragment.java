package com.cloud.debris;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cloud.ebus.EBus;
import com.cloud.objects.bases.BundleData;
import com.cloud.objects.events.OnSupperProperties;
import com.cloud.objects.utils.GlobalUtils;

import java.util.ArrayList;

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
    //页面标识
    private String $_page_code;

    protected void onAttached(Context context) {
        //fragment第一个生命周期
        bundleData = getBundleData();
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
        $_page_code = GlobalUtils.getNewGuid();
        EBus.getInstance().registered(this, $_page_code);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EBus.getInstance().unregister(this, $_page_code);
    }

    public BundleData getBundleData() {
        if (bundleData == null) {
            bundleData = new BundleData(getArguments());
        }
        return bundleData;
    }

    @Override
    public Bundle getBundle() {
        bundleData = getBundleData();
        return bundleData.getBundle();
    }

    @Override
    public String getStringBundle(Bundle bundle, String key, String defaultValue) {
        bundleData = getBundleData();
        return bundleData.getStringBundle(bundle, key, defaultValue);
    }

    @Override
    public String getStringBundle(String key, String defaultValue) {
        bundleData = getBundleData();
        return bundleData.getStringBundle(key, defaultValue);
    }

    @Override
    public String getStringBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getStringBundle(key);
    }

    @Override
    public int getIntBundle(Bundle bundle, String key, int defaultValue) {
        bundleData = getBundleData();
        return bundleData.getIntBundle(bundle, key, defaultValue);
    }

    @Override
    public int getIntBundle(String key, int defaultValue) {
        bundleData = getBundleData();
        return bundleData.getIntBundle(key, defaultValue);
    }

    @Override
    public int getIntBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getIntBundle(key);
    }

    @Override
    public boolean getBooleanBundle(Bundle bundle, String key, boolean defaultValue) {
        bundleData = getBundleData();
        return bundleData.getBooleanBundle(bundle, key, defaultValue);
    }

    @Override
    public boolean getBooleanBundle(String key, boolean defaultValue) {
        bundleData = getBundleData();
        return bundleData.getBooleanBundle(key, defaultValue);
    }

    @Override
    public boolean getBooleanBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getBooleanBundle(key);
    }

    @Override
    public Object getObjectBundle(Bundle bundle, String key, Object defaultValue) {
        bundleData = getBundleData();
        return bundleData.getObjectBundle(bundle, key, defaultValue);
    }

    @Override
    public Object getObjectBundle(String key, Object defaultValue) {
        bundleData = getBundleData();
        return bundleData.getObjectBundle(key, defaultValue);
    }

    @Override
    public Object getObjectBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getObjectBundle(key);
    }

    @Override
    public float getFloatBundle(Bundle bundle, String key, float defaultValue) {
        bundleData = getBundleData();
        return bundleData.getFloatBundle(bundle, key, defaultValue);
    }

    @Override
    public float getFloatBundle(String key, float defaultValue) {
        bundleData = getBundleData();
        return bundleData.getFloatBundle(key, defaultValue);
    }

    @Override
    public float getFloatBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getFloatBundle(key);
    }

    @Override
    public double getDoubleBundle(Bundle bundle, String key, double defaultValue) {
        bundleData = getBundleData();
        return bundleData.getDoubleBundle(bundle, key, defaultValue);
    }

    @Override
    public double getDoubleBundle(String key, double defaultValue) {
        bundleData = getBundleData();
        return bundleData.getDoubleBundle(key, defaultValue);
    }

    @Override
    public double getDoubleBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getDoubleBundle(key);
    }

    @Override
    public long getLongBundle(Bundle bundle, String key, long defaultValue) {
        bundleData = getBundleData();
        return bundleData.getLongBundle(bundle, key, defaultValue);
    }

    @Override
    public long getLongBundle(String key, long defaultValue) {
        bundleData = getBundleData();
        return bundleData.getLongBundle(key, defaultValue);
    }

    @Override
    public long getLongBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getLongBundle(key);
    }

    @Override
    public <T> T getSerializableBundle(String key, T defaultValue) {
        bundleData = getBundleData();
        return bundleData.getSerializableBundle(key, defaultValue);
    }

    @Override
    public <T> T getSerializableBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getSerializableBundle(key);
    }

    @Override
    public <T> T getParcelableBundle(String key, T defaultValue) {
        bundleData = getBundleData();
        return bundleData.getParcelableBundle(key, defaultValue);
    }

    @Override
    public <T> T getParcelableBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getParcelableBundle(key);
    }

    @Override
    public Parcelable[] getParcelableArrayBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getParcelableArrayBundle(key);
    }

    @Override
    public <T extends Parcelable> ArrayList<T> getParcelableArrayListBundle(String key) {
        bundleData = getBundleData();
        return bundleData.getParcelableArrayListBundle(key);
    }

    @Override
    public String getClassAction() {
        bundleData = getBundleData();
        return bundleData.getClassAction(this.getClass());
    }
}
