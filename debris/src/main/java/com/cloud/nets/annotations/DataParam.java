package com.cloud.nets.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/6
 * Description:
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataParam {
    /**
     * 数据返回类型
     *
     * @return
     */
    Class value();

    /**
     * 数据返回描述
     *
     * @return
     */
    String description() default "";

    /**
     * 当子类没有属性时，设置此属性后；结果返回后可自动对相应的属性进行设置;
     *
     * @return
     */
    DataPropertyItem[] properties() default {};
}
