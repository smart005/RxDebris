package com.cloud.images.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.cloud.images.RxImage;

import java.io.File;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/1
 * Description:图片加载前参数配置优化
 * Modifier:
 * ModifyContent:
 */
class ImageBuildOptimize {

    //图片url
    private CusGlideUrl glideUrl = null;
    //图片请求对象
    private RequestBuilder<Drawable> requestBuilder;
    //gif图片请求对象
    private RequestBuilder<GifDrawable> gifRequestBuilder;
    //bitmap图片
    private RequestBuilder<Bitmap> bitmapRequestBuilder;
    //默认占位图片
    private int placeholder = 0;
    //图片渲染宽度
    private int width = 0;
    //图片渲染高度
    private int height = 0;
    //缩略图的缩放比例
    private float thumbnailScale = 0;
    //显示优先级
    private Priority priority = Priority.NORMAL;
    //是否圆
    private boolean isRound = false;
    //设备密度
    private float density = 0;
    //设置的图片的scaleType类型
    private ScaleType scaleType = ScaleType.centerCrop;
    //第三方跟在图片后面的规则
    private String imageRule = "";
    //图片圆角(由第三方规则处理)
    private int roundCorners = 0;
    //gif图片
    private boolean isGif = false;
    //图片类型(默认网络图片)
    private GlideImageType imageType = GlideImageType.netImage;
    //文件图片
    private File fileImage = null;
    //资源图片
    private int resImage = 0;
    //uri图片
    private Uri uriImage = null;
    //加载图片索引
    private int position = 0;

    public CusGlideUrl getGlideUrl() {
        return glideUrl;
    }

    public void setGlideUrl(CusGlideUrl glideUrl) {
        this.glideUrl = glideUrl;
    }

    public void setRequestBuilder(RequestBuilder<Drawable> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    public void setGifRequestBuilder(RequestBuilder<GifDrawable> gifRequestBuilder) {
        this.gifRequestBuilder = gifRequestBuilder;
    }

    public RequestBuilder<Bitmap> getBitmapRequestBuilder() {
        return bitmapRequestBuilder;
    }

    public void setBitmapRequestBuilder(RequestBuilder<Bitmap> bitmapRequestBuilder) {
        this.bitmapRequestBuilder = bitmapRequestBuilder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    /**
     * 设置图片宽度
     *
     * @param width 宽
     */
    public void setWidth(int width) {
        //取最大宽度
        if (this.width < width) {
            this.width = width;
        }
    }

    /**
     * 获取图片宽
     *
     * @return 宽
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * 设置图片高度
     *
     * @param height 高
     */
    public void setHeight(int height) {
        //取最大高度
        if (this.height < height) {
            this.height = height;
        }
    }

    /**
     * 获取图片高
     *
     * @return 高
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * 缩略图的缩放比例
     *
     * @param thumbnailScale 相对于原图的倍数(0~1)
     */
    public void setThumbnailScale(float thumbnailScale) {
        if (thumbnailScale < 0) {
            return;
        }
        if (thumbnailScale > 1) {
            this.thumbnailScale = 1;
            return;
        }
        this.thumbnailScale = thumbnailScale;
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

    public boolean isRound() {
        return isRound;
    }

    /**
     * 设置图片的scaleType类型
     *
     * @param scaleType ScaleType
     */
    public void setScaleType(ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    /**
     * 获取图片规则
     *
     * @return 已拼接的规则
     */
    public String getImageRule() {
        return imageRule;
    }

    /**
     * 设置图片规则
     *
     * @param imageRule 已拼接的规则
     */
    public void setImageRule(String imageRule) {
        this.imageRule = imageRule;
    }

    /**
     * 获取图片圆角(由第三方规则处理)
     *
     * @return 圆角弧度
     */
    public int getRoundCorners() {
        return roundCorners;
    }

    /**
     * 设置图片圆角(由第三方规则处理)
     *
     * @param roundCorners 圆角弧度
     */
    public void setRoundCorners(int roundCorners) {
        this.roundCorners = roundCorners;
    }

    /**
     * 是否为gif图片
     *
     * @return true-gif;false-jpg/png等
     */
    public boolean isGif() {
        return isGif;
    }

    /**
     * 设置gif图片
     *
     * @param gif true-gif;false-jpg/png等
     */
    public void setGif(boolean gif) {
        isGif = gif;
    }

    /**
     * 获取图片类型(默认网络图片)
     *
     * @return GlideImageType
     */
    public GlideImageType getImageType() {
        return imageType;
    }

    /**
     * 设置图片类型(默认网络图片)
     *
     * @param imageType GlideImageType
     */
    public void setImageType(GlideImageType imageType) {
        this.imageType = imageType;
    }

    /**
     * 获取文件图片
     *
     * @return File
     */
    public File getFileImage() {
        return fileImage;
    }

    /**
     * 设置文件图片
     *
     * @param fileImage 文件图片
     */
    public void setFileImage(File fileImage) {
        this.fileImage = fileImage;
    }

    /**
     * 获取资源图片
     *
     * @return 资源id
     */
    public int getResImage() {
        return resImage;
    }

    /**
     * 设置资源图片
     *
     * @param resImage 资源图片
     */
    public void setResImage(int resImage) {
        this.resImage = resImage;
    }

    /**
     * 获取uri图片
     *
     * @return uri图片
     */
    public Uri getUriImage() {
        return uriImage;
    }

    /**
     * 设置uri图片
     *
     * @param uriImage uri图片
     */
    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }

    /**
     * 获取图片索引
     *
     * @return 图片索引
     */
    public int getPosition() {
        return position;
    }

    /**
     * 设置图片索引
     *
     * @param position 图片索引
     */
    public void setPosition(int position) {
        this.position = position;
    }

    public RequestBuilder<Drawable> loadConfig() {
        //若占位图未设置则取全局设置的默认图片
        if (this.placeholder == 0) {
            RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
            this.placeholder = builder.getDefImage();
        }
        RequestBuilder<Drawable> builder = requestBuilder.placeholder(this.placeholder)
                .thumbnail(thumbnailScale)//缩略图相对于原图的比例
                .priority(priority)
                .timeout(3000)//请求超时时间
                .skipMemoryCache(true)//设置内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        builder = bindScaleType(builder);
        //如果图片宽高非空则重置图片大小
        if (width > 0 && height > 0) {
            builder = builder.override(width, height);
        }
        if (isRound) {
            //对于非gif图片,若图片太大会导致transform失败,因此先进行压缩;
            //对于本地图片需要先压缩则处理
            builder = builder.transform(new GlideCircleTransform());
        }
        return builder;
    }

    public RequestBuilder<Bitmap> loadBitmapConfig() {
        //若占位图未设置则取全局设置的默认图片
        if (this.placeholder == 0) {
            RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
            this.placeholder = builder.getDefImage();
        }
        RequestBuilder<Bitmap> builder = bitmapRequestBuilder.placeholder(this.placeholder)
                .thumbnail(thumbnailScale)//缩略图相对于原图的比例
                .priority(priority)
                .timeout(3000)//请求超时时间
                .skipMemoryCache(true)//设置内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        builder = bindBitmapScaleType(builder);
        //如果图片宽高非空则重置图片大小
        if (width > 0 && height > 0) {
            builder = builder.override(width, height);
        }
        if (isRound) {
            //对于非gif图片,若图片太大会导致transform失败,因此先进行压缩;
            //对于本地图片需要先压缩则处理
            builder = builder.transform(new GlideCircleTransform());
        }
        return builder;
    }

    public RequestBuilder<GifDrawable> loadGifConfig() {
        if (this.placeholder == 0) {
            RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
            this.placeholder = builder.getDefImage();
        }
        RequestBuilder<GifDrawable> builder = gifRequestBuilder.placeholder(this.placeholder)
                .thumbnail(thumbnailScale)
                .priority(priority)
                .timeout(3000)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        builder = bindGifScaleType(builder);
        //如果图片宽高非空则重置图片大小
        if (width > 0 && height > 0) {
            builder = builder.override(width, height);
        }
        if (isRound) {
            builder = builder.transform(new GlideCircleTransform());
        }
        return builder;
    }

    private RequestBuilder<Bitmap> bindBitmapScaleType(RequestBuilder<Bitmap> builder) {
        if (scaleType == ScaleType.centerCrop) {
            return builder.centerCrop();
        } else if (scaleType == ScaleType.centerInside) {
            return builder.centerInside();
        } else if (scaleType == ScaleType.fitCenter) {
            return builder.fitCenter();
        }
        return builder;
    }

    private RequestBuilder<Drawable> bindScaleType(RequestBuilder<Drawable> builder) {
        if (scaleType == ScaleType.centerCrop) {
            return builder.centerCrop();
        } else if (scaleType == ScaleType.centerInside) {
            return builder.centerInside();
        } else if (scaleType == ScaleType.fitCenter) {
            return builder.fitCenter();
        }
        return builder;
    }

    private RequestBuilder<GifDrawable> bindGifScaleType(RequestBuilder<GifDrawable> builder) {
        if (scaleType == ScaleType.centerCrop) {
            return builder.centerCrop();
        } else if (scaleType == ScaleType.centerInside) {
            return builder.centerInside();
        } else if (scaleType == ScaleType.fitCenter) {
            return builder.fitCenter();
        }
        return builder;
    }
}
