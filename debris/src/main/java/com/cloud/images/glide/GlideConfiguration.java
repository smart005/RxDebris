package com.cloud.images.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.cloud.images.RxImage;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/6/13
 * Description:glide缓存对象设置
 * Modifier:
 * ModifyContent:
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //内存缓存
        MemorySizeCalculator.Builder calculatorBuilder = new MemorySizeCalculator.Builder(context);
        MemorySizeCalculator build = calculatorBuilder.build();
        int defaultMemoryCacheSize = build.getMemoryCacheSize();
        int defaultBitmapPoolSize = build.getBitmapPoolSize();
        //图片内存缓存大小为当前应用的总内存的1/4
        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize / 4));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize / 4));
        //磁盘缓存(单位字节)
        int cacheSizeBytes = 1024 * 1024 * 256 * 2;
        //磁盘缓存
        File cacheDir = RxImage.getInstance().getBuilder().getImageCacheDir();
        String diskCacheDir = cacheDir.getAbsolutePath();
        builder.setDiskCache(new DiskLruCacheFactory(diskCacheDir, cacheSizeBytes));
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSizeBytes));
        //设置图片解码格式
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
