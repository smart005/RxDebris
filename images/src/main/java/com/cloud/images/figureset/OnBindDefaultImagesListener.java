package com.cloud.images.figureset;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/9/4
 * @Description:绑定默认图片回调
 * @Modifier:
 * @ModifyContent:
 */
interface OnBindDefaultImagesListener {

    /**
     * 绑定默认图片
     *
     * @param position 图片索引
     * @param fileName 文件名
     * @param filePath 文件路径
     */
    public void onBindDefaultImages(int position, String fileName, String filePath);
}
