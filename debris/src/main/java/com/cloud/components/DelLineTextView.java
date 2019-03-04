package com.cloud.components;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/5/28
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class DelLineTextView extends TextView {
    public DelLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextPaint paint = this.getPaint();
        paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        paint.setAntiAlias(true);
    }
}
