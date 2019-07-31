package com.cloud.mixed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-30
 * Description:hybrid基础交互回调
 * Modifier:
 * ModifyContent:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HybridBasisBridgeCall {
    /**
     * 基础bridge
     *
     * @return bridge class
     */
    Class bridgeClass();

//    /**
//     * web call class
//     *
//     * @return activity call class
//     */
//    Class webCallClass();
}
