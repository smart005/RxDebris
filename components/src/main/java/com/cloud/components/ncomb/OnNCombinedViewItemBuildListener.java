package com.cloud.components.ncomb;

import android.content.Context;
import android.widget.ImageView;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/28
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public interface OnNCombinedViewItemBuildListener {

    /**
     * 视图构建
     *
     * @param context   上下文
     * @param imageView 图片视图
     * @param url       图片url
     * @param width     宽度
     * @param height    高度
     * @param extras    扩展数据
     */
    public void onNCombinedViewItemBuild(Context context, ImageView imageView, String url, int width, int height, Object extras);
}
