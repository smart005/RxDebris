package com.cloud.dialogs.toasty;

import android.graphics.Typeface;
import android.support.annotation.CheckResult;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-04-29
 * Description:toast配置类
 * Modifier:
 * ModifyContent:
 */
public class ToastConfig {
    private Typeface typeface;
    private int textSize = 13;
    private boolean tintIcon = true;
    private boolean allowQueue = true;
    private static ToastConfig toastConfig;

    private ToastConfig() {
        // avoiding instantiation
    }

    @CheckResult
    public static ToastConfig getInstance() {
        if (toastConfig == null) {
            synchronized (ToastConfig.class) {
                if (toastConfig == null) {
                    toastConfig = new ToastConfig();
                }
            }
        }
        return toastConfig;
    }

    /**
     * 重置
     */
    public void reset() {
        typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
        textSize = 13;
        tintIcon = true;
        allowQueue = true;
    }

    @CheckResult
    public ToastConfig setToastTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    @CheckResult
    public ToastConfig setTextSize(int sizeInSp) {
        this.textSize = sizeInSp;
        return this;
    }

    public int getTextSize() {
        return this.textSize;
    }

    @CheckResult
    public ToastConfig tintIcon(boolean tintIcon) {
        this.tintIcon = tintIcon;
        return this;
    }

    public boolean isTintIcon() {
        return this.tintIcon;
    }

    @CheckResult
    public ToastConfig allowQueue(boolean allowQueue) {
        this.allowQueue = allowQueue;
        return this;
    }
}
