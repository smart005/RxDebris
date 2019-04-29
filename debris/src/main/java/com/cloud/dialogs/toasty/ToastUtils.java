package com.cloud.dialogs.toasty;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.cloud.debris.R;
import com.cloud.objects.utils.ResUtils;

public class ToastUtils extends BaseToast {

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param icon    消息图标
     */
    public static void show(Context context, CharSequence message, @DrawableRes int icon) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_353a3e);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        Drawable drawable = null;
        if (icon != 0) {
            drawable = ResUtils.getDrawable(context, icon);
        }
        Toast toast = custom(context, message, drawable, color, textColor, Toast.LENGTH_SHORT, false, true);
        toast.show();
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void show(Context context, CharSequence message) {
        show(context, message, 0);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     * @param icon    消息图标
     */
    public static void show(Context context, @StringRes int message, @DrawableRes int icon) {
        String msg = context.getString(message);
        show(context, msg, icon);
    }

    /**
     * Toast消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void show(Context context, @StringRes int message) {
        show(context, message, 0);
    }

    /**
     * 显示错误消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showError(Context context, CharSequence message) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_d50000);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        //图标
        Drawable icon = ResUtils.getDrawable(context, R.drawable.cl_toast_error_white_24dp);
        Toast toast = custom(context, message, icon, color, textColor, Toast.LENGTH_SHORT, true, true);
        toast.show();
    }

    /**
     * 显示错误消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showError(Context context, @StringRes int message) {
        String msg = context.getString(message);
        showError(context, msg);
    }

    /**
     * 显示success消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showSuccess(Context context, CharSequence message) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_388e3c);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        //图标
        Drawable icon = ResUtils.getDrawable(context, R.drawable.cl_toast_success_white_24dp);
        Toast toast = custom(context, message, icon, color, textColor, Toast.LENGTH_SHORT, true, true);
        toast.show();
    }

    /**
     * 显示success消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showSuccess(Context context, int message) {
        String msg = context.getString(message);
        showSuccess(context, msg);
    }

    /**
     * 显示warning消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showWarning(Context context, CharSequence message) {
        //背景色
        int color = ContextCompat.getColor(context, R.color.color_ffa900);
        //文本颜色
        int textColor = ContextCompat.getColor(context, R.color.color_ffffff);
        //图标
        Drawable icon = ResUtils.getDrawable(context, R.drawable.cl_toast_outline_white_24dp);
        Toast toast = custom(context, message, icon, color, textColor, Toast.LENGTH_SHORT, true, true);
        toast.show();
    }

    /**
     * 显示warning消息
     *
     * @param context 上下文
     * @param message 消息
     */
    public static void showWarning(Context context, @StringRes int message) {
        String msg = context.getString(message);
        showWarning(context, msg);
    }
}
