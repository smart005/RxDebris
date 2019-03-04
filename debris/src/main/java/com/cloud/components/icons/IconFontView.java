package com.cloud.components.icons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.components.enums.FontType;
import com.cloud.debris.R;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/3
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class IconFontView extends RelativeLayout {

    private String iconUcode = "";
    private int iconColor = 0;
    private float iconSize = 0;
    private int textColor = 0;
    private float textSize = 0;
    private int textDirection = 0;
    private int textFontSpacing = 0;
    private String text = "";
    //文本字体样式
    private int textStyle = 0;
    //文本对齐
    private int textGravity = 0;
    //图标对齐
    private int iconGravity = 0;

    private enum TextLocation {
        left,
        top,
        right,
        bottom
    }

    private enum TextStyle {
        none,
        agencyb
    }

    private enum TextGravity {
        lef,
        top,
        right,
        bottom,
        center_vertical,
        center_horizontal
    }

    private enum IconGravity {
        lef,
        top,
        right,
        bottom,
        center_vertical,
        center_horizontal
    }

    private interface ViewIds {
        public int iconViewId = 379125939;
        public int textViewId = 1814552795;
    }

    public IconFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        float defSize = sp2px(12);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IconFontView);
        iconUcode = a.getString(R.styleable.IconFontView_iftv_ucode);
        iconColor = a.getColor(R.styleable.IconFontView_iftv_iconColor, 0);
        iconSize = a.getDimension(R.styleable.IconFontView_iftv_iconSize, defSize);
        textColor = a.getColor(R.styleable.IconFontView_iftv_textColor, 0);
        textSize = a.getDimension(R.styleable.IconFontView_iftv_textSize, defSize);
        textDirection = a.getInt(R.styleable.IconFontView_iftv_textDirection, TextLocation.right.ordinal());
        textFontSpacing = (int) a.getDimension(R.styleable.IconFontView_iftv_textFontSpacing, 0);
        text = a.getString(R.styleable.IconFontView_iftv_text);
        textStyle = a.getInt(R.styleable.IconFontView_iftv_textStyle, TextStyle.none.ordinal());
        iconGravity = a.getInt(R.styleable.IconFontView_iftv_iconGravity, TextGravity.center_vertical.ordinal());
        textGravity = a.getInt(R.styleable.IconFontView_iftv_textGravity, TextGravity.center_vertical.ordinal());
        a.recycle();
        this.setGravity(Gravity.CENTER);
        buildView();
    }

    private void buildLeftView() {
        IconView textView = getText();
        LayoutParams tvparams = (LayoutParams) textView.getLayoutParams();
        tvparams.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(textView);

        IconView iconTextView = getIconTextView();
        LayoutParams params = (LayoutParams) iconTextView.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.RIGHT_OF, ViewIds.textViewId);
        params.setMargins(textFontSpacing, 0, 0, 0);
        this.addView(iconTextView);
    }

    private void buildView() {
        if (TextLocation.left.ordinal() == textDirection) {
            buildLeftView();
        } else if (TextLocation.top.ordinal() == textDirection) {
            IconView textView = getText();
            LayoutParams tvparams = (LayoutParams) textView.getLayoutParams();
            tvparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            this.addView(textView);

            IconView iconTextView = getIconTextView();
            LayoutParams params = (LayoutParams) iconTextView.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, ViewIds.textViewId);
            params.setMargins(0, textFontSpacing, 0, 0);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            this.addView(iconTextView);
        } else if (TextLocation.right.ordinal() == textDirection) {
            IconView iconTextView = getIconTextView();
            LayoutParams iconParams = (LayoutParams) iconTextView.getLayoutParams();
            iconParams.addRule(RelativeLayout.CENTER_VERTICAL);

            int padd = dip2px(getContext(), 3);
            if (iconGravity == IconGravity.top.ordinal()) {
                iconTextView.setGravity(Gravity.TOP);
                iconTextView.setPadding(0, padd, 0, 0);
            } else if (iconGravity == IconGravity.bottom.ordinal()) {
                iconTextView.setGravity(Gravity.BOTTOM);
                iconTextView.setPadding(0, 0, 0, padd);
            } else if (iconGravity == IconGravity.center_vertical.ordinal()) {
                iconTextView.setGravity(Gravity.CENTER_VERTICAL);
            }
            this.addView(iconTextView);

            IconView textView = getText();
            LayoutParams tvparams = (LayoutParams) textView.getLayoutParams();
            tvparams.addRule(RelativeLayout.CENTER_VERTICAL);
            tvparams.addRule(RelativeLayout.RIGHT_OF, ViewIds.iconViewId);
            tvparams.setMargins(textFontSpacing, 0, 0, 0);
            this.addView(textView);
        } else if (TextLocation.bottom.ordinal() == textDirection) {
            IconView iconTextView = getIconTextView();
            LayoutParams params = (LayoutParams) iconTextView.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            this.addView(iconTextView);

            IconView textView = getText();
            LayoutParams tvparams = (LayoutParams) textView.getLayoutParams();
            tvparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            tvparams.addRule(RelativeLayout.BELOW, ViewIds.iconViewId);
            tvparams.setMargins(0, textFontSpacing, 0, 0);
            this.addView(textView);
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private IconView getIconTextView() {
        LayoutParams param = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        IconView iconView = (IconView) View.inflate(getContext(), R.layout.cl_icon_view, null);
        iconView.setId(ViewIds.iconViewId);
        iconView.setLayoutParams(param);
        if (iconColor != 0) {
            iconView.setTextColor(iconColor);
        }
        iconView.setTextSize(TypedValue.COMPLEX_UNIT_PX, iconSize);
        iconView.setIconUcode(iconUcode);
        return iconView;
    }

    private IconView getText() {
        LayoutParams tvparam = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        IconView textView = (IconView) View.inflate(getContext(), R.layout.cl_icon_view, null);
        textView.setLayoutParams(tvparam);
        textView.setId(ViewIds.textViewId);
        textView.setTextColor(textColor == 0 ? Color.parseColor("#333333") : textColor);
        textView.setDimensionTextSize(textSize * 4 / 5);
        if (textStyle == TextStyle.agencyb.ordinal()) {
            textView.setFontStyle(FontType.agencyb);
        }
        textView.setIconUcode(text);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        return textView;
    }

    /**
     * 设置图标字体
     *
     * @param iconUcode 图标字体代码
     */
    public void setIconUcode(String iconUcode) {
        IconView iconView = (IconView) findViewById(ViewIds.iconViewId);
        if (iconView != null) {
            iconView.setIconUcode(iconUcode);
        }
    }

    /**
     * 设置图标颜色
     *
     * @param color 颜色
     */
    public void setIconColor(int color) {
        IconView iconView = (IconView) findViewById(ViewIds.iconViewId);
        if (iconView != null) {
            iconView.setTextColor(color);
        }
    }

    public void setTextColor(int color) {
        TextView textView = (TextView) findViewById(ViewIds.textViewId);
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setTextSize(int unit, int textSize) {
        IconView textView = (IconView) findViewById(ViewIds.textViewId);
        if (textView != null) {
            textView.setTextSize(unit, textSize);
        }
    }

    /**
     * 设置文本
     *
     * @param text 要显示的文本
     */
    public void setText(String text) {
        IconView textView = (IconView) findViewById(ViewIds.textViewId);
        if (textView != null) {
            textView.setIconUcode(text);
        }
    }
}
