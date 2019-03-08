package com.cloud.debris.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;


/**
 * @Author lijinghuan
 * @Email: ljh0576123@163.com
 * @CreateTime:2016/4/1 18:54
 * @Description:对象跳转工具类
 * @Modifier:
 * @ModifyContent:
 */
public class RedirectUtils extends BaseRedirectUtils {

    /**
     * 启动activity
     *
     * @param context 上下文
     * @param cls     要启动activity的类对象
     */
    public static void startActivity(Context context, Class<?> cls) {
        startActivity(context, cls, null);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity    提供上下文的activity
     * @param cls         要启动activity的类对象
     * @param requestCode 回调onActivityResult中接收的requestCode参数
     */
    public static void startActivityForResult(Activity activity,
                                              Class<?> cls,
                                              int requestCode) {
        startActivityForResult(activity, cls, null, requestCode);
    }

    /**
     * 打开App设置页面
     *
     * @param context 上下文
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }


}
