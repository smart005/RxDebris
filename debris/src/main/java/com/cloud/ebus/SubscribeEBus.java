package com.cloud.ebus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/23
 * Description:EBus事件订阅
 * Modifier:
 * ModifyContent:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubscribeEBus {

    /**
     * 默认同步且在主线程中执行
     *
     * @return
     */
    ThreadModeEBus threadMode() default ThreadModeEBus.MAIN;

    /**
     * 接收key
     *
     * @return
     */
    String receiveKey();
}
