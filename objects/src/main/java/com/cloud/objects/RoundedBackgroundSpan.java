package com.cloud.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/26
 * Description:Spannable标签
 * Modifier:
 * ModifyContent:
 */
public class RoundedBackgroundSpan extends ReplacementSpan {
    private static int CORNER_RADIUS = 8;
    private int backgroundColor = 0;
    private int textColor = 0;

    public RoundedBackgroundSpan(int backgroundColor, int textColor) {
        super();
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        RectF rect = new RectF(x, top, x + measureText(paint, text, start, end), fm.bottom - fm.top);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}
