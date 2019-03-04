package com.cloud.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloud.components.shape.shapes.RoundRectView;
import com.cloud.objects.events.HookEvent;
import com.cloud.objects.utils.PixelUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/23
 * Description:可变按钮(背景、圆角、文本颜色)
 * Modifier:
 * ModifyContent:
 */
public class VariableButton extends RoundRectView {

    private int borderColor = 0;
    private int borderWidth = 0;
    private int bottomLeftRadius = 0;
    private int bottomRightRadius = 0;
    private int topLeftRadius = 0;
    private int topRightRadius = 0;
    private int backgroundColor = 0;
    private int pressBackgroundColor = 0;
    private String text = "";
    private int textColor = 0;
    private int textSize = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int textGravity = 0;
    private int background = 0;
    private int disableBackgroundColor = 0;
    private boolean isEnabled = true;
    private TextView textView = null;

    public VariableButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        int plr = PixelUtils.dip2px(context, 12);
        int ptb = PixelUtils.dip2px(context, 8);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VariableButton);
        borderColor = typedArray.getColor(R.styleable.VariableButton_vb_borderColor, 0);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_borderWidth, 0);
        bottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_bottomLeftRadius, 0);
        bottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_bottomRightRadius, 0);
        topLeftRadius = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_topLeftRadius, 0);
        topRightRadius = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_topRightRadius, 0);
        backgroundColor = typedArray.getColor(R.styleable.VariableButton_vb_backgroundColor, 0);
        pressBackgroundColor = typedArray.getColor(R.styleable.VariableButton_vb_pressBackgroundColor, 0);
        text = typedArray.getString(R.styleable.VariableButton_vb_text);
        textColor = typedArray.getColor(R.styleable.VariableButton_vb_textColor, 0);
        textSize = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_textSize, 0);
        paddingLeft = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_paddingLeft, plr);
        paddingRight = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_paddingRight, plr);
        paddingTop = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_paddingTop, ptb);
        paddingBottom = typedArray.getDimensionPixelSize(R.styleable.VariableButton_vb_paddingBottom, ptb);
        textGravity = typedArray.getInt(R.styleable.VariableButton_vb_textGravity, 0);
        background = typedArray.getResourceId(R.styleable.VariableButton_vb_background, 0);
        disableBackgroundColor = typedArray.getColor(R.styleable.VariableButton_vb_disableBackgroundColor, 0);
        isEnabled = typedArray.getBoolean(R.styleable.VariableButton_vb_isEnabled, true);
        typedArray.recycle();

        init();
    }

    private void init() {
        super.setBorderColor(borderColor);
        super.setBorderWidthPx(borderWidth);
        super.setBottomLeftRadius(bottomLeftRadius);
        super.setBottomRightRadius(bottomRightRadius);
        super.setTopLeftRadius(topLeftRadius);
        super.setTopRightRadius(topRightRadius);
        //解决与TextThemeView属性app:ttv_titleGravity="left"冲突问题
        this.setVerticalScrollBarEnabled(true);
        this.setScrollbarFadingEnabled(true);

        FrameLayout.LayoutParams vparam = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        textView = new TextView(getContext());
        if (isEnabled) {
            textView.setBackgroundColor(backgroundColor);
        } else {
            textView.setBackgroundColor(disableBackgroundColor);
        }
        textView.setText(text);
        textView.setTextColor(textColor == 0 ? Color.parseColor("#041d29") : textColor);
        if (textSize == 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setSingleLine(true);
        switch (textGravity) {
            case 1:
                textView.setGravity(Gravity.LEFT);
                break;
            case 2:
                textView.setGravity(Gravity.TOP);
                break;
            case 3:
                textView.setGravity(Gravity.RIGHT);
                break;
            case 4:
                textView.setGravity(Gravity.BOTTOM);
                break;
            default:
                textView.setGravity(Gravity.CENTER);
                break;
        }
        if (background != 0) {
            textView.setBackgroundResource(background);
        }
        this.addView(textView, vparam);
        hookEvent.didHook(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (isEnabled && pressBackgroundColor != 0 && background == 0) {
                    textView.setBackgroundColor(pressBackgroundColor);
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isEnabled && backgroundColor != 0 && background == 0) {
                    textView.setBackgroundColor(backgroundColor);
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    private HookEvent hookEvent = new HookEvent() {
        @Override
        protected void onPreClick(View v) {

        }

        @Override
        protected void onAfterClick(View v) {

        }
    };
}
