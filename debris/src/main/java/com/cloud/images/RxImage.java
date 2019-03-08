package com.cloud.images;

import android.text.TextUtils;

import com.cloud.cache.MemoryCache;
import com.cloud.objects.config.Recycling;
import com.cloud.objects.events.OnRecyclingListener;
import com.cloud.objects.storage.StorageUtils;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:图片初始化
 * Modifier:
 * ModifyContent:
 */
public class RxImage implements OnRecyclingListener {

    private static RxImage rxImage = null;
    private ImagesBuilder builder = null;

    @Override
    public void recycling() {
        rxImage = null;
    }

    private RxImage() {
        Recycling.getInstance().addRecyclingListener(this);
    }

    public static RxImage getInstance() {
        return rxImage == null ? rxImage = new RxImage() : rxImage;
    }

    /**
     * 获取图片构建对象
     *
     * @return ImagesBuilder
     */
    public ImagesBuilder getBuilder() {
        if (builder == null) {
            builder = new ImagesBuilder();
        }
        return builder;
    }

    public class ImagesBuilder {
        /**
         * 图片缓存目录
         */
        private String imageCacheDir = "image_cache";

        public File getImageCacheDir() {
            if (TextUtils.isEmpty(imageCacheDir) || TextUtils.equals(imageCacheDir, "image_cache")) {
                Object cacheDir = MemoryCache.getInstance().get("$_DebrisImageCacheDir");
                imageCacheDir = String.valueOf(cacheDir);
            }
            File dir = StorageUtils.getDir(imageCacheDir);
            return dir;
        }

        public ImagesBuilder setImageCacheDir(String imageCacheDir) {
            this.imageCacheDir = imageCacheDir;
            MemoryCache.getInstance().set("$_DebrisImageCacheDir", imageCacheDir);
            return this;
        }
    }
}
