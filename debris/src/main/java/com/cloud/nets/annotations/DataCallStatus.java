package com.cloud.nets.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/2
 * Description:数据回调状态
 * Modifier:
 * ModifyContent:
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataCallStatus {

//    /**
//     * 数据回调类型默认{@link CallStatus}.OnlyNet
//     *
//     * @return CallStatus
//     */
//    CallStatus callStatus() default CallStatus.OnlyNet;
}
