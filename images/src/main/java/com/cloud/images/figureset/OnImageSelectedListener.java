package com.cloud.images.figureset;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/27
 * @Description:图片已选择监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnImageSelectedListener {

    /**
     * 选择图片回调
     *
     * @param selectImageItem  当前选择的图片
     * @param selectImageItems 已选择图片集合
     */
    public void onImageSelected(SelectImageItem selectImageItem, List<SelectImageItem> selectImageItems);
}
