package com.cloud.components.beans;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/26
 * Description:Spannable标签属性
 * Modifier:
 * ModifyContent:
 */
public class SpannableTagItem {
    //标签背景颜色
    private int backgroundColor = 0;
    //标签文本颜色
    private int textColor = 0;
    //标签文本
    private String text = "";
    //标签文本大小
    private int textSize = 0;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
