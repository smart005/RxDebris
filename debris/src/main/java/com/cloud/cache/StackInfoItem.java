package com.cloud.cache;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/15
 * Description:堆栈信息记录
 * Modifier:
 * ModifyContent:
 */
@Entity(nameInDb = "rx_cache_stack_info")
public class StackInfoItem {

    @Id()
    @Property(nameInDb = "key")
    @Index(unique = true)
    private String key = "";

    /**
     * 堆栈信息
     */
    @Property(nameInDb = "stack")
    private String stack = "";

    @Generated(hash = 1320685753)
    public StackInfoItem(String key, String stack) {
        this.key = key;
        this.stack = stack;
    }

    @Generated(hash = 461612283)
    public StackInfoItem() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
