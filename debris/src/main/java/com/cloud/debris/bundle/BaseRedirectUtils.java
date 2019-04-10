package com.cloud.debris.bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.ebus.EBus;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.beans.MapEntry;
import com.cloud.objects.beans.MapEntryItem;
import com.cloud.objects.utils.BundleUtils;
import com.cloud.objects.utils.OperationUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:
 * Modifier:
 * ModifyContent:
 */
class BaseRedirectUtils {

    //这里的params非空,在调用方法前需做判断;
    private static void putBundleValues(Bundle bundle, MapEntryItem<?>... params) {
        for (MapEntry<String, ?> param : params) {
            BundleUtils.setBundleValue(bundle, param.getKey(), param.getValue());
        }
    }

    /**
     * 启动服务
     *
     * @param context 上下文
     * @param cls     需要启动的服务类
     * @param action  启动服务action,接收时传回
     * @param params  服务启动时传入的数据参数
     */
    public static void startService(Context context, Class<?> cls, String action, MapEntryItem<?>... params) {
        Intent _intent = new Intent();
        if (!TextUtils.isEmpty(action)) {
            _intent.setAction(action);
        }
        _intent.setClass(context, cls);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //map初始扩容值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            _intent.putExtras(bundle);
        }
        context.startService(_intent);
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
     * @param params  传入的bundle数据参数
     */
    public static void startActivity(Context context, Class<?> cls, MapEntryItem<?>... params) {
        Intent _intent = new Intent();
        _intent.setClass(context, cls);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩展初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            _intent.putExtras(bundle);
        }
        context.startActivity(_intent);
    }

    /**
     * 启动activity
     *
     * @param context 上下文
     * @param cls     要启动类对象
     * @param params  传入的bundle数据参数
     */
    public static void startActivityNewTask(Context context, Class<?> cls, MapEntryItem<?>... params) {
        Intent _intent = new Intent();
        _intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _intent.setClass(context, cls);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩展初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            _intent.putExtras(bundle);
        }
        context.startActivity(_intent);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity    提供上下文的activity
     * @param cls         要启动类对象
     * @param requestCode 回调onActivityResult时传回的requestCode,0<requestCode<2^16(65536)
     * @param params      传入的bundle数据参数
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, int requestCode, MapEntryItem<?>... params) {
        Intent _intent = new Intent();
        _intent.setClass(activity, cls);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩容初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            _intent.putExtras(bundle);
        }
        if (requestCode < 0 || requestCode > 65536) {
            //2^16=65536
            //避免requestCode值超出导致启动异常
            requestCode = 0;
        }
        activity.startActivityForResult(_intent, requestCode);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity    提供上下文的activity
     * @param cls         要启动类对象
     * @param requestCode 回调onActivityResult时传回的requestCode,0<requestCode<2^16(65536)
     * @param params      传入的bundle数据参数
     */
    public static void startActivityForResultNewTask(Activity activity, Class<?> cls, int requestCode, MapEntryItem<?>... params) {
        Intent _intent = new Intent();
        _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _intent.setClass(activity, cls);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩容初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            _intent.putExtras(bundle);
        }
        if (requestCode < 0 || requestCode > 65536) {
            //2^16=65536
            //避免requestCode值超出导致启动异常
            requestCode = 0;
        }
        activity.startActivityForResult(_intent, requestCode);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param requestCode   回调onActivityResult中接收的requestCode参数,0<requestCode<2^16(65536)
     * @param params        传入的bundle数据参数
     */
    public static void startActivityForResult(Activity activity, String packageName, String classFullName, int requestCode, MapEntryItem<?>... params) {
        if (activity == null || TextUtils.isEmpty(packageName) || TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(packageName, classFullName);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩展初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            intent.putExtras(bundle);
        }
        if (requestCode < 0 || requestCode > 65536) {
            //2^16=65536
            //避免requestCode值超出导致启动异常
            requestCode = 0;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param classFullName 启动activity的全路径
     * @param requestCode   回调onActivityResult中接收的requestCode参数,0<requestCode<2^16(65536)
     * @param params        传入的bundle数据参数
     */
    public static void startActivityForResult(Activity activity, String classFullName, int requestCode, MapEntryItem<?>... params) {
        startActivityForResult(activity, "", classFullName, requestCode, params);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param packageName   包名
     * @param classFullName 启动activity的全路径
     * @param requestCode   回调onActivityResult中接收的requestCode参数,0<requestCode<2^16(65536)
     * @param params        传入的bundle数据参数
     */
    public static void startActivityForResultNewTask(Activity activity, String packageName, String classFullName, int requestCode, MapEntryItem<?>... params) {
        if (activity == null || TextUtils.isEmpty(packageName) || TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(packageName, classFullName);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩展初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            intent.putExtras(bundle);
        }
        if (requestCode < 0 || requestCode > 65536) {
            //2^16=65536
            //避免requestCode值超出导致启动异常
            requestCode = 0;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 以result方式启动activity
     *
     * @param activity      提供上下文的activity
     * @param classFullName 启动activity的全路径
     * @param requestCode   回调onActivityResult中接收的requestCode参数,0<requestCode<2^16(65536)
     * @param params        传入的bundle数据参数
     */
    public static void startActivityForResultNewTask(Activity activity, String classFullName, int requestCode, MapEntryItem<?>... params) {
        startActivityForResultNewTask(activity, "", classFullName, requestCode, params);
    }

    /**
     * 启动activity
     *
     * @param context       提供上下文的activity
     * @param packageName   包含
     * @param classFullName activity全路径类名
     * @param params        传入的bundle数据参数
     */
    public static void startActivity(Context context, String packageName, String classFullName, MapEntryItem<?>... params) {
        if (context == null || TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent _intent = new Intent();
        if (TextUtils.isEmpty(packageName)) {
            _intent.setClassName(context, classFullName);
        } else {
            _intent.setClassName(packageName, classFullName);
        }
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩展初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            _intent.putExtras(bundle);
        }
        context.startActivity(_intent);
    }

    /**
     * 启动activity
     *
     * @param context       提供上下文的activity
     * @param classFullName activity全路径类名
     * @param params        传入的bundle数据参数
     */
    public static void startActivity(Context context, String classFullName, MapEntryItem<?>... params) {
        startActivity(context, "", classFullName, params);
    }

    /**
     * 启动activity
     *
     * @param context       提供上下文的activity
     * @param classFullName activity全路径类名
     */
    public static void startActivity(Context context, String classFullName) {
        startActivity(context, "", classFullName, (MapEntryItem) null);
    }

    /**
     * 启动activity
     *
     * @param context       提供上下文的activity
     * @param packageName   包含
     * @param classFullName activity全路径类名
     * @param params        传入的bundle数据参数
     */
    public static void startActivityNewTask(Context context, String packageName, String classFullName, MapEntryItem<?>... params) {
        if (context == null || TextUtils.isEmpty(classFullName)) {
            return;
        }
        Intent _intent = new Intent();
        if (TextUtils.isEmpty(packageName)) {
            _intent.setClassName(context, classFullName);
        } else {
            _intent.setClassName(packageName, classFullName);
        }
        _intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!ObjectJudge.isNullOrEmpty(params)) {
            //扩展初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            _intent.putExtras(bundle);
        }
        context.startActivity(_intent);
    }

    /**
     * 启动activity
     *
     * @param context       提供上下文的activity
     * @param classFullName activity全路径类名
     * @param params        传入的bundle数据参数
     */
    public static void startActivityNewTask(Context context, String classFullName, MapEntryItem<?>... params) {
        startActivityNewTask(context, "", classFullName, params);
    }

    /**
     * 启动activity
     *
     * @param context       提供上下文的activity
     * @param classFullName activity全路径类名
     */
    public static void startActivityNewTask(Context context, String classFullName) {
        startActivityNewTask(context, "", classFullName, (MapEntryItem) null);
    }

    /**
     * 结束当前activity
     *
     * @param activity    提供上下文的activity
     * @param requestCode start activity result for request code
     * @param params      start activity result callback params
     */
    public static void finishActivity(Activity activity, int requestCode, MapEntryItem<?>... params) {
        if (requestCode == -62731) {
            activity.finish();
            return;
        }
        if (requestCode < 0 || requestCode > 65536) {
            requestCode = 0;
        }
        if (!ObjectJudge.isNullOrEmpty(params)) {
            Intent intent = new Intent();
            //扩展初始值
            int capacityValue = OperationUtils.getCapacityValue(params.length);
            Bundle bundle = new Bundle(capacityValue);
            putBundleValues(bundle, params);
            intent.putExtras(bundle);
            activity.setResult(requestCode, intent);
        } else {
            activity.setResult(requestCode);
        }
        activity.finish();
    }

    /**
     * 结束当前activity
     *
     * @param activity 提供上下文的activity
     */
    public static void finishActivity(Activity activity) {
        finishActivity(activity, -62731);
    }

    /**
     * 结束当前activity
     *
     * @param activityAction 可以通过ActivityActions.xxx获取
     * @param requestCode    start activity result for request code
     * @param params         start activity result callback params
     */
    public static void finishSelfActivity(String activityAction, int requestCode, MapEntryItem<?>... params) {
        if (TextUtils.isEmpty(activityAction)) {
            return;
        }
        if (requestCode < 0 || requestCode > 65536) {
            requestCode = 0;
        }
        EBus.getInstance().post(activityAction, requestCode, params);
    }
}
