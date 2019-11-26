package com.cloud.debris;

import android.app.Activity;

import com.cloud.cache.DerivedCache;
import com.cloud.coms.dialogs.BaseFloatViewDialog;
import com.cloud.objects.beans.MapEntryItem;
import com.cloud.objects.utils.SharedPrefUtils;

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
            //e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        }
    }

    /**
     * 是否主activity(默认为false)
     *
     * @param cls            当前处理类
     * @param isMainActivity true-主activity;false-非主页;
     */
    public static void setMainActivity(Class cls, boolean isMainActivity) {
        if (isMainActivity) {
            DerivedCache.getInstance().put("20882af386ee4fb1b052f8d701527402",
                    new MapEntryItem("isLaunch", true),
                    new MapEntryItem("pageName", cls.getSimpleName()));
        } else {
            DerivedCache.getInstance().clear("20882af386ee4fb1b052f8d701527402");
        }
    }

    public static boolean isProjectActivitys(Activity activity) {
        if (activity instanceof BasicActivity ||
                activity instanceof BasicCompatActivity ||
                activity instanceof BasicFragmentActivity) {
            return true;
        }
        return false;
    }

    /**
     * 启动时移除全局浮动视图
     *
     * @param activity activity
     */
    public static void removeGlobalFloatViews(Activity activity) {
        if (!isProjectActivitys(activity)) {
            return;
        }
        boolean state = SharedPrefUtils.getPrefBoolean(activity, "$_global_float_dialog_destoryed");
        if (state) {
            BaseFloatViewDialog.removeFloatViewForActivity(activity, R.id.cl_tip_view_rl);
        }
    }
}
