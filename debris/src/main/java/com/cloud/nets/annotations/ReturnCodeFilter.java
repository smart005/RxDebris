package com.cloud.nets.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/22
 * Description:接口统一返回码过滤
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReturnCodeFilter {
    /**
     * 对于接口中code包含retCodes()时,将会回调retCodesListeningClass()监听类
     *
     * @return
     */
    String[] retCodes();

    /**
     * 接口返回码监听类
     *
     * @return
     */
    Class<?> retCodesListeningClass();
}
