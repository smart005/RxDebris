package com.cloud.images.glide;

import com.cloud.images.RxImage;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class CusGlideUrl {

    private ImageRuleProperties properties = null;
    private String url = "";

    public CusGlideUrl(String url) {
        this.url = url;
    }

    public void setProperties(ImageRuleProperties properties) {
        this.properties = properties;
    }

    public String getUrl() {
        //如果gif图片或本地图片则用默认地址
        if (properties == null ||
                properties.isGif() ||
                properties.getImageType() == GlideImageType.fileImage ||
                properties.getImageType() == GlideImageType.resImage ||
                properties.getImageType() == GlideImageType.uriImage) {
            return url;
        }
        RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
        OnImageUrlCombinationListener combinationListener = builder.getOnImageUrlCombinationListener();
        if (properties.getWidth() > 0 && properties.getHeight() > 0) {
            return combinationListener.onUrlCombination(url, properties);
        } else {
            return url;
        }
    }
}
