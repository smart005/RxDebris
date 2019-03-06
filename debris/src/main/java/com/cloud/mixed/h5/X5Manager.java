package com.cloud.mixed.h5;

import android.app.Application;

import com.cloud.objects.logs.Logger;
import com.tencent.smtt.sdk.QbSdk;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/11/17
 * @Description:x5浏览器管理
 * @Modifier:
 * @ModifyContent:
 */
public class X5Manager {

    private static X5Manager x5Manager = null;

    public static X5Manager getInstance() {
        return x5Manager == null ? x5Manager = new X5Manager() : x5Manager;
    }

    public void initialize(Application application) {
        try {
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                @Override
                public void onViewInitFinished(boolean isLoadSuccess) {

                }

                @Override
                public void onCoreInitFinished() {

                }
            };
            //x5内核初始化接口
            QbSdk.initX5Environment(application, cb);
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
