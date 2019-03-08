package com.cloud.debris.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.objects.ClickEvent;
import com.cloud.objects.ObjectJudge;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseRedirectUtils {
    /**
     * 启动服务
     *
     * @param context 上下文
     * @param cls     需要启动的服务类
     * @param action  启动服务action,接收时传回
     * @param bundle  服务启动时传入的数据bundle
     */
    public static void startService(Context context, Class<?> cls, String action, Bundle bundle) {
        Intent _intent = new Intent();
        if (!TextUtils.isEmpty(action)) {
            _intent.setAction(action);
        }
        _intent.setClass(context, cls);
        if (bundle != null) {
            _intent.putExtras(bundle);
        }
        context.startService(_intent);
    }

    /**
     * 启动服务
     *
     * @param context 上下文
     * @param cls     需要启动的服务类
     * @param action  启动服务action,接收时传回
     */
    public static void startService(Context context, Class<?> cls, String action) {
        startService(context, cls, action, null);
    }

    /**
     * 启动服务
     *
     * @param context 上下文
     * @param cls     需要启动的服务类
     * @param bundle  服务启动时传入的数据bundle
     */
    public static void startService(Context context, Class<?> cls, Bundle bundle) {
        startService(context, cls, "", bundle);
    }

    /**
     * 启动服务
     *
     * @param context 上下文
     * @param cls     需要启动的服务类
     */
    public static void startService(Context context, Class<?> cls) {
        startService(context, cls, "", null);
    }

    /**
     * 停止服务
     *
     * @param context 上下文
     * @param cls     要停止的服务类
     */
    public static void stopService(Context context, Class<?> cls) {
        Intent _intent = new Intent();
        _intent.setClass(context, cls);
        context.stopService(_intent);
    }

    /**
     * 绑定服务
     *
     * @param context 上下文
     * @param conn    服务连接器
     * @param action  服务回调时传回action
     */
    public static void bindService(Context context, ServiceConnection conn, String action) {
        Intent _intent = new Intent(action);
        context.bindService(_intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 取消服务绑定
     *
     * @param context 上下文
     * @param conn    服务连接器
     */
    public static void unbindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

    /**
     * 启动activity
     *
     * @param context 上下文
     * @param cls     要启动类对象
     * @param bundle  传入的bundle数据
     */
    public static void startActivity(Context context, Class<?> cls, Bundle bundle) {
        Intent _intent = new Intent();
        _intent.setClass(context, cls);
        if (bundle != null) {
            _intent.putExtras(bundle);
        }
        if (!ObjectJudge.isRunningActivity(context, cls.getSimpleName(), true)) {
            context.startActivity(_intent);
        }
    }

    /**
     * 以result方式启动activity
     *
     * @param activity    提供上下文的activity
     * @param cls         要启动类对象
     * @param bundle      传入的bundle数据
     * @param requestCode 回调onActivityResult时传回的requestCode
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        Intent _intent = new Intent();
        _intent.setClass(activity, cls);
        if (bundle != null) {
            _intent.putExtras(bundle);
        }
        if (!ObjectJudge.isRunningActivity(activity, cls.getSimpleName(), true)) {
            activity.startActivityForResult(_intent, requestCode);
        }
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param requestCode   回调onActivityResult中接收的requestCode参数
     */
    public static void startActivityForResult(Activity activity,
                                                 String packageName,
                                                 String classFullName,
                                                 int requestCode) {
        startActivityForResult(activity, packageName, classFullName, null, requestCode);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param bundle        传入的bundle数据
     * @param requestCode   回调onActivityResult中接收的requestCode参数
     */
    public static void startActivityForResult(Activity activity,
                                                 String packageName,
                                                 String classFullName,
                                                 Bundle bundle,
                                                 int requestCode) {
        if (TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(packageName, classFullName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (!ObjectJudge.isRunningActivity(activity, classFullName, false)) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 启动activity
     *
     * @param context       提供上下文的activity
     * @param classFullName activity全路径类名
     * @param bundle        传入的bundle数据
     */
    public static void startActivity(Context context, String classFullName, Bundle bundle) {
        if (context == null || TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent _intent = new Intent();
        _intent.setClassName(context, classFullName);
        _intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            _intent.putExtras(bundle);
        }
        context.startActivity(_intent);
    }

    /**
     * 启动指定包下的activity
     *
     * @param context       提供上下文的context
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param bundle        传入的bundle数据
     */
    public static void startActivity(Context context, String packageName, String classFullName, Bundle bundle) {
        if (TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(packageName, classFullName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (!ObjectJudge.isRunningActivity(context, classFullName, false)) {
            context.startActivity(intent);
        }
    }

    /**
     * 启动指定包下的activity
     *
     * @param context       提供上下文的context
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     */
    public static void startActivity(Context context, String packageName, String classFullName) {
        startActivity(context, packageName, classFullName, null);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param classFullName activity全路径类名
     * @param bundle        传入的bundle数据
     * @param requestCode   回调onActivityResult时传回的requestCode
     */
    public static void startActivityForResult(Activity activity,
                                              String classFullName,
                                              Bundle bundle,
                                              int requestCode) {
        if (activity == null || TextUtils.isEmpty(classFullName) || ClickEvent.isFastDoubleClick()) {
            return;
        }
        Intent _intent = new Intent();
        _intent.setClassName(activity, classFullName);
        _intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            _intent.putExtras(bundle);
        }
        if (!ObjectJudge.isRunningActivity(activity, classFullName, false)) {
            activity.startActivityForResult(_intent, requestCode);
        }
    }

    /**
     * 结束当前activity
     *
     * @param activity 提供上下文的activity
     */
    public static void finishActivity(Activity activity) {
        activity.finish();
    }

    /**
     * 启动通话界面
     *
     * @param context     上下文
     * @param phonenumber 电话号码
     */
    public static void startTel(Context context, String phonenumber) {
        Intent intent = null;
        if (TextUtils.isEmpty(phonenumber)) {
            return;
        }
        if (phonenumber.contains("tel")) {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse(phonenumber));
        } else {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse(String.format(
                    "tel:%s", phonenumber)));
        }
        context.startActivity(intent);
    }

    /**
     * 启动桌面
     *
     * @param context 上下文
     */
    public static void startHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }

    /**
     * 判断某个Activity是否存在
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   要判断activity的类名(全路径)
     * @return true:存在;false:不存在;
     */
    public boolean isActivityExist(Context context,
                                   String packageName,
                                   String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        } else {
            return true;
        }
    }
}
