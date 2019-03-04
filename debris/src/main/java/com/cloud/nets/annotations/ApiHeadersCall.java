package com.cloud.nets.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/16
 * Description:api headers回调
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiHeadersCall {
    /**
     * api唯一标识(区分不同接口的标识符)
     *
     * @return
     */
    String unique();

    /**
     * http请求头回调类(此类需实现OnHttpRequestHeadersListener)
     *
     * @return
     */
    Class<?> requestHeadersCallClass();
}
