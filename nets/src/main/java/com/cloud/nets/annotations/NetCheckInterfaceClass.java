package com.cloud.nets.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/8
 * Description:网络检测接口类
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NetCheckInterfaceClass {
    /**
     * 网络检测接口类
     *
     * @return 已实现的类
     */
    Class<?> value();
}
