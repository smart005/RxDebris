package com.cloud.mixed.h5;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cloud.dataprocessor.events.OnScriptRegisterBox;
import com.cloud.mixed.abstracts.OnBridgeAbstract;
import com.cloud.mixed.abstracts.OnRegisterBridgeAbstract;
import com.cloud.mixed.annotations.HybridLogicBridge;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action2;
import com.cloud.objects.mapper.UrlParamsEntry;
import com.cloud.objects.utils.ConvertUtils;
import com.cloud.objects.utils.GlobalUtils;
import com.cloud.objects.utils.JsonUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/12/13
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class H5WebView extends BaseH5WebView {

    //是否已添加基础js bridge
    private boolean isAddedBasisJsBridge;

    public H5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //解决5.0以上启动硬件加速导致的异常
        if (Build.VERSION.SDK_INT >= 19) {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        this.setDrawingCacheEnabled(true);
    }

    /**
     * 调用bridge对象
     *
     * @param bridgeKeys bridge name keys
     */
    public void startBridges(String... bridgeKeys) {
        if (!isAddedBasisJsBridge) {
            this.addJavascriptInterface(javascriptMethods, "cl_cloud_group_jsm");
            isAddedBasisJsBridge = true;
        }
        if (ObjectJudge.isNullOrEmpty(bridgeKeys)) {
            return;
        }
        List<String> keys = ConvertUtils.toList(bridgeKeys);
        Context context = getContext();
        Class<? extends Context> contextClass = context.getClass();
        //添加basis bridge

        //添加logic bridge
        addLogicBridge(contextClass, keys);
    }

    private void addLogicBridge(Class<? extends Context> contextClass, List<String> keys) {
        Action2<Annotation[], List<String>> action1 = new Action2<Annotation[], List<String>>() {
            @Override
            public void call(Annotation[] annotations, List<String> keys) {
                if (ObjectJudge.isNullOrEmpty(annotations)) {
                    return;
                }
                for (Annotation annotation : annotations) {
                    if (!(annotation instanceof HybridLogicBridge)) {
                        continue;
                    }
                    HybridLogicBridge logicBridge = (HybridLogicBridge) annotation;
                    if (!keys.contains(logicBridge.key())) {
                        continue;
                    }
                    Object obj = JsonUtils.newNull(logicBridge.bridgeClass());
                    if (!(obj instanceof OnRegisterBridgeAbstract)) {
                        continue;
                    }
                    OnRegisterBridgeAbstract bridgeAbstract = (OnRegisterBridgeAbstract) obj;
                    bridgeAbstract.registerBridges(H5WebView.this);
                }
            }
        };
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            HybridLogicBridge[] annotations = contextClass.getAnnotationsByType(HybridLogicBridge.class);
            action1.call(annotations, keys);
        } else {
            Annotation[] annotations = contextClass.getDeclaredAnnotations();
            action1.call(annotations, keys);
        }
    }

    /**
     * 注册对象
     *
     * @param registerBox 实现OnScriptRegisterBox
     */
    public void registerBox(OnScriptRegisterBox registerBox) {
        if (registerBox == null || registerBox.getRegisterObject() == null || TextUtils.isEmpty(registerBox.getBridgeName())) {
            return;
        }
        if (bridgeRegisterMap.containsKey(registerBox.getBridgeName())) {
            return;
        }
        this.addJavascriptInterface(registerBox.getRegisterObject(), registerBox.getBridgeName());
        this.bridgeRegisterMap.put(registerBox.getBridgeName(), registerBox);
    }

    /**
     * 获取注册对象
     *
     * @param bridgeName bridgeName
     * @return OnScriptRegisterBox
     */
    public OnScriptRegisterBox getRegisterBox(String bridgeName) {
        if (TextUtils.isEmpty(bridgeName) || !bridgeRegisterMap.containsKey(bridgeName)) {
            return null;
        }
        return bridgeRegisterMap.get(bridgeName);
    }

    private boolean interceptEffectiveScheme(String url) {
        if (TextUtils.equals(url, "about:blank")) {
            return true;
        }
        OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
        if (bridgeAbstract == null) {
            return false;
        }
        UrlParamsEntry urlParamsEntry = new UrlParamsEntry();
        urlParamsEntry.mapper(url);
        String scheme = urlParamsEntry.getParams("scheme");
        if (!TextUtils.isEmpty(scheme)) {
            bridgeAbstract.nativeSchemeCall(scheme);
            return true;
        } else {
            return bridgeAbstract.onUrlListener(this, url);
        }
    }

    private boolean downloadFile(String url) {
        //拦截apk地址(如果以apk为后缀的则直接下载)
        //取url路径
        Uri uri = Uri.parse(url);
        String path = uri.getPath();
        String suffixName = GlobalUtils.getSuffixName(path);
        List<String> suffixs = Arrays.asList("apk", "rar");
        if (!TextUtils.isEmpty(suffixName) && suffixs.contains(suffixName)) {
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract != null) {
                String segment = uri.getLastPathSegment();
                int index = segment.lastIndexOf(".");
                String name = segment.substring(0, index);
                bridgeAbstract.download(url, name);
            }
            return true;
        }
        return false;
    }

    private boolean callTel(String url) {
        if (url.startsWith("tel:")) {
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract != null) {
                bridgeAbstract.onCallTel(url);
            }
            return true;
        }
        return false;
    }

    private boolean callSms(String url) {
        if (url.startsWith("sms:") || url.startsWith("smsto:")) {
            OnBridgeAbstract bridgeAbstract = getOnBridgeAbstract();
            if (bridgeAbstract != null) {
                bridgeAbstract.onCallSms(url);
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean onOverrideUrlLoading(View view, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        //拨打电话
        if (callTel(url)) {
            return true;
        }
        //发送短信
        if (callSms(url)) {
            return true;
        }
        //文件下载
        if (downloadFile(url)) {
            return true;
        }
        //拦截有效schemeUrl
        return interceptEffectiveScheme(url);
    }

}
