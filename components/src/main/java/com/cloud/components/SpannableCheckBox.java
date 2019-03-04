package com.cloud.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;

import com.cloud.components.beans.SpannableTagItem;
import com.cloud.components.events.OnSpannableTextClickListener;
import com.cloud.components.utils.BaseSpannableText;
import com.cloud.objects.utils.PixelUtils;
import com.cloud.objects.utils.SpannableUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/23
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class SpannableCheckBox extends CheckBox {

    private BaseSpannableText spannableText = new BaseSpannableText();
    private boolean isEnableUnderline = false;
    private OnSpannableTextClickListener onSpannableTextClickListener = null;
    private CharSequence tempText = null;
    private int chbButtonDrawable = 0;
    //扩展数据
    private Object extras = null;

    public Object getExtras() {
        return extras;
    }

    public void setExtras(Object extras) {
        this.extras = extras;
    }

    public SpannableCheckBox(Context context) {
        super(context);
    }

    public SpannableCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        spannableText.setOnClickableSpanListener(new SpannableUtils.OnClickableSpanListener() {
            @Override
            public void onSpannableClick(Object extras) {
                if (onSpannableTextClickListener == null) {
                    return;
                }
                onSpannableTextClickListener.onSpannableTextClick(SpannableCheckBox.this, extras);
                SpannableStringBuilder spannable = spannableText.getSpannable(getContext(), null, getText());
                setText(spannable);
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpannableCheckBox);
        isEnableUnderline = a.getBoolean(R.styleable.SpannableCheckBox_scb_isEnableUnderline, false);
        chbButtonDrawable = a.getResourceId(R.styleable.SpannableCheckBox_scb_chbButtonDrawable, R.drawable.cl_check_box_bg);
        a.recycle();
        spannableText.setEnableUnderline(isEnableUnderline);
        SpannableStringBuilder spannable = spannableText.getSpannable(context, attrs, getText());
        this.setText(spannable);
        this.setHighlightColor(Color.parseColor("#10000000"));
        this.setMovementMethod(LinkMovementMethod.getInstance());
        this.setButtonDrawable(chbButtonDrawable);
        //根据选择框显示在左边还是右边来设置文本与选择框之间的间距
        int[] androidAttrs = new int[]{
                android.R.attr.layoutDirection
        };
        int ldirection = 0;
        TypedArray a1 = context.obtainStyledAttributes(attrs, androidAttrs);
        ldirection = a1.getInt(0, LAYOUT_DIRECTION_LTR);
        a1.recycle();
        if (ldirection == LAYOUT_DIRECTION_LTR) {
            this.setPadding(PixelUtils.dip2px(context, 3), 0, 0, 0);
        } else {
            this.setPadding(0, 0, PixelUtils.dip2px(context, 3), 0);
        }

        this.setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 是否启用下划线
     *
     * @param enableUnderline true-显示下划线;false-不显示;
     */
    public void setEnableUnderline(boolean enableUnderline) {
        isEnableUnderline = enableUnderline;
    }

    /**
     * 设置SpannableText点击监听
     *
     * @param listener SpannableText点击监听
     */
    public void setOnSpannableTextClickListener(OnSpannableTextClickListener listener) {
        this.onSpannableTextClickListener = listener;
    }

    /**
     * 设置文本内容
     *
     * @param text 显示文本
     */
    public void setSpannableText(CharSequence text, SpannableTagItem... tagItems) {
        this.tempText = text;
        spannableText.setOnClickableSpanListener(new SpannableUtils.OnClickableSpanListener() {
            @Override
            public void onSpannableClick(Object extras) {
                if (onSpannableTextClickListener == null) {
                    return;
                }
                onSpannableTextClickListener.onSpannableTextClick(SpannableCheckBox.this, extras);
                SpannableStringBuilder spannable = spannableText.getSpannable(getContext(), null, tempText);
                setText(spannable);
            }
        });
        spannableText.setEnableUnderline(isEnableUnderline);
        SpannableStringBuilder spannable = spannableText.getSpannable(getContext(), null, text, tagItems);
        this.setText(spannable);
        this.setHighlightColor(Color.parseColor("#10000000"));
        this.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void setButtonDrawable(int resId) {
        super.setButtonDrawable(resId == 0 ? chbButtonDrawable : resId);
    }

    /**
     * 设置check box图标相对于文本位置
     *
     * @param layoutDirection View.LAYOUT_DIRECTION_LTR-位于文本左边;View.LAYOUT_DIRECTION_RTL-位于文本右边;
     */
    @Override
    public void setLayoutDirection(int layoutDirection) {
        super.setLayoutDirection(layoutDirection);
        if (layoutDirection == LAYOUT_DIRECTION_LTR) {
            this.setPadding(PixelUtils.dip2px(getContext(), 3), 0, 0, 0);
        } else {
            this.setPadding(0, 0, PixelUtils.dip2px(getContext(), 3), 0);
        }
    }
}
