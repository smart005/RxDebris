package com.cloud.debris.event;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-08-11
 * Description:下载完成监听
 * Modifier:
 * ModifyContent:
 */
public interface OnApkDownloadCompleteListener {

    /**
     * apk开始下载时回调
     *
     * @param target 用于业务判断此次下载
     * @param url    下载url
     * @param extras 扩展参数
     */
    public void onApkDownloadStart(String target, String url, Object extras);

    /**
     * apk下载
     *
     * @param target       用于业务判断此次下载
     * @param url          下载url
     * @param downloadFile 下载文件
     * @param extras       扩展参数
     */
    public void onApkDownloadComplete(String target, String url, File downloadFile, Object extras);
}
