package com.cloud.debris.event;

import com.cloud.debris.enums.StatisticalTypes;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/26
 * Description:用于统计的生命周期定义
 * Modifier:
 * ModifyContent:
 */
public interface OnLifeCycleStatistical {

    /**
     * 返回统计类对象
     *
     * @param statisticalTypes 统计类型
     * @return 统计类对象
     */
    public <T> T onStatisticalClassObject(StatisticalTypes statisticalTypes);

    /**
     * 设置统计类对象
     *
     * @param statisticalTypes       统计类型
     * @param statisticalClassObject 统计类对象
     */
    public void setStatisticalClassObject(StatisticalTypes statisticalTypes, Object statisticalClassObject);
}
