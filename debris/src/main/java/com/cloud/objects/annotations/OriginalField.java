package com.cloud.objects.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/11
 * Description:将value()中设置的字段,在json中对应的数据赋值到对象中当前注解的字段;
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OriginalField {
    /**
     * json中对应的字段名
     * (列表直接设置集合字段名,每个对象的json会自动对应赋值)
     *
     * @return 字段名
     */
    String value();
}
