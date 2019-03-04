package com.cloud.cache;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/28
 * Description:缓存数据项
 * Modifier:
 * ModifyContent:
 */
@Entity(nameInDb = "rx_cache_data")
public class CacheDataItem {

    /**
     * 缓存键
     */
    @Id()
    @Property(nameInDb = "key")
    @Index(unique = true)
    private String key = "";
    /**
     * 缓存值
     */
    @Property(nameInDb = "value")
    private String value = "";
    /**
     * 到期时间(缓存时间+时间段)
     */
    @Property(nameInDb = "effective")
    private long effective = 0;
    /**
     * 状态
     */
    @Property(nameInDb = "flag")
    private boolean flag = false;
    /**
     * int值
     */
    @Property(nameInDb = "iniValue")
    private int iniValue = 0;
    /**
     * long值
     */
    @Property(nameInDb = "longValue")
    private long longValue = 0;

    @Generated(hash = 178030439)
    public CacheDataItem(String key, String value, long effective, boolean flag,
            int iniValue, long longValue) {
        this.key = key;
        this.value = value;
        this.effective = effective;
        this.flag = flag;
        this.iniValue = iniValue;
        this.longValue = longValue;
    }

    @Generated(hash = 2035359245)
    public CacheDataItem() {
    }
    
    /**
     * 获取缓存键
     */
    public String getKey() {
        if (key == null) {
            key = "";
        }
        return key;
    }

    /**
     * 设置缓存键
     *
     * param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取缓存值
     */
    public String getValue() {
        if (value == null) {
            value = "";
        }
        return value;
    }

    /**
     * 设置缓存值
     *
     * param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取到期时间(缓存时间+时间段)
     */
    public long getEffective() {
        return effective;
    }

    /**
     * 设置到期时间(缓存时间+时间段)
     *
     * param effective
     */
    public void setEffective(long effective) {
        this.effective = effective;
    }

    /**
     * 获取状态
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * 设置状态
     *
     * param flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * 获取int值
     */
    public int getIniValue() {
        return iniValue;
    }

    /**
     * 设置int值
     *
     * param iniValue
     */
    public void setIniValue(int iniValue) {
        this.iniValue = iniValue;
    }

    /**
     * 获取long值
     */
    public long getLongValue() {
        return longValue;
    }

    /**
     * 设置long值
     *
     * param longValue
     */
    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }
}
