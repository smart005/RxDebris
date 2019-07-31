package com.cloud.mixed.abstracts;

import com.cloud.mixed.h5.H5WebView;
import com.cloud.mixed.h5.events.OnHybridListener;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-30
 * Description:bridge
 * Modifier:
 * ModifyContent:
 */
public abstract class OnBridgeAbstract implements OnHybridListener {

    private H5WebView webView;

    public void setH5WebView(H5WebView view) {
        this.webView = view;
    }

    @Override
    public boolean onUrlListener(H5WebView webView, String url) {
        return false;
    }

    @Override
    public void addUserAgent(List<String> userAgents) {

    }

    @Override
    public void onLoaded(boolean success, int errorCode, String description, String url) {

    }

    @Override
    public void onCheckJsFunctionCall(String funName, boolean isExist) {

    }

    @Override
    public void getAPIMethod(String extras) {

    }

    @Override
    public void getSelectText(String selectText) {

    }

    @Override
    public void nativeSchemeCall(String scheme) {

    }

    @Override
    public void download(String url, String name) {

    }

    @Override
    public void onCallTel(String tel) {
        //拨打电话功能需要在头部加上<meta name="format-detection" content="telephone=yes"/>
    }

    @Override
    public void onCallSms(String sms) {
        //拨打电话功能需要在头部加上<meta name="format-detection" content="telephone=yes"/>
    }
}
