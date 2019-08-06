package com.cloud.mixed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-31
 * Description:各业务逻辑注解
 * Modifier:
 * ModifyContent:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HybridLogicBridge {
    /**
     * 对应逻辑bridge class
     *
     * @return bridge class
     */
    Class bridgeClass();

    /**
     * logic bridge key
     *
     * @return key
     */
    String key() default "";

    /**
     * 是否基础bridge
     *
     * @return true-公用js bridge,反之false;
     */
    boolean isBasisBridge() default false;
}
