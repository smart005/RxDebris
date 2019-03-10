package com.cloud.mixed.h5;

import android.net.Uri;

import com.tencent.smtt.sdk.ValueCallback;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/10
 * Description:图片选择监听
 * Modifier:
 * ModifyContent:
 */
public interface OnH5ImageSelectedListener {

    /**
     * 重写WebChromeClient的openFileChooser回调
     * 扩展浏览器上传文件
     *
     * @param uploadMsg     要上传的文件
     * @param sdk5UploadMsg x5回调
     */
    public void openFileChooserImpl(ValueCallback<Uri> uploadMsg, ValueCallback<Uri[]> sdk5UploadMsg);
}
