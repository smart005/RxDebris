package com.cloud.images.figureset;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/29
 * @Description:已拖拽图片监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnDrageredImageListener {

    /**
     * 已拖拽图片回调
     *
     * @param drageredSelectImage 已拖拽图片
     * @param oldPosition         拖拽前图片索引
     * @param position            拖拽后图片索引
     */
    public void onDrageredImage(SelectImageItem drageredSelectImage, int oldPosition, int position);
}
