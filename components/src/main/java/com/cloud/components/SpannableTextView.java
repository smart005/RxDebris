package com.cloud.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cloud.components.events.OnSpannableTextClickListener;
import com.cloud.components.utils.BaseSpannableText;
import com.cloud.objects.utils.SpannableUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/22
 * Description:可解析<font size="12" color="#ff0000">文本</font>内容
 * Modifier:
 * ModifyContent:
 */
public class SpannableTextView extends TextView {

    private OnSpannableTextClickListener onSpannableTextClickListener = null;
    private BaseSpannableText spannableText = new BaseSpannableText();
    private CharSequence tempText = null;
    private boolean isEnableUnderline = false;

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        spannableText.setOnClickableSpanListener(new SpannableUtils.OnClickableSpanListener() {
            @Override
            public void onSpannableClick(Object extras) {
                if (onSpannableTextClickListener == null) {
                    return;
                }
                onSpannableTextClickListener.onSpannableTextClick(SpannableTextView.this, extras);
                SpannableStringBuilder spannable = spannableText.getSpannable(getContext(), null, getText());
                setText(spannable);
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpannableTextView);
        isEnableUnderline = a.getBoolean(R.styleable.SpannableTextView_stv_isEnableUnderline, false);
        a.recycle();
        spannableText.setEnableUnderline(isEnableUnderline);
        SpannableStringBuilder spannable = spannableText.getSpannable(context, attrs, getText());
        this.setText(spannable);
        this.setHighlightColor(Color.parseColor("#10000000"));
        this.setMovementMethod(LinkMovementMethod.getInstance());
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
    public void setSpannableText(CharSequence text) {
        this.tempText = text;
        spannableText.setOnClickableSpanListener(new SpannableUtils.OnClickableSpanListener() {
            @Override
            public void onSpannableClick(Object extras) {
                if (onSpannableTextClickListener == null) {
                    return;
                }
                onSpannableTextClickListener.onSpannableTextClick(SpannableTextView.this, extras);
                SpannableStringBuilder spannable = spannableText.getSpannable(getContext(), null, tempText);
                setText(spannable);
            }
        });
        spannableText.setEnableUnderline(isEnableUnderline);
        SpannableStringBuilder spannable = spannableText.getSpannable(getContext(), null, text);
        this.setText(spannable);
        this.setHighlightColor(Color.parseColor("#10000000"));
        this.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 是否启用下划线
     *
     * @param enableUnderline true-显示下划线;false-不显示;
     */
    public void setEnableUnderline(boolean enableUnderline) {
        isEnableUnderline = enableUnderline;
    }
}
