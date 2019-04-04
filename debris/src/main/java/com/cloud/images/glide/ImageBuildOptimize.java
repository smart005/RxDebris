package com.cloud.images.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.signature.ObjectKey;
import com.cloud.images.RxImage;
import com.cloud.images.enums.CacheMode;
import com.cloud.images.enums.GlideCallType;
import com.cloud.images.enums.GlideRequestType;
import com.cloud.images.enums.LoadType;
import com.cloud.images.enums.ScaleType;

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
    private GlideRequestType imageType = GlideRequestType.netImage;
    //文件图片
    private File fileImage = null;
    //资源图片
    private int resImage = 0;
    //uri图片
    private Uri uriImage = null;
    //加载图片索引
    private int position = 0;
    //缓存模式(默认全缓存)
    private CacheMode cacheMode = CacheMode.memoryDisk;
    //散列key,适用于请求url不变但图片已更新情况
    private String hashKey = "";

    public CusGlideUrl getGlideUrl() {
        return glideUrl;
    }

    public void setGlideUrl(CusGlideUrl glideUrl) {
        this.glideUrl = glideUrl;
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
    public GlideRequestType getImageType() {
        return imageType;
    }

    /**
     * 设置图片类型(默认网络图片)
     *
     * @param imageType GlideImageType
     */
    public void setImageType(GlideRequestType imageType) {
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

    /**
     * 设置缓存模式
     *
     * @param cacheMode 缓存模式
     */
    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = cacheMode;
    }

    /**
     * 散列key,适用于请求url不变但图片已更新情况
     *
     * @param hashKey 散列key
     */
    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    /**
     * loadType加载类型{@link com.cloud.images.enums.LoadType}
     */
    public RequestBuilder loadConfig(RequestBuilder requestBuilder, LoadType loadType, GlideCallType callType) {
        //若占位图未设置则取全局设置的默认图片
        if (this.placeholder == 0) {
            RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
            this.placeholder = builder.getDefImage();
        }
        /**
         * 默认的策略是DiskCacheStrategy.AUTOMATIC
         * DiskCacheStrategy有五个常量：
         * DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
         * DiskCacheStrategy.NONE 不使用磁盘缓存
         * DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
         * DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
         * DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
         */
        RequestBuilder builder = (RequestBuilder) requestBuilder.placeholder(this.placeholder)
                .priority(priority)
                //请求超时时间
                .timeout(3000);
        //缩略图相对于原图的比例
        builder = builder.thumbnail(thumbnailScale);
        if (loadType == LoadType.normal) {
            builder = bindScaleType(builder);
        } else if (loadType == LoadType.bitmap) {
            builder = bindBitmapScaleType(builder);
        } else if (loadType == LoadType.file) {
            builder = bindFileScaleType(builder);
        } else if (loadType == LoadType.gif) {
            builder = bindGifScaleType(builder);
        }
        //散列key,适用于请求url不变但图片已更新情况
        if (!TextUtils.isEmpty(hashKey)) {
            //https://blog.csdn.net/iblade/article/details/79865354
            builder = (RequestBuilder) builder.signature(new ObjectKey(hashKey));
        }
        //设置缓存模式
        if (cacheMode == CacheMode.onlyMemory) {
            //此时取消磁盘缓存
            builder = (RequestBuilder) builder.diskCacheStrategy(DiskCacheStrategy.NONE);
        } else {
            DiskCacheStrategy strategy = (callType == GlideCallType.file ? DiskCacheStrategy.DATA : DiskCacheStrategy.ALL);
            builder = (RequestBuilder) builder.diskCacheStrategy(strategy);
        }
        //如果图片宽高非空则重置图片大小
        if (width > 0 && height > 0) {
            builder = (RequestBuilder) builder.override(width, height);
        }
        if (isRound) {
            //对于非gif图片,若图片太大会导致transform失败,因此先进行压缩;
            //对于本地图片需要先压缩则处理
            builder = (RequestBuilder) builder.transform(new GlideCircleTransform());
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
        return builder.skipMemoryCache(true);
    }

    private RequestBuilder<Drawable> bindScaleType(RequestBuilder<Drawable> builder) {
        if (scaleType == ScaleType.centerCrop) {
            return builder.centerCrop();
        } else if (scaleType == ScaleType.centerInside) {
            return builder.centerInside();
        } else if (scaleType == ScaleType.fitCenter) {
            return builder.fitCenter();
        }
        return builder.skipMemoryCache(false);
    }

    private RequestBuilder<GifDrawable> bindGifScaleType(RequestBuilder<GifDrawable> builder) {
        if (scaleType == ScaleType.centerCrop) {
            return builder.centerCrop();
        } else if (scaleType == ScaleType.centerInside) {
            return builder.centerInside();
        } else if (scaleType == ScaleType.fitCenter) {
            return builder.fitCenter();
        }
        return builder.skipMemoryCache(false);
    }

    private RequestBuilder<File> bindFileScaleType(RequestBuilder<File> builder) {
        if (scaleType == ScaleType.centerCrop) {
            return builder.centerCrop();
        } else if (scaleType == ScaleType.centerInside) {
            return builder.centerInside();
        } else if (scaleType == ScaleType.fitCenter) {
            return builder.fitCenter();
        }
        return builder.skipMemoryCache(true);
    }
}
