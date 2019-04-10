package com.cloud.debrisTest.web;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cloud.components.themes.OnThemeViewKeyListener;
import com.cloud.debris.BaseFragmentActivity;
import com.cloud.debrisTest.R;
import com.cloud.debrisTest.databinding.H5ViewBinding;
import com.cloud.mixed.RxMixed;
import com.cloud.mixed.h5.JavascriptInterface;
import com.cloud.mixed.h5.OnH5ImageSelectedListener;
import com.cloud.mixed.h5.OnH5WebViewListener;
import com.tencent.smtt.sdk.ValueCallback;
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
public class H5Test extends BaseFragmentActivity implements OnThemeViewKeyListener, OnH5ImageSelectedListener {

    private H5ViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxMixed.getInstance().registerH5Listener(calls);

        binding = DataBindingUtil.setContentView(this, R.layout.h5_view);

        RxMixed.getInstance().setOnH5ImageSelectedListener(this);
        binding.headTtv.setOnThemeViewKeyListener(this);

        binding.h5Test.bindInterface("mibao");
//        binding.h5Test.load("http://www.slcore.com:201");
//        binding.h5Test.load("http://www.slcore.com:201/upload_image.html");
//    binding.h5Test.load("http://192.168.188.129:8020/worklinks/post_detail.html");
//        File file = new File("/storage/emulated/0/Android/data/com.changshuo.ui/.bundle/bundledest/article_detail/index.html");
//        String content = StorageUtils.readContent(file);
//        binding.h5Test.loadData(content);
        binding.h5Test.load("http://sz.108sq.org:920/shuo/detail/267401");
    }

    @Override
    public void onKeyListener(View view, int id) {
        if (id == R.id.return_itv) {
            binding.h5Test.getSelectText();
        }
    }

    public OnH5WebViewListener calls = new OnH5WebViewListener() {
        @Override
        public void onTitle(String title) {
            binding.headTtv.setTitle(title);
        }

        @android.webkit.JavascriptInterface
        @JavascriptInterface
        public String getToken() {

            //这里获取native登录的令牌
            return null;
        }

        @Override
        public void addUserAgent(List<String> userAgents) {
            super.addUserAgent(userAgents);
        }

        @Override
        public boolean onUrlListener(String url) {
            return super.onUrlListener(url);
        }

        @Override
        public void onLoaded(WebView view, boolean success, int errorCode, String description, String url) {
            super.onLoaded(view, success, errorCode, description, url);
        }

        @Override
        public void getAPIMethod(String extras) {
            super.getAPIMethod(extras);
        }

        @Override
        public void getSelectText(String selectText) {
            super.getSelectText(selectText);
        }

        @Override
        public void nativeSchemeCall(String scheme) {
            super.nativeSchemeCall(scheme);
        }

        @Override
        public void download(String url, String name) {
            super.download(url, name);
        }

        @Override
        public void onCallTel(String tel) {
            super.onCallTel(tel);
        }

        @Override
        public void onCallSms(String sms) {
            super.onCallSms(sms);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message) {
            return super.onJsConfirm(view, url, message);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //h5选择图片后回调并上传
        binding.h5Test.onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void openFileChooserImpl(ValueCallback<Uri> uploadMsg, ValueCallback<Uri[]> sdk5UploadMsg) {
        //h5选择图片回调
        binding.h5Test.selectLocalImages(getActivity());
    }
}
