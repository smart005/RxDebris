package com.cloud.images.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action3;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:图片请求构建(这里只提供必要的)
 * Modifier:
 * ModifyContent:
 */
public class GlideRequestBuilder {

    //构建处理
    private ImageBuildOptimize optimize;
    //request manager
    private RequestManager manager;

    public GlideRequestBuilder(RequestManager manager) {
        this.manager = manager;
        optimize = new ImageBuildOptimize();
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
     * @param width    图片宽度
     * @param isDpUnit true-单位dp;false-单位px;
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setWidth(int width, boolean isDpUnit) {
        if (isDpUnit) {
            float density = this.optimize.getDensity();
            if (density > 0) {
                width = (int) (width * density + 0.5f);
            }
        }
        this.optimize.setWidth(width);
        return this;
    }

    /**
     * 设置图片加载的宽度(默认从控件取，未取到则原图大小)
     *
     * @param width 图片宽度(默认单位px)
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setWidth(int width) {
        float density = this.optimize.getDensity();
        if (density > 0) {
            width = (int) (width * density + 0.5f);
        }
        return setWidth(width, false);
    }

    /**
     * 设置图片加载高度(默认从控件取，未取到则原图大小)
     *
     * @param height   图片高度
     * @param isDpUnit true-单位dp;false-单位px;
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setHeight(int height, boolean isDpUnit) {
        if (isDpUnit) {
            float density = this.optimize.getDensity();
            if (density > 0) {
                height = (int) (height * density + 0.5f);
            }
        }
        this.optimize.setHeight(height);
        return this;
    }

    /**
     * 设置图片加载高度(默认从控件取，未取到则原图大小)
     *
     * @param height 图片高度(默认单位px)
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setHeight(int height) {
        float density = this.optimize.getDensity();
        if (density > 0) {
            height = (int) (height * density + 0.5f);
        }
        return setHeight(height, false);
    }

    /**
     * 设置缩略图的缩放比例(相对于原图的倍数0~1)
     *
     * @param thumbnailScale 取值范围0~1
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setThumbnailScale(float thumbnailScale) {
        this.optimize.setThumbnailScale(thumbnailScale);
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
     * 设置图片的scaleType类型
     *
     * @param scaleType 默认为centerCrop
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setScaleType(ScaleType scaleType) {
        this.optimize.setScaleType(scaleType);
        return this;
    }

    /**
     * 设置图片规则
     *
     * @param imageRule 已拼接的规则
     */
    public GlideRequestBuilder setImageRule(String imageRule) {
        this.optimize.setImageRule(imageRule);
        return this;
    }

    /**
     * 设置图片圆角(由第三方规则处理)
     *
     * @param roundCorners 圆角弧度
     */
    public GlideRequestBuilder setRoundCorners(int roundCorners) {
        this.optimize.setRoundCorners(roundCorners);
        return this;
    }

    public GlideRequestBuilder asGif() {
        this.optimize.setGif(true);
        return this;
    }

    /**
     * 获取Glide builder
     *
     * @param imageView  图片控件
     * @param properties 图片规则相关属性
     * @param call       RequestBuilder回调
     */
    public <T> void getBuilder(ImageView imageView, ImageRuleProperties properties, Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call) {
        if (imageView == null || call == null) {
            return;
        }
        //若未设置width和height(或其中之一<=0)则获取控件的宽高作为图片尺寸
        int width = this.optimize.getWidth();
        int height = this.optimize.getHeight();
        if (width <= 0 || height <= 0) {
            width = imageView.getWidth();
            height = imageView.getHeight();
            if (width <= 0 || height <= 0) {
                imageView.post(new ImagePostRunable<T>(imageView, properties, call));
            } else {
                if (optimize.isGif()) {
                    RequestBuilder builder = this.optimize.loadGifConfig();
                    call.call(builder, imageView, properties);
                } else {
                    RequestBuilder builder = this.optimize.loadConfig();
                    call.call(builder, imageView, properties);
                }
            }
        } else {
            if (optimize.isGif()) {
                RequestBuilder builder = this.optimize.loadGifConfig();
                call.call(builder, imageView, properties);
            } else {
                RequestBuilder builder = this.optimize.loadConfig();
                call.call(builder, imageView, properties);
            }
        }
    }

    private class ImagePostRunable<T> implements Runnable {

        private ImageView imageView;
        private ImageRuleProperties properties;
        private Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call;

        public ImagePostRunable(ImageView imageView, ImageRuleProperties properties, Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call) {
            this.imageView = imageView;
            this.properties = properties;
            this.call = call;
        }

        @Override
        public void run() {
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            optimize.setWidth(width);
            optimize.setHeight(height);
            if (optimize.isGif()) {
                RequestBuilder builder = optimize.loadGifConfig();
                call.call(builder, imageView, properties);
            } else {
                RequestBuilder builder = optimize.loadConfig();
                call.call(builder, imageView, properties);
            }
        }
    }

    /**
     * 渲染图片
     */
    private ImageRuleProperties into() {
        if (manager == null) {
            return null;
        }
        ImageRuleProperties properties = new ImageRuleProperties();
        properties.setRoundCorners(optimize.getRoundCorners());
        properties.setImageRule(optimize.getImageRule());
        properties.setGif(optimize.isGif());
        properties.setImageType(optimize.getImageType());

        CusGlideUrl glideUrl = optimize.getGlideUrl();

        if (optimize.getImageType() == GlideImageType.fileImage) {
            if (optimize.isGif()) {
                //如果是gif则加asGif
                RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                optimize.setGifRequestBuilder(requestBuilder.load(optimize.getFileImage()));
            } else {
                optimize.setRequestBuilder(manager.load(optimize.getFileImage()));
            }
        } else if (optimize.getImageType() == GlideImageType.resImage) {
            if (optimize.isGif()) {
                //如果是gif则加asGif
                RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                optimize.setGifRequestBuilder(requestBuilder.load(optimize.getResImage()));
            } else {
                optimize.setRequestBuilder(manager.load(optimize.getResImage()));
            }
        } else if (optimize.getImageType() == GlideImageType.uriImage) {
            if (optimize.isGif()) {
                //如果是gif则加asGif
                RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                optimize.setGifRequestBuilder(requestBuilder.load(optimize.getUriImage()));
            } else {
                optimize.setRequestBuilder(manager.load(optimize.getUriImage()));
            }
        } else if (optimize.getImageType() == GlideImageType.bitmapImage) {
            //bitmap只针对网络图片
            RequestBuilder<Bitmap> requestBuilder = manager.asBitmap();
            if (glideUrl != null) {
                glideUrl.setProperties(properties);
                optimize.setBitmapRequestBuilder(requestBuilder.load(glideUrl));
            } else if (optimize.getFileImage() != null) {
                optimize.setBitmapRequestBuilder(requestBuilder.load(optimize.getFileImage()));
            } else if (optimize.getResImage() != 0) {
                optimize.setBitmapRequestBuilder(requestBuilder.load(optimize.getResImage()));
            } else if (optimize.getUriImage() != null) {
                optimize.setBitmapRequestBuilder(requestBuilder.load(optimize.getUriImage()));
            }
        } else {
            if (glideUrl == null) {
                return null;
            }
            //初始url拼接相关属性
            glideUrl.setProperties(properties);
            if (optimize.isGif()) {
                //如果是gif则加asGif
                RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                optimize.setGifRequestBuilder(requestBuilder.load(glideUrl));
            } else {
                optimize.setRequestBuilder(manager.load(glideUrl));
            }
        }
        return properties;
    }

    /**
     * 渲染图片
     *
     * @param imageView 图片控件
     * @param call      图片加载完成回调
     */
    public void into(ImageView imageView, final Action2<Drawable, Transition<? super Drawable>> call) {
        ImageRuleProperties properties = this.into();
        if (properties == null) {
            return;
        }
        getBuilder(imageView, properties, new Action3<RequestBuilder<Drawable>, ImageView, ImageRuleProperties>() {
            @Override
            public void call(RequestBuilder<Drawable> builder, ImageView imageView, ImageRuleProperties properties) {
                properties.setWidth(optimize.getWidth());
                properties.setHeight(optimize.getHeight());
                //设置动画
                DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory
                        .Builder(150)
                        .setCrossFadeEnabled(true)
                        .build();
                //https://www.jianshu.com/p/28f5bcee409f
                RequestBuilder<Drawable> transition = builder.transition(DrawableTransitionOptions.with(drawableCrossFadeFactory));
                //渲染图片
                if (call == null) {
                    transition.into(imageView);
                } else {
                    transition.into(new DrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            super.onResourceReady(resource, transition);
                            call.call(resource, transition);
                        }
                    });
                }
            }
        });
    }

    /**
     * 渲染图片
     *
     * @param imageView 图片控件
     */
    public void into(ImageView imageView) {
        into(imageView, null);
    }

    /**
     * 获取bitmap图片
     *
     * @param call bitmap图片回调
     */
    public void into(Action1<Bitmap> call) {
        if (call == null) {
            return;
        }
        ImageRuleProperties properties = this.into();
        if (properties == null) {
            return;
        }
        properties.setWidth(optimize.getWidth());
        properties.setHeight(optimize.getHeight());
        RequestBuilder<Bitmap> builder = this.optimize.loadBitmapConfig();
        builder.into(new BitmapTarget(properties, call));
    }

    private class BitmapTarget extends CustomTarget<Bitmap> {

        private ImageRuleProperties properties;
        private Action1<Bitmap> call;

        public BitmapTarget(ImageRuleProperties properties, Action1<Bitmap> call) {
            this.properties = properties;
            this.call = call;
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            if (call == null) {
                return;
            }
            call.call(resource);
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {
            //占位图加载
        }
    }
}
