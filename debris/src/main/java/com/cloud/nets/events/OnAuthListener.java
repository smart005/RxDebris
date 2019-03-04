package com.cloud.nets.events;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:授权监听(获取获取登录token)
 * Modifier:
 * ModifyContent:
 */
public interface OnAuthListener {

    /**
     * 获取权限token
     *
     * @return token
     */
    public String getAuthToken();
}
