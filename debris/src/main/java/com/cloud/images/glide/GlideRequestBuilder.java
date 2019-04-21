package com.cloud.images.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.cloud.cache.PathCacheInfoItem;
import com.cloud.cache.entries.PathCacheDataEntry;
import com.cloud.images.enums.CacheMode;
import com.cloud.images.enums.GlideCallType;
import com.cloud.images.enums.GlideRequestType;
import com.cloud.images.enums.LoadType;
import com.cloud.images.enums.ScaleType;
import com.cloud.objects.events.Action2;
import com.cloud.objects.events.Action3;
import com.cloud.objects.observable.ObservableComponent;
import com.cloud.objects.storage.DirectoryUtils;
import com.cloud.objects.storage.StorageUtils;
import com.cloud.objects.utils.GlobalUtils;

import java.io.File;

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
     * 设置缓存模式
     *
     * @param cacheMode 缓存模式
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setCacheMode(CacheMode cacheMode) {
        this.optimize.setCacheMode(cacheMode);
        return this;
    }

    /**
     * 散列key,适用于请求url不变但图片已更新情况
     *
     * @param hashKey 散列key
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setHashKey(String hashKey) {
        this.optimize.setHashKey(hashKey);
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

    /**
     * 转换为gif类型
     *
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder asGif() {
        this.optimize.setGif(true);
        return this;
    }

    /**
     * 返回类型为File时，文件要移动至目录的目录名
     *
     * @param directoryName 目录名
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder toMove(String directoryName) {
        this.optimize.setMoveDirectoryName(directoryName);
        return this;
    }

    /**
     * 设置高斯模糊半径[1,200]
     *
     * @param blurRadius 高斯模糊半径[1,200]
     * @return GlideRequestBuilder
     */
    public GlideRequestBuilder setBlurRadius(int blurRadius) {
        this.optimize.setBlurRadius(blurRadius);
        return this;
    }

    private class RendBuilderAction<T> implements Action3<ImageRuleProperties, RequestBuilder, String> {

        private Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call;
        private ImageView imageView;
        private LoadType loadType = LoadType.normal;

        public RendBuilderAction(Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call, ImageView imageView, LoadType loadType) {
            this.call = call;
            this.imageView = imageView;
            this.loadType = loadType;
        }

        @Override
        public void call(ImageRuleProperties properties, RequestBuilder requestBuilder, String originalUrl) {
            RequestBuilder builder = optimize.loadConfig(requestBuilder, loadType, GlideCallType.view);
            call.call(builder, imageView, properties);
        }
    }

    /**
     * 获取Glide builder
     *
     * @param imageView 图片控件
     * @param call      RequestBuilder回调
     */
    public <T> void getBuilder(ImageView imageView, Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call) {
        if (imageView == null || call == null) {
            return;
        }
        LoadType loadType = optimize.isGif() ? LoadType.gif : LoadType.normal;
        //若未设置width和height(或其中之一<=0)则获取控件的宽高作为图片尺寸
        int width = this.optimize.getWidth();
        int height = this.optimize.getHeight();
        if (width <= 0 || height <= 0) {
            width = imageView.getWidth();
            height = imageView.getHeight();
            if (width <= 0 || height <= 0) {
                imageView.post(new ImagePostRunable<T>(imageView, loadType, call));
            } else {
                this.into(width, height, GlideCallType.view, new RendBuilderAction(call, imageView, loadType));
            }
        } else {
            this.into(width, height, GlideCallType.view, new RendBuilderAction(call, imageView, loadType));
        }
    }

    private class ImagePostRunable<T> implements Runnable {

        private ImageView imageView;
        private Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call;
        private LoadType loadType = LoadType.normal;

        public ImagePostRunable(ImageView imageView, LoadType loadType, Action3<RequestBuilder<T>, ImageView, ImageRuleProperties> call) {
            this.imageView = imageView;
            this.call = call;
            this.loadType = loadType;
        }

        @Override
        public void run() {
            GlideRequestBuilder.this.into(imageView.getWidth(), imageView.getHeight(), GlideCallType.view, new RendBuilderAction(call, imageView, loadType));
        }
    }

    /**
     * 渲染图片
     */
    private void into(int width, int height, GlideCallType callType, Action3<ImageRuleProperties, RequestBuilder, String> call) {
        if (manager == null || call == null) {
            return;
        }
        ImageRuleProperties properties = new ImageRuleProperties();
        properties.setRoundCorners(optimize.getRoundCorners());
        properties.setImageRule(optimize.getImageRule());
        properties.setGif(optimize.isGif());
        properties.setImageType(optimize.getImageType());
        properties.setWidth(width);
        properties.setHeight(height);

        if (optimize.getImageType() == GlideRequestType.fileImage) {
            if (optimize.isGif()) {
                //如果是gif则加asGif
                RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                call.call(properties, requestBuilder.load(optimize.getFileImage()), "");
            } else {
                call.call(properties, manager.load(optimize.getFileImage()), "");
            }
        } else if (optimize.getImageType() == GlideRequestType.resImage) {
            if (optimize.isGif()) {
                //如果是gif则加asGif
                RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                call.call(properties, requestBuilder.load(optimize.getResImage()), "");
            } else {
                call.call(properties, manager.load(optimize.getResImage()), "");
            }
        } else if (optimize.getImageType() == GlideRequestType.uriImage) {
            if (optimize.isGif()) {
                //如果是gif则加asGif
                RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                call.call(properties, requestBuilder.load(optimize.getUriImage()), "");
            } else {
                call.call(properties, manager.load(optimize.getUriImage()), "");
            }
        } else {
            CusGlideUrl glideUrl = optimize.getGlideUrl();
            //初始url拼接相关属性
            if (callType == GlideCallType.bitmap) {
                RequestBuilder<Bitmap> requestBuilder = manager.asBitmap();
                if (glideUrl != null) {
                    glideUrl.setProperties(properties);
                    call.call(properties, requestBuilder.load(glideUrl.getUrl()), "");
                } else if (optimize.getFileImage() != null) {
                    call.call(properties, requestBuilder.load(optimize.getFileImage()), "");
                } else if (optimize.getResImage() != 0) {
                    call.call(properties, requestBuilder.load(optimize.getResImage()), "");
                } else if (optimize.getUriImage() != null) {
                    call.call(properties, requestBuilder.load(optimize.getUriImage()), "");
                }
            } else if (callType == GlideCallType.file) {
                RequestBuilder<File> requestBuilder = manager.asFile();
                if (glideUrl != null) {
                    glideUrl.setProperties(properties);
                    call.call(properties, requestBuilder.load(glideUrl.getUrl()), "");
                } else if (optimize.getFileImage() != null) {
                    call.call(properties, requestBuilder.load(optimize.getFileImage()), "");
                } else if (optimize.getResImage() != 0) {
                    call.call(properties, requestBuilder.load(optimize.getResImage()), "");
                } else if (optimize.getUriImage() != null) {
                    call.call(properties, requestBuilder.load(optimize.getUriImage()), "");
                }
            } else {
                if (optimize.isGif()) {
                    //如果是gif则加asGif
                    RequestBuilder<GifDrawable> requestBuilder = manager.asGif();
                    call.call(properties, requestBuilder.load(glideUrl.getUrl()), glideUrl.getOriginalUrl());
                } else {
                    call.call(properties, manager.load(glideUrl.getUrl()), glideUrl.getOriginalUrl());
                }
            }
        }
    }

    /**
     * 渲染图片
     *
     * @param imageView 图片控件
     * @param call      图片加载完成回调
     */
    public void into(ImageView imageView, final Action2<Drawable, Transition<? super Drawable>> call) {
        getBuilder(imageView, new Action3<RequestBuilder<Drawable>, ImageView, ImageRuleProperties>() {
            @Override
            public void call(RequestBuilder<Drawable> builder, ImageView imageView, ImageRuleProperties properties) {
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

    private class ObjectBuilderAction<T> implements Action3<ImageRuleProperties, RequestBuilder, String> {

        private GFileCallback<T> call;
        private LoadType loadType;
        private GlideCallType callType;

        public ObjectBuilderAction(GFileCallback<T> call, LoadType loadType, GlideCallType callType) {
            this.call = call;
            this.loadType = loadType;
            this.callType = callType;
        }

        @Override
        public void call(ImageRuleProperties properties, RequestBuilder requestBuilder, String originalUrl) {
            if (call == null) {
                return;
            }
            RequestBuilder<T> builder = optimize.loadConfig(requestBuilder, loadType, callType);
            builder.into(new ObjectTarget(properties, originalUrl, call));
        }
    }

    /**
     * 获取Bitmap图片
     *
     * @param call 图片回调
     */
    public void into(GBitmapCallback call) {
        if (call == null) {
            return;
        }
        this.into(optimize.getWidth(), optimize.getHeight(), GlideCallType.bitmap, new ObjectBuilderAction(call, LoadType.bitmap, GlideCallType.bitmap));
    }

    private class ObjectTarget<T> extends CustomTarget<T> {

        private ImageRuleProperties properties;
        private GFileCallback<T> call;
        private int renderCount = 0;
        private String originalUrl = "";

        public ObjectTarget(ImageRuleProperties properties, String originalUrl, GFileCallback<T> call) {
            this.properties = properties;
            this.originalUrl = originalUrl;
            this.call = call;
        }

        @Override
        public void onResourceReady(@NonNull T resource, @Nullable Transition<? super T> transition) {
            if (call == null || renderCount > 0) {
                return;
            }
            renderCount++;
            //如果resource非File类型只作回调处理
            if (!(resource instanceof File)) {
                this.call.call(resource);
                return;
            }
            File file = (File) resource;
            //缓存图片信息
            cacheImageInfoComponent.build(file, originalUrl, this.call);
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {
            //占位图加载
        }
    }

    private ObservableComponent<File, Object> cacheImageInfoComponent = new ObservableComponent<File, Object>() {
        @Override
        protected File subscribeWith(Object[] params) throws Exception {
            //需要处理的文件
            File file = (File) params[0];
            if (file == null) {
                return null;
            }
            //原url
            String originalUrl = String.valueOf(params[1]);
            if (TextUtils.isEmpty(originalUrl)) {
                return null;
            }
            //移动文件至目标目录
            File targetDir = DirectoryUtils.getInstance().getDirectory(optimize.getMoveDirectoryName());
            String fileName = String.format("%s.%s", GlobalUtils.getGuidNoConnect(), GlobalUtils.getSuffixName(file.getName()));
            File targetFile = new File(targetDir, fileName);
            StorageUtils.copyFile(file, targetFile);
            //移动成功后file-originalUrl-targetFile关联
            PathCacheInfoItem cacheInfoItem = new PathCacheInfoItem();
            cacheInfoItem.setUrl(originalUrl);
            cacheInfoItem.setPath(file.getAbsolutePath());
            cacheInfoItem.setTargetPath(targetFile.getAbsolutePath());
            cacheInfoItem.setName(file.getName());
            PathCacheDataEntry pathCacheDataEntry = new PathCacheDataEntry();
            pathCacheDataEntry.insertOrReplace(cacheInfoItem);
            return targetFile;
        }

        @Override
        protected void nextWith(File file, Object[] params) {
            if (file == null || params[2] == null) {
                return;
            }
            GFileCallback<File> callback = (GFileCallback<File>) params[2];
            callback.call(file);
        }
    };

    /**
     * 获取File图片
     *
     * @param call 图片回调
     */
    public void into(GFileCallback call) {
        if (call == null) {
            return;
        }
        this.into(optimize.getWidth(), optimize.getHeight(), GlideCallType.file, new ObjectBuilderAction(call, LoadType.file, GlideCallType.file));
    }
}
