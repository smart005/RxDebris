package com.cloud.components.events;

import android.view.View;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/17
 * Description:SpannableText点击监听
 * Modifier:
 * ModifyContent:
 */
public interface OnSpannableTextClickListener<T> {
    /**
     * SpannableText点击监听
     *
     * @param view   当前视图
     * @param extras 扩展数据
     */
    public void onSpannableTextClick(View view, T extras);
}
