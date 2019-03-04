package com.cloud.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloud.components.enums.FontType;
import com.cloud.components.events.OnSpannableTextClickListener;
import com.cloud.components.icons.IconView;
import com.cloud.debris.R;
import com.cloud.objects.utils.PixelUtils;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/18
 * Description:图标-文本-选择
 * Modifier:
 * ModifyContent:
 */
public class IconSpannableCheckBox extends RelativeLayout {

    private int iconColor = 0;
    private int iconSize = 0;
    private String icon = "";
    private int chbButtonDrawable = 0;
    private boolean isEnableUnderline = false;
    private CharSequence text = "";
    private int textColor = 0;
    private int iconTextPadding = 0;
    private boolean checked = false;
    private OnSpannableTextClickListener onSpannableTextClickListener = null;

    public IconSpannableCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconSpannableCheckBox);
        iconColor = a.getColor(R.styleable.IconSpannableCheckBox_iscb_iconColor, Color.parseColor("#333333"));
        iconSize = a.getDimensionPixelSize(R.styleable.IconSpannableCheckBox_iscb_iconSize, PixelUtils.dip2px(context, 20));
        icon = a.getString(R.styleable.IconSpannableCheckBox_iscb_icon);
        chbButtonDrawable = a.getResourceId(R.styleable.IconSpannableCheckBox_iscb_chbButtonDrawable, R.drawable.cl_check_box_bg);
        isEnableUnderline = a.getBoolean(R.styleable.IconSpannableCheckBox_iscb_isEnableUnderline, false);
        text = a.getText(R.styleable.IconSpannableCheckBox_iscb_text);
        textColor = a.getColor(R.styleable.IconSpannableCheckBox_iscb_textColor, Color.parseColor("#041d29"));
        iconTextPadding = a.getDimensionPixelSize(R.styleable.IconSpannableCheckBox_iscb_iconTextPadding, 0);
        checked = a.getBoolean(R.styleable.IconSpannableCheckBox_iscb_checked, false);
        a.recycle();

        this.addView(getText());
        this.addView(getCheckBox());
    }

    private interface ViewIds {
        public int iconViewId = 379125939;
    }

    private IconView getText() {
        LayoutParams tvparam = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        tvparam.addRule(RelativeLayout.CENTER_VERTICAL);
        tvparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        IconView textView = (IconView) View.inflate(getContext(), R.layout.cl_icon_view, null);
        textView.setLayoutParams(tvparam);
        textView.setId(ViewIds.iconViewId);
        textView.setTextColor(iconColor);
        textView.setDimensionTextSize(iconSize * 4 / 5);
        textView.setFontStyle(FontType.icon);
        textView.setIconUcode(icon);
        return textView;
    }

    private SpannableCheckBox getCheckBox() {
        LayoutParams tvparam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tvparam.addRule(RelativeLayout.CENTER_VERTICAL);
        tvparam.addRule(RelativeLayout.RIGHT_OF, ViewIds.iconViewId);
        tvparam.leftMargin = iconTextPadding;
        SpannableCheckBox checkBox = new SpannableCheckBox(getContext());
        checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        checkBox.setButtonDrawable(chbButtonDrawable);
        checkBox.setLayoutParams(tvparam);
        checkBox.setOnSpannableTextClickListener(new OnSpannableTextClickListener() {
            @Override
            public void onSpannableTextClick(View view, Object extras) {
                if (onSpannableTextClickListener == null) {
                    return;
                }
                onSpannableTextClickListener.onSpannableTextClick(IconSpannableCheckBox.this, extras);
            }
        });
        checkBox.setEnableUnderline(isEnableUnderline);
        checkBox.setTextColor(textColor);
        checkBox.setChecked(checked);
        checkBox.setSpannableText(text);
        return checkBox;
    }

    /**
     * 设置SpannableText点击监听
     *
     * @param listener SpannableText点击监听
     */
    public void setOnSpannableTextClickListener(OnSpannableTextClickListener listener) {
        this.onSpannableTextClickListener = listener;
    }
}
