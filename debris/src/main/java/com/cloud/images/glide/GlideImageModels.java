package com.cloud.images.glide;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.RequestManager;
import com.cloud.images.enums.GlideRequestType;

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
    private float density;
    //临时上下文件(只有加载项目资源目录下的图片有用)
    private Context tempContext;

    public GlideImageModels(float density, Context tempContext, RequestManager manager) {
        this.density = density;
        this.tempContext = tempContext;
        requestBuilder = new GlideRequestBuilder(manager);
    }

    /**
     * 加载图片
     *
     * @param url 图片url
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder load(String url) {
        if (TextUtils.isEmpty(url)) {
            return requestBuilder;
        }
        tempContext = null;
        ImageBuildOptimize optimize = requestBuilder.getOptimize();
        optimize.setDensity(this.density);
        optimize.setGlideUrl(new CusGlideUrl(url));
        optimize.setPosition(0);
        //设置图片类型
        if (url.contains("file://") || TextUtils.equals(url.substring(0, 1), "/")) {
            optimize.setImageType(GlideRequestType.fileImage);
        } else {
            optimize.setImageType(GlideRequestType.netImage);
        }
        return requestBuilder;
    }

//    /**
//     * 加载图片
//     *
//     * @param url 图片url
//     * @return GlideRequestBuilder
//     */
//    public GlideRequestBuilder loadBitmap(String url) {
//        if (TextUtils.isEmpty(url)) {
//            return requestBuilder;
//        }
//        tempContext = null;
//        ImageBuildOptimize optimize = requestBuilder.getOptimize();
//        optimize.setDensity(this.density);
//        optimize.setImageType(GlideImageType.bitmapImage);
//        optimize.setGlideUrl(new CusGlideUrl(url));
//        optimize.setPosition(0);
//        return requestBuilder;
//    }

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
        optimize.setFileImage(file);
        optimize.setPosition(0);
        optimize.setImageType(GlideRequestType.fileImage);
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
        optimize.setResImage(resId);
        optimize.setPosition(0);
        optimize.setImageType(GlideRequestType.resImage);
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
        optimize.setUriImage(uri);
        optimize.setPosition(0);
        optimize.setImageType(GlideRequestType.uriImage);
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
