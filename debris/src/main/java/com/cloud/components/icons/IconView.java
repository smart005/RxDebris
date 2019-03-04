package com.cloud.components.icons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.cloud.components.enums.FontType;
import com.cloud.debris.R;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/20
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class IconView extends TextView {

    private String iconUcode = "";
    private int fontStyle = 0;
    private boolean isShowDelLine = false;

    private enum FontStyle {
        icon,
        agencyb
    }

    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IconView);
        iconUcode = a.getString(R.styleable.IconView_itv_ucode);
        fontStyle = a.getInt(R.styleable.IconView_itv_fontStyle, 0);
        isShowDelLine = a.getBoolean(R.styleable.IconView_itv_isShowDelLine, false);
        a.recycle();
        int[] androidAttrs = new int[]{
                android.R.attr.textSize
        };
        TypedArray a1 = getContext().obtainStyledAttributes(attrs, androidAttrs);
        float textSize = a1.getDimension(0, 0);
        if (textSize > 0) {
            float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
            textSize = ((int) (textSize / fontScale + 0.5f)) * 5 / 4;
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
        a1.recycle();
        this.setText(iconUcode);
        if (fontStyle == 0) {
            setFontStyle(FontType.icon);
        } else if (fontStyle == 1) {
            setFontStyle(FontType.agencyb);
        }
        setShowDelLine(isShowDelLine);
        this.setIncludeFontPadding(false);
    }

    public void setShowDelLine(boolean isShowDelLine) {
        this.isShowDelLine = isShowDelLine;
        if (isShowDelLine) {
            TextPaint paint = this.getPaint();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);
        }
    }

    /**
     * 设置字体样式
     *
     * @param fontType 字体样式
     */
    public void setFontStyle(FontType fontType) {
        if (fontType == FontType.icon) {
            this.setTypeface(IconLibs.getInstance().getIconfont(getContext(), FontType.icon));
        } else if (fontType == FontType.agencyb) {
            this.setTypeface(IconLibs.getInstance().getIconfont(getContext(), FontType.agencyb));
        }
    }

    /**
     * 设置字体大小
     *
     * @param textSize 字体大小(以dp为单位)
     */
    public void setDimensionTextSize(float textSize) {
        if (textSize > 0) {
            float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
            textSize = ((int) (textSize / fontScale + 0.5f)) * 5 / 4;
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    /**
     * 设置字体图标
     *
     * @param iconUcode unicode图标字符
     */
    public void setIconUcode(String iconUcode) {
        if (TextUtils.isEmpty(iconUcode)) {
            this.setText("");
            return;
        }
        iconUcode = iconUcode.trim();
        StringBuilder sb = new StringBuilder();
        while (iconUcode.length() > 0) {
            int index = iconUcode.indexOf("&#x");
            if (index >= 0) {
                sb.append(iconUcode.substring(0, index));
                //复位
                iconUcode = iconUcode.substring(index + 3);
                //图标结束符号索引
                int esignIndex = iconUcode.indexOf(";");
                if (esignIndex >= 0) {
                    String ucode = "\\u" + iconUcode.substring(0, esignIndex);
                    sb.append(decode(ucode));
                    iconUcode = iconUcode.substring(esignIndex + 1);
                } else {
                    sb.append(iconUcode);
                    break;
                }
            } else {
                sb.append(iconUcode);
                break;
            }
        }
        this.setText(sb.toString());
    }

    private String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }
}
