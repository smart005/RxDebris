package com.cloud.debrisTest;

import android.graphics.PixelFormat;
import android.os.Bundle;

import com.cloud.debris.BaseActivity;
import com.cloud.mixed.RxMixed;
import com.cloud.mixed.h5.H5WebView;
import com.cloud.mixed.h5.JavascriptInterface;
import com.cloud.mixed.h5.OnH5WebViewListener;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/5
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class H5Test extends BaseActivity {

    private H5WebView<OnH5WebViewListener> h5Test = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxMixed.getInstance().registerH5Listener(calls);
        setContentView(R.layout.h5_view);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        h5Test = (H5WebView<OnH5WebViewListener>) findViewById(R.id.h5_test);
        h5Test.bindInterface("cloud");
        h5Test.load("http://www.slcore.com:201");

    }

    public OnH5WebViewListener calls = new OnH5WebViewListener() {
        @Override
        public void addUserAgent(List<String> userAgents) {

        }

        @Override
        public void onTitle(String title) {

        }

        @Override
        public boolean onUrlListener(String url) {
            return false;
        }

        @android.webkit.JavascriptInterface
        @JavascriptInterface
        public String getToken() {
            return null;
        }

        @Override
        public void onLoaded(WebView view, boolean success, int errorCode, String description, String url) {

        }

        @Override
        public void nativeSchemeCall(String scheme) {
            super.nativeSchemeCall(scheme);
        }

        @Override
        public void getSelectText(String selectText) {
            super.getSelectText(selectText);
        }

        @Override
        public void getAPIMethod(String extras) {

        }

        @Override
        public void onCallTel(String tel) {
            super.onCallTel(tel);
        }

        @Override
        public void download(String url, String name) {

        }
    };
}
