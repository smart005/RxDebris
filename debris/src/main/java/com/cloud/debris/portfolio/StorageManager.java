package com.cloud.debris.portfolio;

import com.cloud.images2.RxImage;
import com.cloud.objects.storage.StorageUtils;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/22
 * Description:文件库管理
 * Modifier:
 * ModifyContent:
 */
public class StorageManager extends StorageUtils {

    /**
     * 创建图片文件
     *
     * @param fileName 文件名
     * @return 已创建空文件
     */
    public static File createImageFile(String fileName) {
        RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
        File imageCacheDir = builder.getImageCacheDir();
        return getFile(imageCacheDir, fileName, true);
    }
}
