package com.cloud.images.glide;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:图片url组合监听
 * Modifier:
 * ModifyContent:
 */
public interface OnImageUrlCombinationListener {

    /**
     * 远程图片url组合
     *
     * @param originalUrl    原图片地址
     * @param ruleProperties 规则属性
     * @return
     */
    public String onUrlCombination(String originalUrl, ImageRuleProperties ruleProperties);
}
