package com.cloud.images.glide;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.RequestManager;

import java.io.File;

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
    //密度
    private float density = 0;
    //临时上下文件(只有加载项目资源目录下的图片有用)
    private Context tempContext = null;

    public GlideImageModels(float density, Context tempContext, RequestManager manager) {
        this.density = density;
        this.tempContext = tempContext;
        requestBuilder = new GlideRequestBuilder(manager);
    }

    /**
     * 加载图片
     *
     * @param url       图片url
     * @param imageType 图片类型
     * @param position  加载图片索引
     * @return GlideRequestBuilder
     */
    private GlideRequestBuilder load(String url, GlideImageType imageType, int position) {
        if (TextUtils.isEmpty(url)) {
            return requestBuilder;
        }
        tempContext = null;
        ImageBuildOptimize optimize = requestBuilder.getOptimize();
        optimize.setDensity(this.density);
        optimize.setImageType(imageType);
        optimize.setGlideUrl(new CusGlideUrl(url));
        optimize.setPosition(position);
        return requestBuilder;
    }

    /**
     * 加载图片
     *
     * @param url 图片url
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder load(String url) {
        return load(url, GlideImageType.netImage, 0);
    }

    /**
     * 加载图片
     *
     * @param file 图片文件
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder load(File file) {
        if (file == null) {
            return requestBuilder;
        }
        tempContext = null;
        ImageBuildOptimize optimize = requestBuilder.getOptimize();
        optimize.setDensity(this.density);
        optimize.setImageType(GlideImageType.fileImage);
        optimize.setFileImage(file);
        optimize.setPosition(0);
        return requestBuilder;
    }

    /**
     * 加载图片
     *
     * @param resId 资源图片
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder load(int resId) {
        if (tempContext == null || resId == 0) {
            return requestBuilder;
        }
        tempContext = null;
        ImageBuildOptimize optimize = requestBuilder.getOptimize();
        optimize.setDensity(this.density);
        optimize.setImageType(GlideImageType.resImage);
        optimize.setResImage(resId);
        optimize.setPosition(0);
        return requestBuilder;
    }

    /**
     * 加载图片
     *
     * @param uri uri图片
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder load(Uri uri) {
        if (tempContext == null || uri == null || uri == Uri.EMPTY) {
            return requestBuilder;
        }
        tempContext = null;
        ImageBuildOptimize optimize = requestBuilder.getOptimize();
        optimize.setDensity(this.density);
        optimize.setImageType(GlideImageType.uriImage);
        optimize.setUriImage(uri);
        optimize.setPosition(0);
        return requestBuilder;
    }

    /**
     * 获取图片构建对象
     *
     * @param density
     * @param tempContext
     * @param manager
     * @return 构建对象
     */
    public static GlideImageModels getBuilder(float density, Context tempContext, RequestManager manager) {
        return new GlideImageModels(density, tempContext, manager);
    }
}
