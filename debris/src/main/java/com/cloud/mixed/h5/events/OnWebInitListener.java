package com.cloud.mixed.h5.events;

import com.cloud.mixed.h5.H5WebView;
import com.cloud.objects.bases.BundleData;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-13
 * Description:初始化监听
 * Modifier:
 * ModifyContent:
 */
public interface OnWebInitListener {
    /**
     * 初始化
     *
     * @param h5WebView  H5WebView
     * @param bundleData BundleData
     */
    public void onWebInit(H5WebView h5WebView, BundleData bundleData);
}
