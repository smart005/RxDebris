package com.cloud.objects.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

    private static Toast toast = null;
    private static Toast cusToast = null;

    private static Toast buildToast(Context context, CharSequence message, int duration, int yOffset) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, yOffset);
        return toast;
    }

    /**
     * 短时间显示Toast
     * <p>
     * param context
     * param message
     * param yOffset
     */
    public static void showShort(Context context, CharSequence message,
                                 int yOffset) {
        if (toast == null) {
            toast = buildToast(context, message, Toast.LENGTH_SHORT, yOffset);
        } else {
            if (toast.getDuration() == Toast.LENGTH_SHORT) {
                toast.setView(toast.getView());
                toast.setText(message);
                toast.setDuration(Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, yOffset);
            }
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     * <p>
     * param context
     * param message
     */
    public static void showShort(Context context, CharSequence message) {
        showShort(context, message, -16);
    }

    /**
     * 短时间显示Toast
     * <p>
     * param context
     * param resId
     * param yOffset
     */
    public static void showShort(Context context, int resId, int yOffset) {
        CharSequence message = context.getResources().getText(resId);
        showShort(context, message, yOffset);
    }

    /**
     * 短时间显示Toast
     * <p>
     * param context
     * param resId
     */
    public static void showShort(Context context, int resId) {
        showShort(context, resId, -16);
    }

    /**
     * 长时间显示Toast
     * <p>
     * param context
     * param message
     * param yOffset
     */
    public static void showLong(Context context, CharSequence message, int yOffset) {
        if (toast == null) {
            toast = buildToast(context, message, Toast.LENGTH_LONG, yOffset);
        } else {
            if (toast.getDuration() == Toast.LENGTH_LONG) {
                toast.setView(toast.getView());
                toast.setText(message);
                toast.setDuration(Toast.LENGTH_LONG);
            } else {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, yOffset);
            }
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     * <p>
     * param context
     * param message
     */
    public static void showLong(Context context, CharSequence message) {
        showLong(context, message, -16);
    }

    /**
     * 长时间显示Toast
     * <p>
     * param context
     * param resId
     * param yOffset
     */
    public static void showLong(Context context, int resId, int yOffset) {
        CharSequence message = context.getResources().getText(resId);
        showLong(context, message, yOffset);
    }

    /**
     * 长时间显示Toast
     * <p>
     * param context
     * param resId
     */
    public static void showLong(Context context, int resId) {
        showLong(context, resId, -16);
    }

    public static void hide() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
