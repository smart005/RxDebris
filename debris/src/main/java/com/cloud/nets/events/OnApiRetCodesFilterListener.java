package com.cloud.nets.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/22
 * Description:接口返回码过滤监听
 * Modifier:
 * ModifyContent:
 */
public interface OnApiRetCodesFilterListener {

    /**
     * 接口返回码过滤
     *
     * @param returnCode 返回码
     * @param dataObject 数据对象
     */
    public void onApiRetCodesFilter(String returnCode, Object dataObject);
}
