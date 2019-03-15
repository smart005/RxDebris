package com.cloud.nets.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/14
 * Description:错误类型
 * Modifier:
 * ModifyContent:
 */
public enum ErrorType {
    /**
     * (占位参数)
     */
    none,
    /**
     * 网络请求失败
     * (网络本身失败)
     */
    netRequest,
    /**
     * 业务处理失败(请求前验证,数据类型,请求后判断...)
     */
    businessProcess
}
