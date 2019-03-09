package com.cloud.images.glide;

import com.bumptech.glide.load.model.GlideUrl;
import com.cloud.images.RxImage;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class CusGlideUrl extends GlideUrl {

    private ImageRuleProperties properties = null;

    public CusGlideUrl(String url) {
        super(url);
    }

    public void setProperties(ImageRuleProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getCacheKey() {
        //如果gif图片或本地图片则用默认地址
        if (properties == null ||
                properties.isGif() ||
                properties.getImageType() == GlideImageType.fileImage ||
                properties.getImageType() == GlideImageType.resImage ||
                properties.getImageType() == GlideImageType.uriImage) {
            return super.getCacheKey();
        }
        RxImage.ImagesBuilder builder = RxImage.getInstance().getBuilder();
        OnImageUrlCombinationListener combinationListener = builder.getOnImageUrlCombinationListener();
        String url = combinationListener.onUrlCombination(super.getCacheKey(), properties);
        return url;
    }
}
