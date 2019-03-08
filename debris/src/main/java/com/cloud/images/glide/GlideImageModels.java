package com.cloud.images.glide;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:glide图片模型(load的不同形式)
 * Modifier:
 * ModifyContent:
 */
public class GlideImageModels {

    //image builder
    private GlideRequestBuilder requestBuilder;
    //request manager
    private RequestManager manager;

    public GlideImageModels(RequestManager manager) {
        this.manager = manager;
        requestBuilder = GlideRequestBuilder.getBuilder(manager);
    }

    @SuppressLint("CheckResult")
    public GlideRequestBuilder load(RequestBuilder<Drawable> thumbnailBuilder, String url) {
        RequestBuilder<Drawable> builder = manager.load(url);
        //thumbnailBuilder!=null表示需要做缩略图请求
        if (thumbnailBuilder != null) {
            builder.thumbnail(thumbnailBuilder);
        }
        ImageBuildOptimize optimize = requestBuilder.getOptimize();
        optimize.setRequestBuilder(builder);
        return requestBuilder;
    }

    /**
     * 获取图片构建对象
     *
     * @param manager
     * @return 构建对象
     */
    public static GlideImageModels getBuilder(RequestManager manager) {
        return new GlideImageModels(manager);
    }
}
