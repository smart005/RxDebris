package com.cloud.debrisTest.images;

import android.text.TextUtils;

import com.cloud.images.glide.ImageRuleProperties;
import com.cloud.images.glide.OnImageUrlCombinationListener;

import java.text.MessageFormat;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class ImageSuffixCombination implements OnImageUrlCombinationListener {

    @Override
    public String onUrlCombination(String originalUrl, ImageRuleProperties properties) {
        String imageRule = properties.getImageRule();
        if (!ImgRuleType.isEffective(imageRule)) {
            return originalUrl;
        }
        int reqIndex = originalUrl.lastIndexOf("?");
        if (reqIndex > 0) {
            //移除原链接中有包含的参数，如果需要添加在最后图片规则后缀拼接完后追加;
            //注：加在前面相应的规则不生效;
            originalUrl = originalUrl.substring(0, reqIndex);
        }
        originalUrl = joinSuffix(originalUrl, imageRule, properties);
        return originalUrl;
    }

    private String joinSuffix(String originalUrl, String imageRule, ImageRuleProperties properties) {
        //七牛图片宽高取值范围在1~9999
        if (properties.getWidth() > 9999) {
            properties.setWidth(9999);
        }
        if (properties.getHeight() > 9999) {
            properties.setHeight(9999);
        }
        StringBuilder builder = new StringBuilder();
        if (TextUtils.equals(imageRule, ImgRuleType.GeometricForWidth.getRule())) {
            //按宽度进行缩放
            imageRule = MessageFormat.format(imageRule, properties.getWidth());
        } else if (TextUtils.equals(imageRule, ImgRuleType.TailoringWHRectangular.getRule())) {
            //裁剪(矩形)
            imageRule = MessageFormat.format(imageRule, properties.getWidth(), properties.getHeight());
        }
        builder.append(originalUrl).append(imageRule);
        //图片最大大小限制
        //根据图片优化规则限制size-limit值 https://demo.codimd.org/hxmSrHlNTgKfpQv2QiiUWw?both
        long pixel = properties.getWidth() * properties.getHeight();
        if (pixel <= 900) {
            builder.append("/size-limit/1k!");
        } else if (pixel <= 10000) {
            builder.append("/size-limit/2k!");
        } else if (pixel <= 40000) {
            builder.append("/size-limit/7k!");
        } else if (pixel <= 90000) {
            builder.append("/size-limit/14k!");
        } else if (pixel <= 250000) {
            builder.append("/size-limit/31k!");
        } else {
            //<1638400[1280x1280] 或其它
            builder.append("/size-limit/100k!");
        }
        return builder.toString();
    }
}
