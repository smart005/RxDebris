package com.cloud.nets.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2017/11/29
 * Description:
 * Modifier:
 * ModifyContent:
 */
public interface OnAuthCallInfoListener {
    /**
     * 授权回调
     *
     * @param response 授权回调信息
     */
    public void onCallInfo(String response);
}
