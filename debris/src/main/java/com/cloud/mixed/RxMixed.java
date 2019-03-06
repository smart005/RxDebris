package com.cloud.mixed;

import android.content.Context;

import com.cloud.cache.MemoryCache;
import com.cloud.mixed.h5.OnH5WebViewListener;
import com.cloud.objects.events.OnRecyclingListener;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;

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
     * 注册h5监听(原控件渲染过程需在application.onCreate或Activity.onCreate setContextView之前注册)
     *
     * @param listener
     * @param <L>
     */
    public <L extends OnH5WebViewListener> RxMixed registerH5Listener(L listener) {
        MemoryCache.getInstance().setSoftCache("H5WebViewListener", listener);
        return this;
    }

    /**
     * 获取h5脚本回调监听
     *
     * @param <L> OnH5WebViewListener子类型
     * @return
     */
    public <L extends OnH5WebViewListener> L getH5Listener() {
        Object listener = MemoryCache.getInstance().getSoftCache("H5WebViewListener");
        if (listener instanceof OnH5WebViewListener) {
            return (L) listener;
        }
        return null;
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
    public void build(final Context applicationContext) {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean flag) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                isInitedX5 = flag;
                if (flag) {
                    CookieSyncManager.createInstance(applicationContext);
                    CookieSyncManager.getInstance().sync();
                } else {
                    android.webkit.CookieSyncManager.createInstance(applicationContext);
                    android.webkit.CookieSyncManager.getInstance().sync();
                }
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb);
    }
}
