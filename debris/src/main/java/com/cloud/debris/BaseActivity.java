package com.cloud.debris;

import android.app.Activity;
import android.os.Bundle;

import com.cloud.debris.enums.StatisticalTypes;
import com.cloud.debris.event.OnLifeCycleStatistical;
import com.cloud.debris.event.OnSupperProperties;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/26
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseActivity extends Activity implements OnSupperProperties, OnLifeCycleStatistical {

    private BundleData bundleData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundleData = new BundleData(getIntent());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
        } catch (Exception e) {
            SuperActivitySupport.callUpActivity(this);
        }
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
    public <T> T onStatisticalClassObject(StatisticalTypes statisticalTypes) {
        return null;
    }

    @Override
    public void setStatisticalClassObject(StatisticalTypes statisticalTypes, Object statisticalClassObject) {

    }
}
