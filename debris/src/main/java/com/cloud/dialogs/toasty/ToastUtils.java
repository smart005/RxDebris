package com.cloud.dialogs.toasty;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.cloud.debris.R;
import com.cloud.dialogs.enums.TextAlign;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.RunnableParamsN;
import com.cloud.objects.handler.HandlerManager;
import com.cloud.objects.utils.ResUtils;

public class ToastUtils extends BaseToast {

    private static void showToast(Context context,
                                  CharSequence message,
                                  Drawable icon,
                                  @ColorInt int tintColor,
                                  @ColorInt int textColor,
                                  int duration,
                                  boolean withIcon,
                                  boolean shouldTint,
                                  TextAlign align) {
        if (ObjectJudge.isMainThread()) {
            Object custom = custom(context, message, icon, tintColor, textColor, duration, withIcon, shouldTint, align);
            if (custom instanceof CustomToast) {
                CustomToast customToast = (CustomToast) custom;
                customToast.show();
            } else {
                Toast toast = (Toast) custom;
                toast.show();
            }
        } else {
            HandlerManager manager = HandlerManager.getInstance();
            manager.post(new ToastRunnable(context, message, icon, tintColor, textColor, duration, withIcon, shouldTint, align));
        }
    }

    private static class ToastRunnable extends RunnableParamsN {

        private Context context;
        private CharSequence message;
        private Drawable icon;
        private @ColorInt
        int tintColor;
        private @ColorInt
        int textColor;
        private int duration;
        private boolean withIcon;
        private boolean shouldTint;
        private TextAlign align;

        public ToastRunnable(Context context, CharSequence message, Drawable icon, @ColorInt int tintColor, @ColorInt int textColor, int duration, boolean withIcon, boolean shouldTint, TextAlign align) {
            this.context = context;
            this.message = message;
            this.icon = icon;
            this.tintColor = tintColor;
            this.textColor = textColor;
            this.duration = duration;
            this.withIcon = withIcon;
            this.shouldTint = shouldTint;
            this.align = align;
        }

        @Override
        public void run(Object[] objects) {
            Object custom = custom(context, message, icon, tintColor, textColor, duration, withIcon, shouldTint, align);
            if (custom instanceof CustomToast) {
                CustomToast customToast = (CustomToast) custom;
                customToast.show();
            } else {
                Toast toast = (Toast) custom;
                toast.show();
            }
        }
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param icon    消息图标
     * @param align   对齐方式
     */
    public static synchronized void show(Context context, CharSequence message, @DrawableRes int icon, TextAlign align) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_353a3e);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        Drawable drawable = null;
        if (icon != 0) {
            drawable = ResUtils.getDrawable(context, icon);
        }
        showToast(context, message, drawable, color, textColor, Toast.LENGTH_LONG, false, true, align);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param icon    消息图标
     */
    public static synchronized void show(Context context, CharSequence message, @DrawableRes int icon) {
        show(context, message, icon, TextAlign.LEFT);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static void show(Context context, CharSequence message, TextAlign align) {
        show(context, message, 0, align);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void show(Context context, CharSequence message) {
        show(context, message, TextAlign.LEFT);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param icon    消息图标
     * @param align   对齐方式
     */
    public static void show(Context context, @StringRes int message, @DrawableRes int icon, TextAlign align) {
        String msg = context.getString(message);
        show(context, msg, icon, align);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param icon    消息图标
     */
    public static void show(Context context, @StringRes int message, @DrawableRes int icon) {
        show(context, message, icon, TextAlign.LEFT);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static void show(Context context, @StringRes int message, TextAlign align) {
        show(context, message, 0, align);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void show(Context context, @StringRes int message) {
        show(context, message, TextAlign.LEFT);
    }

    /**
     * 显示错误消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static synchronized void showError(Context context, CharSequence message, TextAlign align) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_d50000);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        //图标
        Drawable icon = ResUtils.getDrawable(context, R.drawable.cl_toast_error_white_24dp);
        showToast(context, message, icon, color, textColor, Toast.LENGTH_LONG, true, true, align);
    }

    /**
     * 显示错误消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static void showError(Context context, @StringRes int message, TextAlign align) {
        String msg = context.getString(message);
        showError(context, msg, align);
    }

    /**
     * 显示错误消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showError(Context context, @StringRes int message) {
        showError(context, message, TextAlign.LEFT);
    }

    /**
     * 显示success消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static void showSuccess(Context context, CharSequence message, TextAlign align) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_388e3c);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        //图标
        Drawable icon = ResUtils.getDrawable(context, R.drawable.cl_toast_success_white_24dp);
        showToast(context, message, icon, color, textColor, Toast.LENGTH_LONG, true, true, align);
    }

    /**
     * 显示success消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static void showSuccess(Context context, int message, TextAlign align) {
        String msg = context.getString(message);
        showSuccess(context, msg, align);
    }

    /**
     * 显示success消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showSuccess(Context context, int message) {
        showSuccess(context, message, TextAlign.LEFT);
    }

    /**
     * 显示warning消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static void showWarning(Context context, CharSequence message, TextAlign align) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_ffa900);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        //图标
        Drawable icon = ResUtils.getDrawable(context, R.drawable.cl_toast_outline_white_24dp);
        showToast(context, message, icon, color, textColor, Toast.LENGTH_LONG, true, true, align);
    }

    /**
     * 显示warning消息
     *
     * @param context 上下文
     * @param message 消息
     * @param align   对齐方式
     */
    public static void showWarning(Context context, @StringRes int message, TextAlign align) {
        String msg = context.getString(message);
        showWarning(context, msg, align);
    }

    /**
     * 显示warning消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showWarning(Context context, @StringRes int message) {
        showWarning(context, message, TextAlign.LEFT);
    }
}
