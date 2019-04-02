package com.cloud.debris.event;

import android.os.Bundle;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/15
 * Description:基类属性
 * Modifier:
 * ModifyContent:
 */
public interface OnSupperProperties {
    /**
     * 获取bundle对象
     *
     * @return
     */
    public Bundle getBundle();

    /**
     * 从bundle中获取字符串
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public String getStringBundle(Bundle bundle, String key, String defaultValue);

    /**
     * 从bundle中获取字符串
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public String getStringBundle(String key, String defaultValue);

    /**
     * 从bundle中获取字符串
     *
     * @param key
     * @return
     */
    public String getStringBundle(String key);

    /**
     * 从bundle中获取int值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public int getIntBundle(Bundle bundle, String key, int defaultValue);

    /**
     * 从bundle中获取int值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public int getIntBundle(String key, int defaultValue);

    /**
     * 从bundle中获取int值
     *
     * @param key
     * @return
     */
    public int getIntBundle(String key);

    /**
     * 从bundle中获取boolean值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public boolean getBooleanBundle(Bundle bundle, String key, boolean defaultValue);

    /**
     * 从bundle中获取boolean值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public boolean getBooleanBundle(String key, boolean defaultValue);

    /**
     * 从bundle中获取boolean值
     *
     * @param key
     * @return
     */
    public boolean getBooleanBundle(String key);

    /**
     * 从bundle中获取object值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Object getObjectBundle(Bundle bundle, String key, Object defaultValue);

    /**
     * 从bundle中获取object值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Object getObjectBundle(String key, Object defaultValue);

    /**
     * 从bundle中获取object值
     *
     * @param key
     * @return
     */
    public Object getObjectBundle(String key);

    /**
     * 从bundle中获取float值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public float getFloatBundle(Bundle bundle, String key, float defaultValue);

    /**
     * 从bundle中获取float值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public float getFloatBundle(String key, float defaultValue);

    /**
     * 从bundle中获取float值
     *
     * @param key
     * @return
     */
    public float getFloatBundle(String key);

    /**
     * 从bundle中获取double值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public double getDoubleBundle(Bundle bundle, String key, double defaultValue);

    /**
     * 从bundle中获取double值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public double getDoubleBundle(String key, double defaultValue);

    /**
     * 从bundle中获取double值
     *
     * @param key
     * @return
     */
    public double getDoubleBundle(String key);

    /**
     * 从bundle中获取long值
     *
     * @param bundle       bundle对象
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public long getLongBundle(Bundle bundle, String key, long defaultValue);

    /**
     * 从bundle中获取long值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public long getLongBundle(String key, long defaultValue);

    /**
     * 从bundle中获取long值
     *
     * @param key
     * @return
     */
    public long getLongBundle(String key);

    /**
     * 从bundle中获取Serializable对象
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public <T> T getSerializableBundle(String key, T defaultValue);

    /**
     * 从bundle中获取Serializable对象
     *
     * @param key key
     * @return
     */
    public <T> T getSerializableBundle(String key);

    /**
     * 从bundle中获取Parcelable对象
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public <T> T getParcelableBundle(String key, T defaultValue);

    /**
     * 从bundle中获取Parcelable对象
     *
     * @param key
     * @return
     */
    public <T> T getParcelableBundle(String key);

    /**
     * 获取当前类action
     *
     * @return 当前类唯一标识
     */
    public String getClassAction();
}
