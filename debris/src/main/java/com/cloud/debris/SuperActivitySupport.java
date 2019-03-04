package com.cloud.debris;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/27
 * Description:超级类支持
 * Modifier:
 * ModifyContent:
 */
class SuperActivitySupport {

    /**
     * 部分机型在调用onResume时内部方法isTopTask()会异常,主要是ContentProvider可能为null
     * 因此在supper.onResume()异常后调用一下此方法
     *
     * @param activity 当前窗口
     */
    public static void callUpActivity(Activity activity) {
        try {
            Class activityClass = Activity.class;
            Field callField = activityClass.getDeclaredField("mCalled");
            callField.setAccessible(true);
            callField.setBoolean(activity, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
