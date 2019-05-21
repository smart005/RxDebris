package com.cloud.dialogs.toasty;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.debris.R;
import com.cloud.dialogs.enums.TextAlign;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.utils.PixelUtils;
import com.cloud.objects.utils.ResUtils;

import java.util.LinkedHashSet;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019-04-29
 * Description:toast基础类
 * Modifier:
 * ModifyContent:
 */
class BaseToast {

    private static Toast toast = null;
    private static LinkedHashSet<CustomToast> toasts = new LinkedHashSet<>();

    static class ToastViewHolder {

        private View toastView;
        public ImageView toastIcon;
        public TextView toastTextView;

        public ToastViewHolder(Context context) {
            toastView = View.inflate(context, R.layout.cl_toast_view, null);
            toastIcon = (ImageView) toastView.findViewById(R.id.toast_icon);
            toastTextView = (TextView) toastView.findViewById(R.id.toast_text);
        }

        public View getView() {
            return toastView;
        }
    }

    static Drawable tintIcon(Drawable drawable, @ColorInt int tintColor) {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    static Drawable tint9PatchDrawableFrame(Context context, @ColorInt int tintColor) {
        final NinePatchDrawable toastDrawable = (NinePatchDrawable) ResUtils.getDrawable(context, R.drawable.toast_frame);
        return tintIcon(toastDrawable, tintColor);
    }

    static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    @CheckResult
    protected static synchronized <T> T custom(Context context,
                                               CharSequence message,
                                               Drawable icon,
                                               @ColorInt int tintColor,
                                               @ColorInt int textColor,
                                               int duration,
                                               boolean withIcon,
                                               boolean shouldTint,
                                               TextAlign align) {
        //如果系统NotificationManagerService被关闭则调用CostomToast
        boolean isEnableNotice = false;
        CustomToast customToast = null;
        if (ObjectJudge.isNotificationEnabled(context)) {
            toasts.clear();
            isEnableNotice = true;
            if (toast == null) {
                toast = Toast.makeText(context, message, duration);
            } else {
                toast.setDuration(duration);
            }
        } else {
            toast = null;
            isEnableNotice = false;
            //清除队列中toast
            for (CustomToast ts : toasts) {
                ts.cancel();
            }
            toasts.clear();
            //创建当前toast
            customToast = new CustomToast(context);
            if (duration == Toast.LENGTH_LONG) {
                customToast.setDuration(1500);
            } else {
                customToast.setDuration(1000);
            }
            toasts.add(customToast);
        }

        ToastViewHolder holder = new ToastViewHolder(context);
        Drawable drawableFrame;
        if (shouldTint) {
            drawableFrame = tint9PatchDrawableFrame(context, tintColor);
        } else {
            drawableFrame = ResUtils.getDrawable(context, R.drawable.toast_frame);
        }
        View view = holder.getView();
        //设置背景
        setBackground(view, drawableFrame);
        //设置图标
        if (withIcon && icon != null) {
            holder.toastIcon.setVisibility(View.VISIBLE);
            boolean tintIcon = ToastConfig.getInstance().isTintIcon();
            setBackground(holder.toastIcon, tintIcon ? tintIcon(icon, textColor) : icon);
        } else {
            holder.toastIcon.setVisibility(View.GONE);
        }
        //设置文本视图
        holder.toastTextView.setGravity(getTextGravity(align));
        holder.toastTextView.setText(message);
        holder.toastTextView.setTextColor(textColor);
        holder.toastTextView.setTypeface(ToastConfig.getInstance().getTypeface());
        holder.toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ToastConfig.getInstance().getTextSize());
        if (isEnableNotice) {
            toast.setView(view);
            return (T) toast;
        } else {
            customToast.setView(view);
            customToast.setGravity(Gravity.BOTTOM, 0, PixelUtils.dip2px(context, 76));
            return (T) customToast;
        }
    }

    private static int getTextGravity(TextAlign align) {
        if (align == TextAlign.TOP) {
            return Gravity.TOP;
        } else if (align == TextAlign.RIGHT) {
            return Gravity.RIGHT;
        } else if (align == TextAlign.BOTTOM) {
            return Gravity.BOTTOM;
        } else if (align == TextAlign.CENTER) {
            return Gravity.CENTER;
        } else {
            return Gravity.LEFT;
        }
    }
}
