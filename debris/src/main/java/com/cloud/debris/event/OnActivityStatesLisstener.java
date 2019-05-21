package com.cloud.debris.event;

import android.app.Activity;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-05-11
 * Description:activity状态监听
 * Modifier:
 * ModifyContent:
 */
public interface OnActivityStatesLisstener {

    /**
     * 是否主activity(默认为false)
     *
     * @param isMainActivity true-主activity;false-非主页;
     */
    public void setMainActivity(boolean isMainActivity);

    /**
     * activity是否已销毁
     *
     * @param activity 当前activity
     * @return true-已销毁;false-未销毁;
     */
    public boolean isDestroyed(Activity activity);
}
