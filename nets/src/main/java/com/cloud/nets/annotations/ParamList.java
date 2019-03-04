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
public @interface ParamList {
    /**
     * 参数集合(此参数最终与方法定义的参数同一效果)
     *
     * @return
     */
    String value() default "";

    /**
     * 若该请求字段值为空,则请求时不添加该字段
     * <p>
     * return
     */
    boolean isRemoveEmptyValueField() default true;
}
