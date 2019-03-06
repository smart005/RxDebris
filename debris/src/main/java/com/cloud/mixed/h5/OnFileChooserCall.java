package com.cloud.mixed.h5;

import android.net.Uri;

import com.tencent.smtt.sdk.ValueCallback;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/6
 * @Description:文件选择
 * @Modifier:
 * @ModifyContent:
 */
public interface OnFileChooserCall {

    public void onFileChooser(ValueCallback<Uri> uploadMsg);

    public void onFileChooserSdk5(ValueCallback<Uri[]> uploadMsg);
}
