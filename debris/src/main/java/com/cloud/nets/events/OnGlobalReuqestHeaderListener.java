package com.cloud.nets.events;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/5
 * Description:headers params listener 也可以通过@{OkRx.setHeaderParams}来设置
 * Modifier:
 * ModifyContent:
 */
public interface OnGlobalReuqestHeaderListener {

    /**
     * http request header params
     *
     * @return key-value header params
     */
    public HashMap<String, String> onHeaderParams();
}
