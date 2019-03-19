package com.cloud.nets.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:授权监听
 * Modifier:
 * ModifyContent:
 */
public interface OnAuthListener {

    /**
     * 登录回调
     *
     * @param requestMethodName 接口请求对应的方法名
     */
    public void onLoginCall(String requestMethodName);
}
