package com.cloud.images.events;

import com.cloud.objects.storage.DirectoryUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/4
 * Description:图片目录监听
 * Modifier:
 * ModifyContent:
 */
public interface OnImageDirectoryListener {

    /**
     * 构建图片目录
     *
     * @param directoryUtils 目录构建工具
     */
    public void onImageDirectoryBuild(DirectoryUtils directoryUtils);
}
