package com.cloud.mixed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-08
 * Description:
 * Modifier:
 * ModifyContent:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HybridNativeCall {

    /**
     * 优先级:1.HybridNativeCall.schemePath;2.缓存scheme;3.remote api scheme;
     *
     * @return scheme
     */
    String schemePath() default "";

    /**
     * 实现@HybridNativeCall注解的native方法类
     * (用于方法及参数调用)
     *
     * @return class
     */
    Class nativeMethodClass();
}
