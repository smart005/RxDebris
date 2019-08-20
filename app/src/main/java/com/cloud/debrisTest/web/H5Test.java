package com.cloud.debrisTest.web;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.cloud.debris.BaseH5Activity;
import com.cloud.debrisTest.R;
import com.cloud.debrisTest.databinding.H5ViewBinding;
import com.cloud.mixed.annotations.HybridBridges;
import com.cloud.mixed.annotations.HybridLogicBridge;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/5
 * Description:
 * Modifier:
 * ModifyContent:
 */
@HybridBridges(values = {
        @HybridLogicBridge(bridgeClass = BasisBridgeInteraction.class, isBasisBridge = true),
        @HybridLogicBridge(bridgeClass = GoodShopBridge.class, key = LogicBridgeKeys.goodShop)
})
public class H5Test extends BaseH5Activity {

    private H5ViewBinding binding;

    //此类请不要写业务代码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.h5_view);
        //绑定业务交互bridge可以在这里添加
        //bindH5View(binding.h5Test, LogicBridgeKeys.goodShop);
        binding.h5Test.load("http://pandora.yidianzixun.com:3030/article/0MyqDTab?s=op924&appid=s3rd_op924&net=wifi&utk=V2_2yz-dKx2MQZ5ujYl-AiLVWZ5ykQgoPxh0hLtV-w6OoM&log_field=vpk1rl2SN_1BkYeHchShfPf4U1me9PXmLAendvZQDwvQvIcNLHg4x3A8c-XQ1oXMKZGYKW0s3ldVzyEOLEMk0KgFFHWCri8_NG0WX4KIQm_v8wwv_r80UMf3njzleFaOdJP4rAOSEZhi71TQApN12Hi0ZYD1tcu6KWlCU3_G_mnFq4lE_fzSswvhGabaAWEDMY8re1q8Vz-8UnqSF8pWz6fMhtu1yPw9YRR4VbLtign6f5euZz2rI0V3ObtfClZQD7lfSYsklMoeZWeFJw_I22eXldGZEubKPG46hX8_Zfy-Zr9VoPKRLzPqXstZN30nhosav4V6ItL9IkoSdsJiDIFt1hfHeEdkuvqKbw6HwuVPD3AiMzedxb1XUdgD34j-z4FMh_ST1S2shCWijmdA5g&can_comment=1");
        binding.h5Test.setTagsIdsOrClassNames("['header js-header-back header-bottom|none']");
//        binding.h5Test.loadUrl("http://pandora.yidianzixun.com:3030/article/V_03uJjW6R?s=op924&appid=s3rd_op924&net=wifi&utk=V2_cctaMjYIPx0gyks7I3u03GZ5ykQgoPxh0hLtV-w6OoM&log_field=vpk1rl2SN_1BkYeHchShfPf4U1me9PXmLAendvZQDwvQvIcNLHg4x3A8c-XQ1oXMKZGYKW0s3ldVzyEOLEMk0KgFFHWCri8_NG0WX4KIQm_v8wwv_r80UMf3njzleFaOdJP4rAOSEZhi71TQApN12Hi0ZYD1tcu6KWlCU3_G_mnFq4lE_fzSswvhGabaAWEDfTY-afn5qQe9RU2RPKKT2ii-_e_jbx2Xsz9WHUFhTwF4Pmj59lcAbqgetw9mooT96mznCaBjbYDO2eQAqX03RJ3t_AP2WOg1jfIWymx5e3Mkfnn1BtmzjDeojaaA1WiASNs7NCDlqTA2U0nQ7CxyQwDYhJPrXf61NFJOAgoBXx-ItdTZn5OhEwnMLlIqRs67qcBLFH6IdUgZwgMdCJtmOA&can_comment=1");
//        WebSettings settings = binding.h5Test.getSettings();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            settings.setMediaPlaybackRequiresUserGesture(false);
//        }
//        settings.setJavaScriptEnabled(true);
//        settings.setAllowFileAccess(true);
//        binding.h5Test.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                WebSettings webSettings = view.getSettings();
//                webSettings.setMediaPlaybackRequiresUserGesture(true);
//                view.loadUrl("javascript:document.querySelector('video').play();");
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.contains("javascript:")) {
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//        });
//        binding.h5Test.setWebChromeClient(new WebChromeClient() {
//
//        });
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            binding.h5Test.onPause();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            binding.h5Test.onResume();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding.h5Test.resumeTimers();
//        binding.h5Test.destroy();
//    }

//    @Override
//    public void onSettingModified(WebSettings settings) {
//        settings.setMediaPlaybackRequiresUserGesture(false);
//    }

    @Override
    public void onTitle(String title) {

    }
}
