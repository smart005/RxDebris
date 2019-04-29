package com.cloud.dialogs.toasty;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.debris.R;
import com.cloud.objects.utils.ResUtils;

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
    protected static Toast custom(Context context,
                                  CharSequence message,
                                  Drawable icon,
                                  @ColorInt int tintColor,
                                  @ColorInt int textColor,
                                  int duration,
                                  boolean withIcon,
                                  boolean shouldTint) {
        if (toast == null) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setDuration(duration);
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
            boolean tintIcon = ToastConfig.getInstance().isTintIcon();
            setBackground(holder.toastIcon, tintIcon ? tintIcon(icon, textColor) : icon);
        } else {
            holder.toastIcon.setVisibility(View.GONE);
        }
        //设置文本视图
        holder.toastTextView.setText(message);
        holder.toastTextView.setTextColor(textColor);
        holder.toastTextView.setTypeface(ToastConfig.getInstance().getTypeface());
        holder.toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ToastConfig.getInstance().getTextSize());
        toast.setView(view);
        return toast;
    }
}
