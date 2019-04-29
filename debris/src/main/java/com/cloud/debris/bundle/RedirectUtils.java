package com.cloud.debris.bundle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.cloud.dialogs.toasty.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


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
     * 打开App设置页面
     *
     * @param context 上下文
     */
    public static void startAppSettings(Context context) {
        Context applicationContext = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", applicationContext.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 启动通话界面
     *
     * @param activity    上下文
     * @param phonenumber 电话号码
     */
    public static void startTel(final FragmentActivity activity, final String phonenumber) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        @SuppressLint("InlinedApi")
        Disposable disposable = rxPermissions.request(Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
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
                            activity.startActivity(intent);
                        } else {
                            ToastUtils.show(activity, "拨打电话权限未开启,请转到设置页面开启.");
                        }
                    }
                });
    }

    /**
     * 启动发送短信界面
     *
     * @param activity    上下文
     * @param phonenumber 电话号码
     */
    public static void startSms(final FragmentActivity activity, final String phonenumber) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        @SuppressLint("InlinedApi")
        Disposable disposable = rxPermissions.request(Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            Intent intent = null;
                            if (TextUtils.isEmpty(phonenumber)) {
                                return;
                            }
                            if (phonenumber.contains("sms") || phonenumber.contains("smsto")) {
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(phonenumber));
                            } else {
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(
                                        "smsto:%s", phonenumber)));
                            }
                            activity.startActivity(intent);
                        } else {
                            ToastUtils.show(activity, "发送短信权限未开启,请转到设置页面开启.");
                        }
                    }
                });
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
