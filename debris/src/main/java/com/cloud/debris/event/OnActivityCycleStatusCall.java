package com.cloud.debris.event;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/20
 * Description:activity cycle status call
 * Modifier:
 * ModifyContent:
 */
public interface OnActivityCycleStatusCall {
    /**
     * 当前周期状态回调
     *
     * @param status 1-前台;2-退至后台;3-已稍毁;
     */
    public void onCurrCycleStatus(int status);
}
