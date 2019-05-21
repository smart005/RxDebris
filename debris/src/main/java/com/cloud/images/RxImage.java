package com.cloud.images;

import android.text.TextUtils;

import com.cloud.cache.MemoryCache;
import com.cloud.images.glide.OnImageUrlCombinationListener;
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

    /**
     * 获取图片配置实例
     *
     * @return RxImage
     */
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
        //图片缓存目录
        private String imageCacheDirName = "image_cache";
        //glide远程图片url组合监听
        private OnImageUrlCombinationListener onImageUrlCombinationListener = null;
        //默认图片
        private int defImage = 0;

        /**
         * 获取图片缓存目录
         *
         * @return File
         */
        public File getImageCacheDir() {
            if (TextUtils.isEmpty(imageCacheDirName) || TextUtils.equals(imageCacheDirName, "image_cache")) {
                Object cacheDir = MemoryCache.getInstance().get("$_DebrisImageCacheDirName");
                imageCacheDirName = String.valueOf(cacheDir);
            }
            File dir = StorageUtils.getDir(imageCacheDirName);
            return dir;
        }

        /**
         * 设置图片缓存目录名称
         *
         * @param imageRootDir 图片缓存目录名称
         * @return ImagesBuilder
         */
        public ImagesBuilder setImageCacheDirName(String imageRootDir) {
            this.imageCacheDirName = imageRootDir;
            MemoryCache.getInstance().set("$_DebrisImageCacheDirName", imageCacheDirName);
            return this;
        }

        /**
         * 获取远程图片url组合监听
         *
         * @return OnImageUrlCombinationListener
         */
        public OnImageUrlCombinationListener getOnImageUrlCombinationListener() {
            if (onImageUrlCombinationListener == null) {
                Object combinationListener = MemoryCache.getInstance().getSoftCache("$_ImageUrlCombinationListener");
                if (combinationListener instanceof OnImageUrlCombinationListener) {
                    onImageUrlCombinationListener = (OnImageUrlCombinationListener) combinationListener;
                }
            }
            return onImageUrlCombinationListener;
        }

        /**
         * 设置远程图片url监听
         *
         * @param listener OnImageUrlCombinationListener
         * @return ImagesBuilder
         */
        public ImagesBuilder setOnImageUrlCombinationListener(OnImageUrlCombinationListener listener) {
            this.onImageUrlCombinationListener = listener;
            MemoryCache.getInstance().setSoftCache("$_ImageUrlCombinationListener", listener);
            return this;
        }

        /**
         * 获取默认图片
         *
         * @return 默认图片
         */
        public int getDefImage() {
            if (defImage == 0) {
                Object object = MemoryCache.getInstance().get("$_DefImage");
                if (object instanceof Integer) {
                    defImage = (int) object;
                }
            }
            return defImage;
        }

        /**
         * 设置默认图片
         *
         * @param defImage 默认图片
         */
        public ImagesBuilder setDefImage(int defImage) {
            this.defImage = defImage;
            MemoryCache.getInstance().set("$_DefImage", defImage);
            return this;
        }
    }

    /**
     * 清空缓存目录
     *
     * @return RxImage
     */
    public RxImage clearCacheDir() {
        ImagesBuilder builder = getBuilder();
        StorageUtils.cleanDirectory(builder.getImageCacheDir());
        return this;
    }
}
