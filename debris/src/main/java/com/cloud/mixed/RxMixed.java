package com.cloud.mixed;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cloud.cache.MemoryCache;
import com.cloud.mixed.h5.events.OnH5ImageSelectedListener;
import com.cloud.mixed.h5.OnH5WebViewListener;
import com.cloud.objects.events.OnRecyclingListener;
import com.tencent.smtt.export.external.TbsCoreSettings;
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
    //webview图片选择回调
    private OnH5ImageSelectedListener onH5ImageSelectedListener;

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
    public void build(Context applicationContext) {
        //设置x5内核
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        //启动x5预加载服务
        Intent intent = new Intent(applicationContext, PreLoadX5Service.class);
        applicationContext.startService(intent);
        applicationContext.bindService(intent, x5Connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection x5Connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            if (binder instanceof PreLoadX5Service.PreLoadX5Binder) {
                PreLoadX5Service.PreLoadX5Binder x5Binder = (PreLoadX5Service.PreLoadX5Binder) binder;
                final PreLoadX5Service service = x5Binder.getService();
                isInitedX5 = service.isInitedX5();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 获取h5选择native图片监听
     *
     * @return OnH5ImageSelectedListener
     */
    public OnH5ImageSelectedListener getOnH5ImageSelectedListener() {
        if (onH5ImageSelectedListener == null) {
            Object selectedListener = MemoryCache.getInstance().getSoftCache("$_H5ImageSelectedListener");
            if (selectedListener instanceof OnH5ImageSelectedListener) {
                onH5ImageSelectedListener = (OnH5ImageSelectedListener) selectedListener;
            }
        }
        return onH5ImageSelectedListener;
    }

    /**
     * 设置h5选择native图片监听
     *
     * @param listener 图片选择监听
     * @return RxMixed
     */
    public RxMixed setOnH5ImageSelectedListener(OnH5ImageSelectedListener listener) {
        this.onH5ImageSelectedListener = listener;
        MemoryCache.getInstance().setSoftCache("$_H5ImageSelectedListener", listener);
        return this;
    }
}
