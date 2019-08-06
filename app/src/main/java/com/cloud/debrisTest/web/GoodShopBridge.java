package com.cloud.debrisTest.web;

import com.cloud.dataprocessor.annotations.JsBridgeMethod;
import com.cloud.dataprocessor.annotations.RegisterBridges;
import com.cloud.mixed.abstracts.OnRegisterBridgeAbstract;
import com.cloud.mixed.h5.H5WebView;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-31
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class GoodShopBridge extends OnRegisterBridgeAbstract {

    @RegisterBridges(bridgeName = "GoodShop", registerBoxPrefix = "GS", values = {
            @JsBridgeMethod(jsFuns = "download(String url)", describe = "下载")
    })
    @Override
    public void registerBridges(H5WebView webView) {

    }
}
