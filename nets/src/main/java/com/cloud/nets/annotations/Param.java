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
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value() default "";

    /**
     * 若该请求字段值为空,则请求时不添加该字段
     * <p>
     * return
     */
    boolean isRemoveEmptyValueField() default true;

    /**
     * true:除了file/int/Interger/double/Double/float/Float
     * 其它类型直接转成json;若为string则直接传;如果设置true且value()值为空时其它参数将被忽略;
     * false:按正常字段输入
     *
     * @return
     */
    boolean isJson() default false;
}
