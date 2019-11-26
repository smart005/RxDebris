package com.cloud.debris;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;

import com.cloud.cache.DerivedCache;
import com.cloud.mixed.abstracts.OnBridgeAbstract;
import com.cloud.mixed.h5.H5WebView;
import com.cloud.mixed.h5.events.OnH5ImageSelectedListener;
import com.cloud.mixed.h5.events.OnWebActivityListener;
import com.cloud.mixed.h5.events.OnWebCookieListener;
import com.cloud.objects.bases.BundleData;
import com.tencent.smtt.sdk.WebSettings;

import java.util.HashMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-30
 * Description:h5页面基础类
 * Modifier:
 * ModifyContent:
 */
public class BaseH5Activity extends BasicFragmentActivity implements OnWebActivityListener, OnWebCookieListener {

    private H5WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            //是否播放视频状态
            boolean isAutoPlayAudioVideo = intent.getBooleanExtra("isAutoPlayAudioVideo", false);
            String name = this.getClass().getName();
            String apvkey = String.format("%s_isAutoPlayAudioVideo", name);
            DerivedCache.getInstance().put(apvkey, isAutoPlayAudioVideo);
            //标签id或className集合(用于隐藏或显示)
            String tagsArray = intent.getStringExtra("tagsIdsOrClassNames");
            DerivedCache.getInstance().put("$_tagsIdsOrClassNames", tagsArray);
            //是否请求html code
            DerivedCache.getInstance().put("$_isRequestHtml", intent.getBooleanExtra("isRequestHtml", false));
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * bind web view
     *
     * @param h5WebView WebView
     * @param bridgeKey 用于区分本次绑定的是哪个{@link com.cloud.mixed.annotations.HybridLogicBridge}
     */
    protected void bindH5View(H5WebView h5WebView, String bridgeKey) {
        if (h5WebView == null) {
            return;
        }
        this.webView = h5WebView;
        BundleData bundleData = getBundleData();
        h5WebView.setBundleData(bundleData);
        //初始回调
        OnBridgeAbstract onBridgeAbstract = h5WebView.getOnBridgeAbstract();
        if (onBridgeAbstract != null) {
            onBridgeAbstract.onWebInit(h5WebView, bundleData);
        }
        h5WebView.setOnH5ImageSelectedListener(imageSelectedListener);
        h5WebView.startBridges(bridgeKey);
        if (!getBooleanBundle("hasVideo")) {
            //是否启用硬件加速
            h5WebView.setEnableHardwareAcceleration(getBooleanBundle("enableHardwareAcceleration"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (webView != null) {
            //h5选择图片后回调并上传
            webView.onActivityResult(getActivity(), requestCode, resultCode, data);
        }
    }

    private OnH5ImageSelectedListener imageSelectedListener = new OnH5ImageSelectedListener() {
        @Override
        public void openFileChooserImpl(ValueCallback<Uri> uploadMsg, ValueCallback<Uri[]> sdk5UploadMsg) {
            if (webView != null) {
                webView.selectLocalImages(getActivity());
            }
        }
    };

    @Override
    public void onTitle(String title) {
        //html title
    }

    @Override
    public void onSettingModified(WebSettings settings) {
        //x5 WebSettings
    }

    @Override
    public void onSettingModified(android.webkit.WebSettings settings) {
        //android WebSettings
    }

    @Override
    public HashMap<String, String> onWebCookies() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁webview
        if (webView != null) {
            webView.destroy();
        }
        String name = this.getClass().getName();
        String apvkey = String.format("%s_isAutoPlayAudioVideo", name);
        DerivedCache.getInstance().remove(apvkey);
    }
}
