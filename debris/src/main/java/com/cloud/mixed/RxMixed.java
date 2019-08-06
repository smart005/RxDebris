package com.cloud.mixed;

import android.content.Context;

import com.cloud.ebus.EBus;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.OnRecyclingListener;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.logs.Logger;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/2/2
 * Description:混合模块builder
 * Modifier:
 * ModifyContent:
 */
public class RxMixed implements OnRecyclingListener {

    private static RxMixed rxMixed = null;
    //x5 view是否初始化完成
    private boolean isInitedX5 = false;

    @Override
    public void recycling() {
        rxMixed = null;
    }

    private RxMixed() {
        //init
    }

    public static RxMixed getInstance() {
        return rxMixed == null ? rxMixed = new RxMixed() : rxMixed;
    }

    /**
     * @return true x5内核及view初始化完成,false 首次未下载完成或初始化失败;
     */
    public boolean isInitedX5() {
        return this.isInitedX5;
    }

    /**
     * 构建相关配置
     *
     * @param applicationContext 应用程序上下文
     */
    public void build(Context applicationContext) {
        //设置x5内核
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);

        PreLoadCall preLoadCall = new PreLoadCall(applicationContext);
        QbSdk.initX5Environment(applicationContext, preLoadCall);
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(applicationContext, preLoadCall);
        }
    }

    private class PreLoadCall implements QbSdk.PreInitCallback {

        private Context context;

        public PreLoadCall(Context context) {
            this.context = context;
        }

        @Override
        public void onViewInitFinished(boolean success) {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            isInitedX5 = success;
            try {
                if (ObjectJudge.isMainThread()) {
                    if (success) {
                        CookieSyncManager.createInstance(context);
                        CookieSyncManager.getInstance().sync();
                    } else {
                        android.webkit.CookieSyncManager.createInstance(context);
                        android.webkit.CookieSyncManager.getInstance().sync();
                    }
                } else {
                    HandlerManager.getInstance().post(new RunnableParamsN<Object>() {
                        @Override
                        public void run(Object... objects) {
                            if (isInitedX5) {
                                CookieSyncManager.createInstance(context);
                                CookieSyncManager.getInstance().sync();
                            } else {
                                android.webkit.CookieSyncManager.createInstance(context);
                                android.webkit.CookieSyncManager.getInstance().sync();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                //chrome error
                Logger.error(e);
            }
            //通知x5初始化结束
            EBus.getInstance().post("X5_INIT_FINISHED", success);
        }

        @Override
        public void onCoreInitFinished() {

        }
    }
}
