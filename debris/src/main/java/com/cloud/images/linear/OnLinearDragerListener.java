package com.cloud.images.linear;

import android.view.View;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/8/29
 * @Description:拖拽监听
 * @Modifier:
 * @ModifyContent:
 */
public interface OnLinearDragerListener {
    /**
     * 线性视图拖拽回调
     *
     * @param view     当前被拖拽的视图
     * @param position 当前被拖拽视图的索引
     */
    public void onLinearDrager(View view, int position);
}
