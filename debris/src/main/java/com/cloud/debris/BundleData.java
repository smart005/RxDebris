package com.cloud.debris;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.utils.BundleUtils;
import com.cloud.objects.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/20
 * Description:bundle工具类
 * Modifier:
 * ModifyContent:
 */
public class BundleData {

    private Intent intent;
    private Bundle bundle;

    public BundleData(Intent intent) {
        this.intent = intent;
    }

    public BundleData(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
     * 获取bundle对象
     *
     * @param capacity 初始化大小
     * @return
     */
    public Bundle getBundle(int capacity) {
        if (intent == null) {
            if (bundle == null) {
                return capacity > 0 ? new Bundle(capacity) : new Bundle();
            } else {
                return bundle;
            }
        } else {
            Bundle bundle = intent.getExtras();
            return bundle == null ? new Bundle() : bundle;
        }
    }

    /**
     * 获取bundle对象
     *
     * @return
     */
    public Bundle getBundle() {
        return getBundle(0);
    }

    /**
     * 从bundle中获取字符串
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public String getStringBundle(Bundle bundle, String key, String defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object o = bundle.get(key);
            if (o == null) {
                return defaultValue;
            }
            return String.valueOf(o);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取字符串
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public String getStringBundle(String key, String defaultValue) {
        Bundle bundle = getBundle();
        return getStringBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取字符串
     *
     * @param key
     * @return
     */
    public String getStringBundle(String key) {
        return getStringBundle(key, "");
    }

    /**
     * 从bundle中获取int值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public int getIntBundle(Bundle bundle, String key, int defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toInt(value, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取int值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public int getIntBundle(String key, int defaultValue) {
        Bundle bundle = getBundle();
        return getIntBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取int值
     *
     * @param key
     * @return
     */
    public int getIntBundle(String key) {
        return getIntBundle(key, 0);
    }

    /**
     * 从bundle中获取boolean值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public boolean getBooleanBundle(Bundle bundle, String key, boolean defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ObjectJudge.isTrue(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取boolean值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public boolean getBooleanBundle(String key, boolean defaultValue) {
        Bundle bundle = getBundle();
        return getBooleanBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取boolean值
     *
     * @param key
     * @return
     */
    public boolean getBooleanBundle(String key) {
        return getBooleanBundle(key, false);
    }

    /**
     * 从bundle中获取object值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Object getObjectBundle(Bundle bundle, String key, Object defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.get(key);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取object值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Object getObjectBundle(String key, Object defaultValue) {
        Bundle bundle = getBundle();
        return getObjectBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取object值
     *
     * @param key
     * @return
     */
    public Object getObjectBundle(String key) {
        return getObjectBundle(key, null);
    }

    /**
     * 从bundle中获取float值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public float getFloatBundle(Bundle bundle, String key, float defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toFloat(value, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取float值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public float getFloatBundle(String key, float defaultValue) {
        Bundle bundle = getBundle();
        return getFloatBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取float值
     *
     * @param key
     * @return
     */
    public float getFloatBundle(String key) {
        return getFloatBundle(key, 0);
    }

    /**
     * 从bundle中获取double值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public double getDoubleBundle(Bundle bundle, String key, double defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toDouble(value, defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取double值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public double getDoubleBundle(String key, double defaultValue) {
        Bundle bundle = getBundle();
        return getDoubleBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取double值
     *
     * @param key
     * @return
     */
    public double getDoubleBundle(String key) {
        return getDoubleBundle(key, 0);
    }

    /**
     * 从bundle中获取long值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public long getLongBundle(Bundle bundle, String key, long defaultValue) {
        if (bundle != null && bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null) {
                return defaultValue;
            }
            return ConvertUtils.toLong(value, (int) defaultValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取long值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public long getLongBundle(String key, long defaultValue) {
        Bundle bundle = getBundle();
        return getLongBundle(bundle, key, defaultValue);
    }

    /**
     * 从bundle中获取long值
     *
     * @param key
     * @return
     */
    public long getLongBundle(String key) {
        return getLongBundle(key, 0);
    }

    /**
     * 设置对象至bundle
     *
     * @param bundle 当前bundle
     * @param key    数据键
     * @param object 需要设置的数据
     */
    public void setObjectBundle(Bundle bundle, String key, Object object) {
        BundleUtils.setBundleValue(bundle, key, object);
    }

    /**
     * 设置integer类型数据列表
     *
     * @param key  数据键
     * @param list 需要设置的数据
     */
    public void setIntegerArrayList(String key, ArrayList<Integer> list) {
        Bundle bundle = getBundle();
        bundle.putIntegerArrayList(key, list);
    }

    /**
     * integer类型数据列表
     *
     * @param key 数据键
     * @return
     */
    public ArrayList<Integer> getIntegerArrayList(String key) {
        Bundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            ArrayList<Integer> list = bundle.getIntegerArrayList(key);
            return list == null ? new ArrayList<Integer>() : list;
        } else {
            return new ArrayList<Integer>();
        }
    }

    /**
     * 设置String类型数据列表
     *
     * @param key  数据键
     * @param list 需要设置的数据
     */
    public void setStringArrayList(String key, ArrayList<String> list) {
        Bundle bundle = getBundle();
        bundle.putStringArrayList(key, list);
    }

    /**
     * String类型数据列表
     *
     * @param key 数据键
     * @return
     */
    public ArrayList<String> getStringArrayList(String key) {
        Bundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            ArrayList<String> list = bundle.getStringArrayList(key);
            return list == null ? new ArrayList<String>() : list;
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * 从bundle中获取Serializable对象
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public <T> T getSerializableBundle(String key, T defaultValue) {
        Bundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null || !(value instanceof Serializable)) {
                return defaultValue;
            }
            return (T) value;
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取Serializable对象
     *
     * @param key
     * @return
     */
    public <T> T getSerializableBundle(String key) {
        return getSerializableBundle(key, null);
    }

    /**
     * 从bundle中获取Parcelable对象
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public <T> T getParcelableBundle(String key, T defaultValue) {
        Bundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            Object value = bundle.get(key);
            if (value == null || !(value instanceof Parcelable)) {
                return defaultValue;
            }
            return (T) value;
        } else {
            return defaultValue;
        }
    }

    /**
     * 从bundle中获取Parcelable对象
     *
     * @param key
     * @return
     */
    public <T> T getParcelableBundle(String key) {
        return getParcelableBundle(key, null);
    }

    /**
     * 获取当前类action(项目包名+模块全类名)
     * [示例:com.cloud.debris_com.xxx.xxx.ui.[class name]]
     *
     * @param cls 类
     * @return 当前类唯一标识
     */
    public String getClassAction(Class cls) {
        if (cls == null) {
            return "";
        }
        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        String action = String.format("%s_%s", builder.getProjectBuildConfigPackgeName(), cls.getName());
        return action;
    }
}
