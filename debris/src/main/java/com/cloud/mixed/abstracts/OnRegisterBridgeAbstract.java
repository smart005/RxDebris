package com.cloud.mixed.abstracts;

import com.cloud.mixed.h5.H5WebView;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-05
 * Description:
 * Modifier:
 * ModifyContent:
 */
public abstract class OnRegisterBridgeAbstract {

    /**
     * 注册脚本对象
     *
     * @param webView webview
     */
    public abstract void registerBridges(H5WebView webView);
}
