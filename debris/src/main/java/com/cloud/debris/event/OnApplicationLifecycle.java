package com.cloud.debris.event;

import android.app.Activity;
import android.os.Bundle;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/6
 * Description:当前应用程序生命周期
 * Modifier:
 * ModifyContent:
 */
public interface OnApplicationLifecycle {

    /**
     * 应用程序被创建时回调
     */
    public void onApplicationCreated();

    /**
     * 窗口创建成功后回调
     *
     * @param activity           当前窗口activity
     * @param savedInstanceState 如果有Bundle数据状态保存此处返回
     */
    public void onActivityCreated(Activity activity, Bundle savedInstanceState);

    /**
     * 窗口页面启动回调
     *
     * @param activity 窗口页面
     */
    public void onActivityStarted(Activity activity);

    /**
     * 窗口从后台回到前台回调
     *
     * @param activity 窗口页面
     */
    public void onActivityResumed(Activity activity);

    /**
     * 窗口页面从前台退到后台回调
     *
     * @param activity 窗口页面
     */
    public void onActivityPaused(Activity activity);

    /**
     * 窗口页面销毁前停止运行处理
     *
     * @param activity 窗口页面
     */
    public void onActivityStopped(Activity activity);

    /**
     * 窗口页面onSaveInstanceState方法被回调处理
     *
     * @param activity 窗口页面
     * @param outState bundle
     */
    public void onActivitySaveInstanceState(Activity activity, Bundle outState);

    /**
     * 窗口页面被销毁时回调
     *
     * @param activity 窗口页面
     */
    public void onActivityDestroyed(Activity activity);

    /**
     * app退到后台回调
     */
    public void onAppSiwtchToBack();

    /**
     * app回到前台回调
     */
    public void onAppSiwtchToFront();

    /**
     * 在release下日志拦截监听
     *
     * @param throwable 异常信息栈
     */
    public void onReleaseLogIntercept(Throwable throwable);
}
