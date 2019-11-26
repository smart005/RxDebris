package com.cloud.debris;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.cloud.debris.annotations.ActivityTagParams;
import com.cloud.debris.bundle.RedirectUtils;
import com.cloud.debris.enums.StatisticalTypes;
import com.cloud.debris.event.OnActivityStatesLisstener;
import com.cloud.debris.event.OnLifeCycleStatistical;
import com.cloud.ebus.EBus;
import com.cloud.ebus.SubscribeEBus;
import com.cloud.launchs.utils.ActivityUtils;
import com.cloud.objects.bases.BundleData;
import com.cloud.objects.events.OnSupperProperties;
import com.cloud.objects.utils.ActiveParamsUtils;
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
public class BasicFragmentActivity extends FragmentActivity implements OnSupperProperties,
        OnActivityStatesLisstener, OnLifeCycleStatistical {

    private BundleData bundleData = null;
    //页面标识
    private String $_page_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundleData = getBundleData();
        super.onCreate(savedInstanceState);
        $_page_code = GlobalUtils.getNewGuid();
        EBus.getInstance().registered(this, $_page_code);
        //记录当前参数
        Class<? extends BasicFragmentActivity> aClass = getClass();
        if (aClass.isAnnotationPresent(ActivityTagParams.class)) {
            String classPath = aClass.getName();
            ActiveParamsUtils.getInstance().putParams(classPath, bundleData.getBundle());
        }
    }

    public BundleData getBundleData() {
        if (bundleData == null) {
            bundleData = new BundleData(getIntent());
        }
        return bundleData;
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
    protected void onDestroy() {
        super.onDestroy();
        EBus.getInstance().unregister(this, $_page_code);
        ActiveParamsUtils.getInstance().activeDestory();
    }

    /**
     * 销毁活动
     *
     * @param classes 要销毁的活动
     */
    public void destoryActives(Class<?>... classes) {
        ActiveParamsUtils.getInstance().destorys(classes);
    }

    @SubscribeEBus(receiveKey = "$_42e9aa025d5f49e6abbe7d150498e93e")
    public void onEventDestoryActive(String className) {
        String name = getClass().getName();
        if (TextUtils.equals(className, name)) {
            RedirectUtils.finishActivity(this);
        }
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
    public <T> T onStatisticalClassObject(StatisticalTypes statisticalTypes) {
        return null;
    }

    @Override
    public void setStatisticalClassObject(StatisticalTypes statisticalTypes, Object statisticalClassObject) {

    }

    protected FragmentActivity getActivity() {
        return this;
    }

    @Override
    public String getClassAction() {
        bundleData = getBundleData();
        return bundleData.getClassAction(this.getClass());
    }

    @Override
    public void setMainActivity(boolean isMainActivity) {
        SuperActivitySupport.setMainActivity(this.getClass(), isMainActivity);
    }

    @Override
    public boolean isDestroyed(Activity activity) {
        return ActivityUtils.isDestroyed(activity);
    }
}
