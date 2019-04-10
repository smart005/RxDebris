package com.cloud.debrisTest.images;

import android.text.TextUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2016/12/30
 * Description:图片规则类型
 * Modifier:
 * ModifyContent:
 */
public enum ImgRuleType {

    /**
     * auto-orient:建议放在首位，根据原图EXIF信息自动旋正，便于后续处理
     * thumbnail/[Width]x:指定目标图片宽度，高度等比缩放，Width取值范围1-9999
     * ignore-error/1:
     * ● 未设置此参数时，正常返回处理结果。
     * ● 设置了此参数时，若图像处理的结果失败，则返回原图。
     * ● 设置了此参数时，若图像处理的结果成功，则正常返回处理结果
     * sharpen:图片是否锐化，当设置值为1时打开锐化效果
     */

    /**
     * 1.最后在格式拼接时根据width和height加上size-limit/[xxk]!限制
     */

    /**
     * 图片瘦身
     */
    None("?imageslim"),
    /**
     * 按宽度进行缩放
     * (七牛圆角参数和常规格式不能同时使用,因此这一属性最后本地处理;)
     */
    GeometricForWidth("?imageMogr2/auto-orient/thumbnail/{0}x/format/webp/q/75/sharpen/1/ignore-error/1"),
    /**
     * 裁剪(矩形)
     * (七牛圆角参数和常规格式不能同时使用,因此这一属性最后本地处理;)
     */
    TailoringForWH("?imageMogr2/auto-orient/gravity/center/crop/{0}x{1}/format/webp/q/75/sharpen/1/ignore-error/1"),
    /**
     * 高斯模糊(宽度等比缩放-模糊半径)
     * /blur/<radius>x<sigma>
     * radius是模糊半径，取值范围为1-50。sigma是正态分布的标准差，必须大于0。图片格式为gif时，不支持该参数。
     */
    GaussianBlurForWR("?imageMogr2/auto-orient/thumbnail/{0}x/format/jpg/blur/{1}x{2}/q/75/sharpen/1/ignore-error/1");

    private String value = "";

    private ImgRuleType(String value) {
        this.value = value;
    }

    public String getRule() {
        return this.value;
    }

    /**
     * 规则是否有效
     *
     * @param value 规则
     * @return true-预定义规则;false-自定义规则;
     */
    public static final boolean isEffective(String value) {
        boolean flag = false;
        for (ImgRuleType e : ImgRuleType.values()) {
            if (TextUtils.equals(e.getRule(), value)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
