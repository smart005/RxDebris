package com.cloud.images2.glide;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:图片加载前参数配置优化
 * Modifier:
 * ModifyContent:
 */
class ImageBuildOptimize {

    private RequestBuilder<Drawable> requestBuilder;
    //默认占位图片
    private int placeholder = 0;
    //图片渲染宽度
    private int width = 0;
    //图片渲染高度
    private int height = 0;
    //缩略图比例
    private float scale = 0;
    //显示优先级
    private Priority priority = Priority.NORMAL;
    //是否圆
    private boolean isRound = false;

    public void setRequestBuilder(RequestBuilder<Drawable> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public void setWidth(int width) {
        //取最大宽度
        if (this.width < width) {
            this.width = width;
        }
    }

    public void setHeight(int height) {
        //取最大高度
        if (this.height < height) {
            this.height = height;
        }
    }

    /**
     * 缩略图比例
     *
     * @param scale 相对于原图的倍数(0~1)
     */
    public void setScale(float scale) {
        if (scale < 0) {
            return;
        }
        if (scale > 1) {
            this.scale = 1;
            return;
        }
        this.scale = scale;
    }

    /**
     * 图片显示优先级
     *
     * @param priority 默认Priority.NORMAL
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * 图片是否正圆显示
     *
     * @param round true-圆;false-安原图的形状显示;
     */
    public void setRound(boolean round) {
        isRound = round;
    }

    @SuppressLint("CheckResult")
    public RequestBuilder<Drawable> loadConfig() {
//        //占位图设置
//        RequestBuilder<Drawable> builder = requestBuilder.placeholder(this.placeholder)
//                .thumbnail(scale)//缩略图相对于原图的比例
//                .priority(priority)
//                .timeout(3000)//请求超时时间
//                .skipMemoryCache(false)//设置内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//        //如果图片宽高非空则重置图片大小
//        if (width > 0 && height > 0) {
//            builder = builder.override(width, height);
//        }
//        if (isRound) {
//            builder = builder.transform(new GlideCircleTransform());
//        }
//        return builder;
        return null;
    }
}
