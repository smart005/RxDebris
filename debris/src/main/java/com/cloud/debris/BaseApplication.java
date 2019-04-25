package com.cloud.debris;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.cloud.debris.event.OnActivityCycleStatusCall;
import com.cloud.debris.event.OnApplicationLifecycle;
import com.cloud.objects.events.Action2;
import com.cloud.objects.logs.CrashHandler;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/6
 * Description:应用程序基础类;相关生命周期可通过实现{@link com.cloud.debris.event.OnApplicationLifecycle}
 * 和设置
 * Modifier:
 * ModifyContent:
 */
public abstract class BaseApplication extends Application implements OnApplicationLifecycle {

    //当前处于前台的activity数量
    private int countActivity = 0;
    //最近创建的activity
    private Activity recentlyCreateActivity = null;
    //应用是否处于后台
    private boolean isAppOnBackground = false;
    //应用堆栈前一状态,1-前台;2-后台;
    private int prevAppStackStatus = 0;
    //应用堆栈当前状态,1-前台;2-后台;
    private int currAppStackStatus = 0;
    //生命周期回调监听
    private OnApplicationLifecycle onApplicationLifecycle;

    /**
     * 设置生命周期回调监听
     *
     * @param onApplicationLifecycle 生命周期回调监听
     */
    public void setOnApplicationLifecycle(OnApplicationLifecycle onApplicationLifecycle) {
        this.onApplicationLifecycle = onApplicationLifecycle;
    }

    /**
     * 获取最近创建的activity
     *
     * @return
     */
    public Activity getRecentlyCreateActivity() {
        return recentlyCreateActivity;
    }

    /**
     * 获取应用堆栈前一状态
     *
     * @return 1-前台;2-后台;
     */
    public int getPrevAppStackStatus() {
        return prevAppStackStatus;
    }

    /**
     * 获取应用堆栈当前状态
     *
     * @return 1-前台;2-后台;
     */
    public int getCurrAppStackStatus() {
        return currAppStackStatus;
    }

    /**
     * 获取当前activity计数
     *
     * @return
     */
    public int getCountActivity() {
        return countActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.setOnApplicationLifecycle(this);
        registerActivityLifecycle();
        //最后初始化全局异常日志处理
        //便于记录日志记录与拦截
        CrashHandler crashHandler = new CrashHandler() {
            @Override
            protected void onReleaseLogIntercept(Throwable throwable) {
                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onReleaseLogIntercept(throwable);
                }
            }
        };
        crashHandler.init(this, getPackageName());
        if (onApplicationLifecycle != null) {
            onApplicationLifecycle.onApplicationCreated();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isAppOnBackground = true;
            //app stack status
            prevAppStackStatus = currAppStackStatus;
            currAppStackStatus = 2;
            //call into back
            if (onApplicationLifecycle != null) {
                onApplicationLifecycle.onAppSiwtchToBack();
            }
        }
    }

    private void registerActivityLifecycle() {
        final Action2<Activity, Integer> activitySwitchStatueRunnable = new Action2<Activity, Integer>() {
            @Override
            public void call(Activity activity, Integer status) {
                if (!(activity instanceof OnActivityCycleStatusCall)) {
                    return;
                }
                OnActivityCycleStatusCall cycleStatusCall = (OnActivityCycleStatusCall) activity;
                //1-前台;2-退至后台;3-已稍毁;
                cycleStatusCall.onCurrCycleStatus(status);
            }
        };

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                recentlyCreateActivity = activity;
                //回调生命周期
                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onActivityCreated(activity, savedInstanceState);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                //前后切换
                countActivity++;
                if (countActivity > 0) {
                    isAppOnBackground = false;
                    //app stack status
                    prevAppStackStatus = currAppStackStatus;
                    currAppStackStatus = 1;
                    //call into front
                    if (onApplicationLifecycle != null) {
                        onApplicationLifecycle.onAppSiwtchToFront();
                    }
                }
                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onActivityStarted(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                //栈顶activity
                recentlyCreateActivity = activity;
                //设置当前页面切换至后台状态
                //1-前台;2-退至后台;3-已稍毁;
                activitySwitchStatueRunnable.call(activity, 1);

                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onActivityResumed(activity);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onActivityPaused(activity);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //前后台切换
                countActivity--;
                if (countActivity == 0) {
                    isAppOnBackground = true;
                    //app stack status
                    prevAppStackStatus = currAppStackStatus;
                    currAppStackStatus = 2;
                    //call into back
                    if (onApplicationLifecycle != null) {
                        onApplicationLifecycle.onAppSiwtchToBack();
                    }
                }
                //设置当前页面切换至后台状态
                //1-前台;2-退至后台;3-已稍毁;
                activitySwitchStatueRunnable.call(activity, 2);

                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onActivityStopped(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onActivitySaveInstanceState(activity, outState);
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //设置当前页面切换至后台状态
                //1-前台;2-退至后台;3-已稍毁;
                activitySwitchStatueRunnable.call(activity, 3);
                if (recentlyCreateActivity != null && activity == recentlyCreateActivity) {
                    recentlyCreateActivity = null;
                }
                if (onApplicationLifecycle != null) {
                    onApplicationLifecycle.onActivityDestroyed(activity);
                }
            }
        });
    }

    /**
     * @return true在后台运行, false在前台
     */
    public boolean isAppOnBackground() {
        return isAppOnBackground;
    }

    /**
     * 判断应用是否已启动
     *
     * @return
     */
    public boolean isAppStarted() {
        ActivityManager activityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (int i = 0; i < activitys.size(); i++) {
            String baseAcivityName = activitys.get(i).baseActivity.toString();
            // private String accessToken;
            String COMPONENT_INFO = "ComponentInfo{com.changshuo.ui/com.changshuo.ui.activity";
            if (baseAcivityName.startsWith(COMPONENT_INFO)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //created call
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //started call
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //resumed call
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //paused call
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //stopped call
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        //save instance state call
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //destoryed call
    }
}
