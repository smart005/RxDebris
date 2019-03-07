package com.cloud.nets.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/6
 * Description:请求成功后,数据回调状态
 * Modifier:
 * ModifyContent:
 */
public enum CallStatus {
    /**
     * 网络请求-在缓存未失效时网络数据与缓存只会返回其中一个,缓存失效后先请求网络->再缓存->最后返回;
     * 即首次请求时缓存失效的情况会走网络,否则每次只取缓存数据;
     */
    OnlyCache,
    /**
     * 1.在缓存未失效先返回缓存数据,若在{@link com.cloud.nets.annotations.ApiCheckAnnotation}.cacheTime()
     * 时间内但cacheIntervalTime()已失效(其中cacheTime()>cacheIntervalTime())此时只请求网络缓存但不回调;
     * 2.缓存失效后先请求网络->再缓存->最后返回;
     */
    PersistentIntervalCache,
    /**
     * 每次只作网络请求;
     */
    OnlyNet,
    /**
     * 网络请求-在缓存未失败时获取到网络数据和缓存数据均会回调,缓存失效后先请求网络->再缓存->最后返回(即此时只作网络数据的回调);
     * 1.有缓存时先回调缓存数据再请求网络数据然后[缓存+回调];
     * 2.无缓存时不作缓存回调直接请求网络数据后[缓存+回调];
     */
    WeakCacheAccept,
    /**
     * 1.有缓存时先回调缓存数据再请求网络数据然后[缓存]不作网络回调;
     * 2.无缓存时不作缓存回调直接请求网络数据后[缓存]不作网络回调;
     */
    WeakCache
}
