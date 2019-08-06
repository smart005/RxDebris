package com.cloud.debris.presets;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-06
 * Description:配合CycleLiveData生命周期使用
 * Modifier:
 * ModifyContent:
 */
public interface OnCycleLiveDataListener {

    /**
     * 请求或绑定触发回调(在页面由非活跃状态进入活跃状态)
     */
    public void onBindTrigger();

    /**
     * 注销绑定(在页面由活跃状态进入非活跃状态)
     */
    public void onUnBindTrigger();
}
