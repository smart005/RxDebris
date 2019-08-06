package com.cloud.debris;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.ValueCallback;

import com.cloud.mixed.h5.H5WebView;
import com.cloud.mixed.h5.events.OnH5ImageSelectedListener;
import com.cloud.mixed.h5.events.OnWebActivityListener;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-07-30
 * Description:h5页面基础类
 * Modifier:
 * ModifyContent:
 */
public class BaseH5Activity extends BaseFragmentActivity implements OnWebActivityListener {

    private H5WebView webView;

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
        h5WebView.setOnH5ImageSelectedListener(imageSelectedListener);
        if (!TextUtils.isEmpty(bridgeKey)) {
            h5WebView.startBridges(bridgeKey);
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
}
