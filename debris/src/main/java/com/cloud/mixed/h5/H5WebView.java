package com.cloud.mixed.h5;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cloud.mixed.abstracts.OnBridgeAbstract;
import com.cloud.mixed.annotations.HybridLogicBridge;
import com.cloud.objects.ObjectJudge;
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
     * 添加bridge对象
     *
     * @param bridgeKeys bridge name keys
     */
    public void addBridges(String... bridgeKeys) {
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
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            HybridLogicBridge[] annotations = contextClass.getAnnotationsByType(HybridLogicBridge.class);
            if (ObjectJudge.isNullOrEmpty(annotations)) {
                return;
            }
            for (HybridLogicBridge annotation : annotations) {
                addJavascriptObject(annotation, keys);
            }
        } else {
            Annotation[] annotations = contextClass.getDeclaredAnnotations();
            if (ObjectJudge.isNullOrEmpty(annotations)) {
                return;
            }
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof HybridLogicBridge)) {
                    continue;
                }
                addJavascriptObject((HybridLogicBridge) annotation, keys);
            }
        }
    }

    private void addJavascriptObject(HybridLogicBridge bridge, List<String> keys) {
        if (!keys.contains(bridge.key())) {
            return;
        }
        Object bridgeObject = JsonUtils.newNull(bridge.bridgeClass());
        if (bridgeObject == null) {
            return;
        }
        this.addJavascriptInterface(bridgeObject, bridge.bridgeName());
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
