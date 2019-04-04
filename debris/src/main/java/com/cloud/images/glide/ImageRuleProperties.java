package com.cloud.images.glide;

import com.cloud.images.enums.GlideRequestType;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:图片规则属性
 * Modifier:
 * ModifyContent:
 */
public class ImageRuleProperties {

    /**
     * 请求网络图片的宽度
     */
    private int width = 0;
    /**
     * 请求网络图片的高度
     */
    private int height = 0;
    /**
     * 请求网络图片圆角
     */
    private int roundCorners = 0;
    /**
     * 图片规则(一般指第三方给定的规则)
     */
    private String imageRule = "";
    //gif图片
    private boolean isGif = false;
    //图片类型(默认网络图片)
    private GlideRequestType imageType = GlideRequestType.netImage;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRoundCorners() {
        return roundCorners;
    }

    public void setRoundCorners(int roundCorners) {
        this.roundCorners = roundCorners;
    }

    public String getImageRule() {
        return imageRule;
    }

    public void setImageRule(String imageRule) {
        this.imageRule = imageRule;
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
     * @return GlideImageType
     */
    public GlideRequestType getImageType() {
        return imageType;
    }

    /**
     * 设置图片类型(默认网络图片)
     * @param imageType GlideImageType
     */
    public void setImageType(GlideRequestType imageType) {
        this.imageType = imageType;
    }
}
