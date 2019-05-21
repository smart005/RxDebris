package com.cloud.dialogs.toasty;

import android.view.View;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-05-17
 * Description:配合CustomToast使用
 * Modifier:
 * ModifyContent:
 */
public interface IToast {
    public void makeTextShow(String text, long duration);

    public IToast setGravity(int gravity, int xOffset, int yOffset);

    public IToast setDuration(long durationMillis);

    /**
     * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
     */
    public IToast setView(View view);

    public IToast setMargin(float horizontalMargin, float verticalMargin);

    /**
     * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setText(String)}
     */
    public IToast setText(String text);

    public void show();

    public void cancel();
}
