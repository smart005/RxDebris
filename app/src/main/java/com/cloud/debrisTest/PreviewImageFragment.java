package com.cloud.debrisTest;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloud.debris.fragments.BasePreviewImageFragment;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-06-30
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class PreviewImageFragment extends BasePreviewImageFragment {
    @Override
    protected void onModifiedView(FrameLayout container) {
        FrameLayout.LayoutParams tvparam = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        tvparam.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        TextView textView = new TextView(getContext());
        textView.setText("保存");
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentPreviewImage();
            }
        });
        container.addView(textView, tvparam);
    }

    @Override
    protected void onInitialized() {
        super.setShowCount(true);
    }

    @Override
    protected void onSavedFile(File file) {

    }
}
