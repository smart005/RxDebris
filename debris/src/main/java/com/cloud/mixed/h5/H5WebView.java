package com.cloud.mixed.h5;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cloud.objects.utils.GlobalUtils;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class H5WebView<L extends OnH5WebViewListener> extends BaseH5WebView {

    /**
     * js接口对象(name-register javascript class)
     */
    public HashMap<String, Object> jsInterfaceObjects = null;

    public H5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //解决5.0以上启动硬件加速导致的异常
        if (Build.VERSION.SDK_INT >= 19) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        this.setDrawingCacheEnabled(true);
    }

    /**
     * 绑定(注册)接口
     *
     * @param interfaceName 接口名
     */
    public void bindInterface(String interfaceName) {
        if (TextUtils.isEmpty(interfaceName)) {
            return;
        }
        //cache interface name
        this.setTag(405409710, interfaceName);
        //init after register script object
        final L webListener = getWebListener();
        if (webListener != null) {
            //注册子类时超类脚本不会被回调,通过JavascriptMethods回调
            this.addJavascriptInterface(webListener, interfaceName);
            //回调native方法
            this.addJavascriptInterface(new JavascriptMethods(webListener), interfaceName);
        }
    }

    /**
     * 获取js方法或接口名-注册脚本对象
     *
     * @return
     */
    public HashMap<String, Object> getJsInterfaceObjects() {
        return jsInterfaceObjects == null ? jsInterfaceObjects = new HashMap<String, Object>() : jsInterfaceObjects;
    }

    private boolean interceptEffectiveScheme(String url) {
        L listener = getWebListener();
        if (listener == null) {
            return false;
        }
        HashMap<String, String> urlParams = GlobalUtils.getUrlParams(url);
        if (urlParams.containsKey("scheme")) {
            String schemeUrl = urlParams.get("scheme");
            if (!TextUtils.isEmpty(schemeUrl)) {
                listener.nativeSchemeCall(schemeUrl);
                return true;
            } else {
                return listener.onUrlListener(url);
            }
        } else {
            return listener.onUrlListener(url);
        }
    }

    @Override
    protected boolean onOverrideUrlLoading(WebView view, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.contains("tel:")) {
            L listener = getWebListener();
            if (listener != null) {
                listener.onCallTel(url);
            }
            return true;
        }
        //拦截apk地址(如果以apk为后缀的则直接下载)
        //取url路径
        Uri uri = Uri.parse(url);
        String path = uri.getPath();
        String suffixName = GlobalUtils.getSuffixName(path);
        if (!TextUtils.isEmpty(suffixName) && TextUtils.equals(suffixName, "apk")) {
            L listener = getWebListener();
            if (listener != null) {
                listener.download(url, "");
            }
            return true;
        }
        //拦截有效schemeUrl
        return interceptEffectiveScheme(url);
    }
}