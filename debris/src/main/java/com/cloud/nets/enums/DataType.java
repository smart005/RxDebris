package com.cloud.nets.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/6
 * Description:数据类型
 * Modifier:
 * ModifyContent:
 */
public enum DataType {
    /**
     * 缓存数据
     */
    CacheData,
    /**
     * 仅缓存时数据为空(不作网络请求包括首次)
     */
    EmptyForOnlyCache,
    /**
     * 网络数据
     */
    NetData
}
