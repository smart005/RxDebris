package com.cloud.debrisTest.images;

import com.cloud.images.glide.ImageRuleProperties;
import com.cloud.images.glide.OnImageUrlCombinationListener;

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
    public String onUrlCombination(String originalUrl, ImageRuleProperties ruleProperties) {
        return originalUrl;
    }
}
