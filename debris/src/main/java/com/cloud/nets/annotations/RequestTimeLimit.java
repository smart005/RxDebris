package com.cloud.nets.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/6/6
 * Description:请求时间限制;可在请求方法或接口定义上添加;接口定义上设置的值优先于请求方法设置的值;
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestTimeLimit {
    /**
     * 请求总时间,可设置占位符在请求参数中动态替换;
     * (如:"{@link RequestTimePart}参数字段名")
     *
     * @return
     */
    String totalTime() default "";

    /**
     * 网络请求时间单位
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;


}
