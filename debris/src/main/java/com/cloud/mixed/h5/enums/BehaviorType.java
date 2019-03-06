package com.cloud.mixed.h5.enums;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/1/16
 * @Description:行为类型
 * @Modifier:
 * @ModifyContent:
 */
public enum BehaviorType {
    /**
     * 计数统计
     */
    Count,
    /**
     * 属性统计
     */
    Properties,
    /**
     * 数值统计
     */
    Numerical;

    public static final BehaviorType getBehaviorType(int value) {
        BehaviorType currEnum = null;
        for (BehaviorType e : BehaviorType.values()) {
            if (e.ordinal() == value) {
                currEnum = e;
                break;
            }
        }
        return currEnum;
    }
}
