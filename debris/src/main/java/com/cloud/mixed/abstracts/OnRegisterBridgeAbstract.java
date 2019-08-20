package com.cloud.mixed.abstracts;

import com.cloud.mixed.h5.H5WebView;
import com.cloud.mixed.h5.events.OnWebInitListener;
import com.cloud.objects.bases.BundleData;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-05
 * Description:
 * Modifier:
 * ModifyContent:
 */
public abstract class OnRegisterBridgeAbstract implements OnWebInitListener {

    @Override
    public void onWebInit(H5WebView h5WebView, BundleData bundleData) {
        //页面初始化时回调
    }

    /**
     * 注册脚本对象
     *
     * @param webView webview
     */
    public abstract void registerBridges(H5WebView webView);
}
