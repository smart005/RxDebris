package com.cloud.images.glide;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:图片请求构建(这里只提供必要的)
 * Modifier:
 * ModifyContent:
 */
public class GlideRequestBuilder {

    private RequestManager manager;
    //图片模型
    private GlideImageModels imageModels;
    //构建处理
    private ImageBuildOptimize optimize;


    public GlideRequestBuilder(RequestManager manager) {
        this.manager = manager;
        optimize = new ImageBuildOptimize();
    }

    /**
     * 获取图片构建对象
     *
     * @param manager 图片请求管理对象
     * @return 构建对象
     */
    public static GlideRequestBuilder getBuilder(RequestManager manager) {
        return new GlideRequestBuilder(manager);
    }

    public ImageBuildOptimize getOptimize() {
        return optimize;
    }

    /**
     * 设置图片加载时占位图
     *
     * @param placeholder 占位图
     */
    public GlideRequestBuilder setPlaceholder(int placeholder) {
        this.optimize.setPlaceholder(placeholder);
        return this;
    }

    /**
     * 设置图片加载的宽度(默认从控件取，未取到则原图大小)
     *
     * @param width 图片宽度
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setWidth(int width) {
        this.optimize.setWidth(width);
        return this;
    }

    /**
     * 设置图片加载高度(默认从控件取，未取到则原图大小)
     *
     * @param height 图片高度
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setHeight(int height) {
        this.optimize.setHeight(height);
        return this;
    }

    /**
     * 设置图片缩放比例(相对于原图的倍数0~1)
     *
     * @param scale 取值范围0~1
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setScale(int scale) {
        this.optimize.setScale(scale);
        return this;
    }

    /**
     * 图片显示优先级
     *
     * @param priority 默认Priority.NORMAL
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setPriority(Priority priority) {
        this.optimize.setPriority(priority);
        return this;
    }

    /**
     * 图片是否正圆显示
     *
     * @param round true-圆;false-安原图的形状显示;
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setRound(boolean round) {
        this.optimize.setRound(round);
        return this;
    }

    /**
     * 获取Glide builder
     *
     * @param imageView 图片控件
     * @return RequestBuilder
     */
    public RequestBuilder<Drawable> getBuilder(ImageView imageView) {
        //获取控件中图片大小
        imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        this.optimize.setWidth(imageView.getMeasuredWidth());
        this.optimize.setHeight(imageView.getMeasuredHeight());
        RequestBuilder<Drawable> builder = this.optimize.loadConfig();
        return builder;
    }

    /**
     * 渲染图片
     *
     * @param imageView 图片控件
     */
    public void into(ImageView imageView) {
        RequestBuilder<Drawable> builder = getBuilder(imageView);
        builder.into(imageView);
    }
}
