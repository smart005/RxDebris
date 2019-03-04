package com.cloud.dialogs.toasty;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

    private static Toast toast = null;
    private static Toast cusToast = null;

    private static Toast buildToast(Context context, CharSequence message, int duration, int yOffset) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, yOffset);
//        ToastThemeProperties properties = BaseCConfig.getInstance().getToastThemeProperties(context);
//        if (properties != null) {
//            View view = toast.getView();
//            ConfigItem bg = properties.getBackgroundResource();
//            if (!TextUtils.isEmpty(bg.getType()) && !TextUtils.isEmpty(bg.getName())) {
//                ResFolderType folderType = ResFolderType.getResFolderType(bg.getType());
//                int bgRresId = ResUtils.getResource(context, bg.getName(), folderType);
//                if (bgRresId != -1 && bgRresId != 0) {
//                    //设置背景
//                    view.setBackgroundResource(bgRresId);
//                }
//            }
//            //文本颜色、大小
//            if (!TextUtils.isEmpty(properties.getTextColor())) {
//                TextView tv = (TextView) view.findViewById(android.R.id.message);
//                if (tv == null) {
//                    ViewGroup group = (ViewGroup) view;
//                    if (group != null) {
//                        tv = (TextView) group.getChildAt(0);
//                    }
//                }
//                if (tv != null) {
//                    tv.setTextColor(Color.parseColor(properties.getTextColor()));
//                    if (properties.getTextSize() > 0) {
//                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, properties.getTextSize());
//                    }
//                    tv.setText(message);
//                }
//            }
//        }
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
