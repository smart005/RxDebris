package com.cloud.components.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.components.icons.IconView;
import com.cloud.debris.R;
import com.cloud.objects.utils.PixelUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/22
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class TextItemView extends RelativeLayout {

    //文本
    private String text = "";
    //文本颜色
    private int textColor = 0;
    //文本大小
    private int textSize = 0;
    private String iconUcode = "";
    private int iconColor = 0;
    private float iconSize = 0;
    private int textViewId = 1614140121;

    public TextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        float defSize = PixelUtils.dip2px(context, 12);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextItemView);
        text = a.getString(R.styleable.TextItemView_tiv_text);
        textColor = a.getColor(R.styleable.TextItemView_tiv_textColor, Color.parseColor("#041d29"));
        textSize = (int) a.getDimension(R.styleable.TextItemView_tiv_textSize, PixelUtils.dip2px(context, 14));
        iconUcode = a.getString(R.styleable.TextItemView_tiv_ucode);
        iconColor = a.getColor(R.styleable.TextItemView_tiv_iconColor, 0);
        iconSize = a.getDimension(R.styleable.TextItemView_tiv_iconSize, defSize);
        a.recycle();

        this.addView(createTextView());
        this.addView(getIconTextView());
    }

    private TextView createTextView() {
        LayoutParams tvparam = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        int tbpadd = PixelUtils.dip2px(getContext(), 15);
        int lpadd = PixelUtils.dip2px(getContext(), 15);
        tvparam.setMargins(lpadd, tbpadd, 0, tbpadd);
        tvparam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextView textView = new TextView(getContext());
        textView.setId(textViewId);
        textView.setLayoutParams(tvparam);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        return textView;
    }

    private IconView getIconTextView() {
        LayoutParams param = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        param.setMargins(0, 0, PixelUtils.dip2px(getContext(), 15), 0);
        param.addRule(RelativeLayout.CENTER_VERTICAL);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        IconView iconView = (IconView) View.inflate(getContext(), R.layout.cl_icon_view, null);
        iconView.setLayoutParams(param);
        if (iconColor != 0) {
            iconView.setTextColor(iconColor);
        }
        iconView.setTextSize(TypedValue.COMPLEX_UNIT_PX, iconSize);
        iconView.setIconUcode(iconUcode);
        return iconView;
    }

    public void setText(String text) {
        TextView textView = (TextView) findViewById(textViewId);
        if (textView == null) {
            return;
        }
        textView.setText(text);
    }
}
