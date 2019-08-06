package com.cloud.debrisTest.web;

import com.cloud.dataprocessor.annotations.JsBridgeMethod;
import com.cloud.dataprocessor.annotations.RegisterBridges;
import com.cloud.dataprocessor.events.OnScriptRegisterBox;
import com.cloud.mixed.abstracts.OnBridgeAbstract;
import com.cloud.mixed.bridges.BasisScriptRegisterObject;
import com.cloud.mixed.bridges.events.OnBasisBridgeListener;
import com.cloud.mixed.h5.H5WebView;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-30
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BasisBridgeInteraction extends OnBridgeAbstract implements OnBasisBridgeListener {

    @RegisterBridges(bridgeName = "ChangShuoJSBridge", registerBoxPrefix = "Basis", values = {
            @JsBridgeMethod(jsFuns = "getToken(),getUserId()", returnType = "String", describe = "获取用户token"),
            @JsBridgeMethod(jsFuns = "ajax(String json)", describe = "前端请求native接口")
    })
    @Override
    public void registerBridges(H5WebView webView) {
        BasisScriptRegisterObject scriptRegisterObject = new BasisScriptRegisterObject(this);
        OnScriptRegisterBox registerBox = webView.getRegisterBox(scriptRegisterObject.getBridgeName());
        scriptRegisterObject.setRegisterScript(registerBox.getRegisterObject());
        webView.registerBox(scriptRegisterObject);
    }

    @Override
    public void onAjaxCall() {

    }

    @Override
    public void onGetUserIdCall() {

    }

    @Override
    public void onGetTokenCall() {

    }
}
