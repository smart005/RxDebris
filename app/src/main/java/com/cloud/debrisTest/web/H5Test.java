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
        bindH5View(binding.h5Test, LogicBridgeKeys.goodShop);
        binding.h5Test.load("http://192.168.31.52:8020/interaction/index.html");
    }

    @Override
    public void onTitle(String title) {

    }
}
