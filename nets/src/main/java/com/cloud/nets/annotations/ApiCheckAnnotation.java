package com.cloud.nets.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/8/19
 * Description:接口请求验证
 * Modifier:
 * ModifyContent:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiCheckAnnotation {
    /**
     * token验证,默认为false;
     * <p>
     * return true:自动取token值并传入;false:不传;
     */
    boolean isTokenValid() default false;

    /**
     * 是否缓存
     * <p>
     * return
     */
    boolean isCache() default false;

    /**
     * 缓存时间
     * <p>
     * return
     */
    long cacheTime() default 0;

    /**
     * 缓存时间单位
     * <p>
     * return
     */
    TimeUnit cacheTimeUnit() default TimeUnit.SECONDS;

    /**
     * 缓存key
     * <p>
     * return
     */
    String cacheKey() default "859800517";

    /**
     * 在缓存未失败时获取到网络数据和缓存数据均会回调,缓存失效后先请求网络->再缓存->最后返回(即此时只作网络数据的回调)
     *
     * @return
     */
    boolean isCallNCData() default true;
}
