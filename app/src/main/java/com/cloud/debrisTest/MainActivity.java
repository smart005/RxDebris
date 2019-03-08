package com.cloud.debrisTest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cloud.debris.BaseActivity;
import com.cloud.debrisTest.databinding.MainViewBinding;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/8
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class MainActivity extends BaseActivity {

    private MainViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_view);
        binding.setHandler(this);
    }

    public void OnNetFrameClick(View view) {

    }
}
