package com.cloud.nets.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/19
 * Description:token位置
 * Modifier:
 * ModifyContent:
 */
public enum TokenLocation {
    /**
     * token信息带在每次请求cookie信息里
     */
    cookie,
    /**
     * token信息带在每次请求头信息里
     */
    header
}
