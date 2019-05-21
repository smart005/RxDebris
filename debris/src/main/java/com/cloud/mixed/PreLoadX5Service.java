package com.cloud.mixed;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.cloud.ebus.EBus;
import com.cloud.objects.logs.Logger;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-05-16
 * Description:预加载x5内核
 * Modifier:
 * ModifyContent:
 */
public class PreLoadX5Service extends Service {

    //是否已初始化x5
    private boolean isInitedX5;

    @Override
    public IBinder onBind(Intent intent) {
        PreLoadX5Binder binder = new PreLoadX5Binder();
        return binder;
    }

    class PreLoadX5Binder extends Binder {
        public PreLoadX5Service getService() {
            return PreLoadX5Service.this;
        }
    }

    public boolean isInitedX5() {
        return this.isInitedX5;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.initX5Environment(getApplicationContext(), cb);
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(getApplicationContext(), cb);
        }
    }

    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished(boolean success) {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            isInitedX5 = success;
            try {
                if (success) {
                    CookieSyncManager.createInstance(getApplicationContext());
                    CookieSyncManager.getInstance().sync();
                } else {
                    android.webkit.CookieSyncManager.createInstance(getApplicationContext());
                    android.webkit.CookieSyncManager.getInstance().sync();
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
    };
}
