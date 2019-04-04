package com.cloud.images.enums;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/9
 * Description:图片填充类型
 * Modifier:
 * ModifyContent:
 */
public enum ScaleType {
//    /**
//     * 保持原图的大小,当原图的size大于ImageView的size时，多出来的部分被截掉;
//     */
//    center,
    /**
     * 以原图正常显示
     * 如果原图大小大于ImageView的size,就按照比例缩小原图的宽高,居中显示在ImageView中
     * 如果原图size小于ImageView的size,则不做处理居中显示图片
     */
    centerInside,
    /**
     * 以原图填满ImageView
     * 如果原图size大于ImageView的size,则与center_inside一样,按比例缩小居中显示在ImageView上
     * 如果原图size小于ImageView的size,则按比例拉升原图的宽和高,填充ImageView居中显示
     */
    centerCrop,
    /**
     * 把原图按照比例放大缩小到ImageView的高度,显示在ImageView的center(中部/居中显示)
     */
    fitCenter,
//    /**
//     * 把图片按照指定的大小在ImageView中显示,拉伸显示图片不保持原比例填满ImageView
//     */
//    fitXY,
//    /**
//     * 不改变原图的大小,从ImageView的左上角开始绘制超出部分做剪切处理
//     */
//    matrix
}
