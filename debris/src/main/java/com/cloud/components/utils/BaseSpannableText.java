package com.cloud.components.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;

import com.cloud.components.beans.SpannableTagItem;
import com.cloud.objects.RoundedBackgroundSpan;
import com.cloud.objects.utils.SpannableUtils;
import com.cloud.objects.utils.ValidUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/23
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class BaseSpannableText {

    private SpannableUtils.OnClickableSpanListener onClickableSpanListener = null;
    private boolean isEnableUnderline = false;

    /**
     * 设置超链接文本点击监听
     *
     * @param listener 超链接文本点击监听
     */
    public void setOnClickableSpanListener(SpannableUtils.OnClickableSpanListener listener) {
        this.onClickableSpanListener = listener;
    }

    /**
     * 是否启用下划线
     *
     * @param enableUnderline true-显示下划线;false-不显示;
     */
    public void setEnableUnderline(boolean enableUnderline) {
        isEnableUnderline = enableUnderline;
    }

    public SpannableStringBuilder getSpannable(Context context, AttributeSet attrs, CharSequence text, SpannableTagItem... tagItems) {
        SpannableUtils spannableUtils = new SpannableUtils();
        String content = text.toString();
        //过滤font标签
        String tagPatten = "<font(\\S*?) [^>]*>*?[^>]*?[^>]*>([\\s\\S]*?)</font>";
        List<String> list = ValidUtils.matches(tagPatten, text.toString());

        int textColor = 0;
        int textSize = 0;

        if (attrs != null) {
            int[] androidAttrs = new int[]{
                    android.R.attr.textColor,
                    android.R.attr.textSize
            };
            TypedArray a = context.obtainStyledAttributes(attrs, androidAttrs);
            textColor = a.getColor(0, 0);
            textSize = a.getInt(1, 0);
            a.recycle();
        }

        StringBuffer sbtext = new StringBuffer();
        List<GroupItem> items = splitItems(content, sbtext, list, textSize, textColor);

        spannableUtils.newSpannalbe(sbtext.toString());
        for (GroupItem item : items) {
            if (item.textSize != 0) {
                spannableUtils.setSizeSpan(item.textSize, item.start, item.end);
            }
            if (item.clickable) {
                spannableUtils.setOnClickableSpanListener(onClickableSpanListener);
                spannableUtils.setSpannableClick(item.start, item.end, item.text, item.textColor, isEnableUnderline);
            } else {
                if (item.textColor != 0) {
                    spannableUtils.setColorSpan(item.textColor, item.start, item.end);
                }
            }
        }
        //添加标签
        SpannableStringBuilder builder = spannableUtils.getBuilder();
        if (tagItems != null && tagItems.length > 0) {
            for (SpannableTagItem tagItem : tagItems) {
                if (TextUtils.isEmpty(tagItem.getText())) {
                    continue;
                }
                if (tagItem.getTextSize() == 0) {
                    tagItem.setTextSize(textSize);
                }
                //标签添加至对象中
                int start = builder.length() + 1;
                builder.append(String.format("  %s ", tagItem.getText()));
                int end = builder.length();
                RoundedBackgroundSpan span = new RoundedBackgroundSpan(tagItem.getBackgroundColor(), tagItem.getTextColor());
                builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (tagItem.getTextSize() != 0) {
                    AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(tagItem.getTextSize(), true);
                    builder.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return builder;
    }

    private List<GroupItem> splitItems(String content, StringBuffer sbtext, List<String> list, int textSize, int textColor) {
        int pos = 0;

        List<GroupItem> items = new ArrayList<GroupItem>();
        if (list != null && list.size() > 0) {
            for (String item : list) {
                int index = content.indexOf(item);
                if (index < 0) {
                    continue;
                }
                if (index == 0) {
                    //样式文本
                    GroupItem groupItem = getGroupItem(item, pos);
                    if (groupItem.textSize == 0) {
                        groupItem.textSize = textSize;
                    }
                    if (groupItem.textColor == 0) {
                        groupItem.textColor = textColor;
                    }
                    pos = groupItem.end;
                    items.add(groupItem);
                    sbtext.append(groupItem.text);
                    content = content.substring(item.length());
                    continue;
                }
                //默认文本
                String subtext = content.substring(0, index);
                sbtext.append(subtext);
                GroupItem groupItem = new GroupItem();
                groupItem.textSize = textSize;
                groupItem.textColor = textColor;
                groupItem.start = pos;
                pos += index;
                groupItem.end = pos;
                items.add(groupItem);
                content = content.substring(subtext.length());
                //样式文本
                GroupItem gitem = getGroupItem(item, pos);
                if (gitem.textSize == 0) {
                    gitem.textSize = textSize;
                }
                if (gitem.textColor == 0) {
                    gitem.textColor = textColor;
                }
                pos = gitem.end;
                items.add(gitem);
                sbtext.append(gitem.text);
                content = content.substring(item.length());
            }
        }
        //默认文本
        sbtext.append(content);
        GroupItem groupItem = new GroupItem();
        groupItem.textSize = textSize;
        groupItem.textColor = textColor;
        groupItem.start = pos;
        pos += content.length();
        groupItem.end = pos;
        items.add(groupItem);

        return items;
    }

    private GroupItem getGroupItem(String item, int pos) {
        GroupItem groupItem = getGroupItem(item);
        groupItem.start = pos;
        pos += groupItem.text.length();
        groupItem.end = pos;
        return groupItem;
    }

    private GroupItem getGroupItem(String content) {
        GroupItem groupItem = new GroupItem();
        String color = matchAttr(content, "font", "color");
        String size = matchAttr(content, "font", "size");
        String clickable = matchAttr(content, "font", "clickable");
        groupItem.text = matchText(content, "font");
        String hexColor = getHexColor(color);
        if (!TextUtils.isEmpty(hexColor)) {
            groupItem.textColor = Color.parseColor(hexColor);
        }
        size = ValidUtils.matche("\\d+", size);
        if (!TextUtils.isEmpty(size)) {
            groupItem.textSize = Integer.parseInt(size);
        }
        groupItem.clickable = TextUtils.equals(clickable, "true");
        return groupItem;
    }

    private String getHexColor(String hexColor) {
        Matcher matcher = Pattern.compile("[\\da-zA-Z]+").matcher(hexColor);
        while (matcher.find()) {
            hexColor = matcher.group();
        }
        if (hexColor.length() < 6 || hexColor.length() > 8 || hexColor.length() == 7) {
            return "";
        }
        return "#" + hexColor;
    }

    private String matchText(String source, String element) {
        String result = "";
        String reg = "<" + element + ".*?>([\\s\\S]*?)</" + element + ">";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            result = m.group(1);
        }
        return result;
    }

    private String matchAttr(String source, String element, String attr) {
        String result = "";
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            result = m.group(1);
        }
        return result;
    }

    private class GroupItem {
        public String text = "";
        public int textSize = 0;
        public int textColor = 0;
        public int start = 0;
        public int end = 0;
        public boolean clickable = false;
    }

}
