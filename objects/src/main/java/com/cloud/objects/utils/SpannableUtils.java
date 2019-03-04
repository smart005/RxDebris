package com.cloud.objects.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.cloud.objects.RoundedBackgroundSpan;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/9/7
 * Description: Spannable String操作
 * Modifier:
 * ModifyContent:
 */
public class SpannableUtils {

    private SpannableStringBuilder stringBuilder = null;
    private AbsoluteSizeSpan sizeSpan = null;
    private String text = null;
    private OnClickableSpanListener onClickableSpanListener = null;

    /**
     * 设置超链接文本点击监听
     *
     * @param listener 超链接文本点击监听
     */
    public void setOnClickableSpanListener(OnClickableSpanListener listener) {
        this.onClickableSpanListener = listener;
    }

    /**
     * 新建SpannableStringBuilder对象
     *
     * @param text 需要处理的文本
     */
    public void newSpannalbe(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        this.text = text;
        stringBuilder = new SpannableStringBuilder(text);
    }

    /**
     * 设置start和end之间的字体大小
     *
     * @param spValue sp值，内部已转换成px值
     * @param start   设置字体大小的开始索引
     * @param end     设置字体大小的结束索引
     */
    public void setSizeSpan(int spValue, int start, int end) {
        if (stringBuilder == null || start < 0 || start >= end) {
            return;
        }
        int length = text.length();
        if (start >= length) {
            return;
        }
        if (end > length) {
            end = length;
        }
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(spValue, true);
        stringBuilder.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private int[] getChartPosition(String start, String end, boolean isIncludeStart) {
        int spos = 0, epos = 0;
        //计算开始索引
        if (!TextUtils.isEmpty(start)) {
            int index = text.indexOf(start);
            if (index >= 0) {
                if (!isIncludeStart) {
                    index += start.length();
                }
                spos = index;
            }
        }
        //计算结束索引
        if (!TextUtils.isEmpty(end)) {
            int index = text.indexOf(end);
            if (index >= 0) {
                epos = index;
            }
        }
        return new int[]{spos, epos};
    }

    /**
     * 设置start(isIncludeStart=true时包括start)到end(不包括end)的字体大小
     *
     * @param spValue        sp值，内部已转换成px值
     * @param start          设置字体大小的开始字符
     * @param end            设置字体大小的结束字符
     * @param isIncludeStart 是否包含开始字符;
     *                       true-start字符也设置成spValue;
     *                       false-只有start和end之间的字符设置成spValue;
     */
    public void setSizeSpan(int spValue, String start, String end, boolean isIncludeStart) {
        if (stringBuilder == null) {
            return;
        }
        int[] positions = getChartPosition(start, end, isIncludeStart);
        setSizeSpan(spValue, positions[0], positions[1]);
    }

    /**
     * 设置start(不包括start)和end之间的字体大小
     *
     * @param spValue sp值，内部已转换成px值
     * @param start   设置字体大小的开始字符
     * @param end     设置字体大小的结束字符
     */
    public void setSizeSpan(int spValue, String start, String end) {
        setSizeSpan(spValue, start, end, false);
    }

    /**
     * 设置start和end之间的字体颜色
     *
     * @param color 要设置字体颜色
     * @param start 设置字体颜色的开始索引
     * @param end   设置字体颜色的结束索引
     */
    public void setColorSpan(int color, int start, int end) {
        if (stringBuilder == null || start < 0 || start >= end) {
            return;
        }
        int length = text.length();
        if (start >= length) {
            return;
        }
        if (end > length) {
            end = length;
        }
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        stringBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置start(isIncludeStart=true时包括start)到end(不包括end)的字体颜色
     *
     * @param color          要设置字体颜色
     * @param start          设置字体颜色的开始索引
     * @param end            设置字体颜色的结束索引
     * @param isIncludeStart 是否包含开始字符;
     *                       true-start字符也设置成color;
     *                       false-只有start和end之间的字符设置成color;
     */
    public void setColorSpan(int color, String start, String end, boolean isIncludeStart) {
        if (stringBuilder == null) {
            return;
        }
        int[] positions = getChartPosition(start, end, isIncludeStart);
        setColorSpan(color, positions[0], positions[1]);
    }

    /**
     * 设置start(不包括start)和end之间的字体颜色
     *
     * @param color 要设置字体颜色
     * @param start 设置字体颜色的开始索引
     * @param end   设置字体颜色的结束索引
     */
    public void setColorSpan(int color, String start, String end) {
        setColorSpan(color, start, end, false);
    }

    /**
     * 设置start和end之间的字体下划线
     *
     * @param start 设置字体下划线的开始索引
     * @param end   设置字体下划线的结束索引
     */
    public void setUnderlineSpan(int start, int end) {
        if (stringBuilder == null || start < 0 || start >= end) {
            return;
        }
        int length = text.length();
        if (start >= length) {
            return;
        }
        if (end > length) {
            end = length;
        }
        UnderlineSpan underlineSpan = new UnderlineSpan();
        stringBuilder.setSpan(underlineSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置start(isIncludeStart=true时包括start)到end(不包括end)的字体下划线
     *
     * @param start          设置字体下划线的开始索引
     * @param end            设置字体下划线的结束索引
     * @param isIncludeStart 是否包含开始字符;
     *                       true-start字符也设置成下划线;
     *                       false-只有start和end之间的字符设置成下划线;
     */
    public void setUnderlineSpan(String start, String end, boolean isIncludeStart) {
        if (stringBuilder == null) {
            return;
        }
        int[] positions = getChartPosition(start, end, isIncludeStart);
        setUnderlineSpan(positions[0], positions[1]);
    }

    /**
     * 设置start(不包含start)到end(不包括end)的字体下划线
     *
     * @param start 设置字体下划线的开始索引
     * @param end   设置字体下划线的结束索引
     */
    public void setUnderlineSpan(String start, String end) {
        setUnderlineSpan(start, end, false);
    }

    /**
     * 设置start和end之间的字体删除线
     *
     * @param start 设置字体删除线的开始索引
     * @param end   设置字体删除线的结束索引
     */
    public void setStrikethroughSpan(int start, int end) {
        if (stringBuilder == null || start < 0 || start >= end) {
            return;
        }
        int length = text.length();
        if (start >= length) {
            return;
        }
        if (end > length) {
            end = length;
        }
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        stringBuilder.setSpan(strikethroughSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置start(isIncludeStart=true时包括start)到end(不包括end)的字体删除线
     *
     * @param start          设置字体删除线的开始索引
     * @param end            设置字体删除线的结束索引
     * @param isIncludeStart 是否包含开始字符;
     *                       true-start字符也设置成删除线;
     *                       false-只有start和end之间的字符设置成删除线;
     */
    public void setStrikethroughSpan(String start, String end, boolean isIncludeStart) {
        if (stringBuilder == null) {
            return;
        }
        int[] positions = getChartPosition(start, end, isIncludeStart);
        setStrikethroughSpan(positions[0], positions[1]);
    }

    /**
     * 设置start(不包含start)到end(不包括end)的字体删除线
     *
     * @param start 设置字体删除线的开始索引
     * @param end   设置字体删除线的结束索引
     */
    public void setStrikethroughSpan(String start, String end) {
        setStrikethroughSpan(start, end, false);
    }

    /**
     * 设置start和end之间的字体点击事件
     *
     * @param start             设置字体点击事件的开始索引
     * @param end               设置字体点击事件的结束索引
     * @param extras            扩展数据
     * @param color             超链接文本颜色
     * @param isEnableUnderline 是否启用下划线
     */
    public <T> void setSpannableClick(int start, int end, T extras, int color, boolean isEnableUnderline) {
        if (stringBuilder == null || start < 0 || start >= end) {
            return;
        }
        int length = text.length();
        if (start >= length) {
            return;
        }
        if (end > length) {
            end = length;
        }
        OnClickableSpan clickableSpan = new OnClickableSpan(extras, color, isEnableUnderline);
        stringBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置start(isIncludeStart=true时包括start)到end(不包括end)的字体点击事件
     *
     * @param start             设置字体点击事件的开始索引
     * @param end               设置字体点击事件的结束索引
     * @param isIncludeStart    是否包含开始字符;
     *                          true-start字符也设置成点击事件;
     *                          false-只有start和end之间的字符设置成点击事件;
     * @param extras            扩展数据
     * @param color             超链接文本颜色
     * @param isEnableUnderline 是否启用下划线
     */
    public <T> void setSpannableClick(String start, String end, boolean isIncludeStart, T extras, int color, boolean isEnableUnderline) {
        if (stringBuilder == null) {
            return;
        }
        int[] positions = getChartPosition(start, end, isIncludeStart);
        setSpannableClick(positions[0], positions[1], extras, color, isEnableUnderline);
    }

    /**
     * 设置start(不包含start)到end(不包括end)的字体点击事件
     *
     * @param start             设置字体点击事件的开始索引
     * @param end               设置字体点击事件的结束索引
     * @param extras            扩展数据
     * @param color             超链接文本颜色
     * @param isEnableUnderline 是否启用下划线
     */
    public <T> void setSpannableClick(String start, String end, T extras, int color, boolean isEnableUnderline) {
        setSpannableClick(start, end, false, extras, color, isEnableUnderline);
    }

    /**
     * 设置start和end之间的字体为标签模式
     *
     * @param start           设置字体标签的开始索引
     * @param end             设置字体标签的结束索引
     * @param backgroundColor 标签背景
     * @param textColor       标签文本颜色
     */
    public void setSpannableTag(int start, int end, int backgroundColor, int textColor) {
        if (stringBuilder == null || start < 0 || start >= end) {
            return;
        }
        int length = text.length();
        if (start >= length) {
            return;
        }
        if (end > length) {
            end = length;
        }
        RoundedBackgroundSpan span = new RoundedBackgroundSpan(backgroundColor, textColor);
        stringBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置start和end之间的字体为标签模式
     *
     * @param start           设置字体标签的开始索引
     * @param end             设置字体标签的结束索引
     * @param isIncludeStart  是否包含开始字符;
     *                        true-start字符也设置标签模式;
     *                        false-只有start和end之间的字符设置标签模式;
     * @param backgroundColor 标签背景
     * @param textColor       标签文本颜色
     */
    public void setSpannableTag(String start, String end, boolean isIncludeStart, int backgroundColor, int textColor) {
        if (stringBuilder == null) {
            return;
        }
        int[] positions = getChartPosition(start, end, isIncludeStart);
        setSpannableTag(positions[0], positions[1], backgroundColor, textColor);
    }

    /**
     * 设置start和end之间的字体为标签模式
     *
     * @param start           设置字体标签的开始索引
     * @param end             设置字体标签的结束索引
     * @param backgroundColor 标签背景
     * @param textColor       标签文本颜色
     */
    public void setSpannableTag(String start, String end, int backgroundColor, int textColor) {
        setSpannableTag(start, end, false, backgroundColor, textColor);
    }

    public class Builder {

        private int defaultValue = 0x12000000;
        private CharSequence text;

        private int flag;
        private int foregroundColor;
        private int backgroundColor;
        private int quoteColor;

        private boolean isLeadingMargin;
        private int first;
        private int rest;

        private boolean isBullet;
        private int gapWidth;
        private int bulletColor;

        private float proportion;
        private float xProportion;
        private boolean isStrikethrough;
        private boolean isUnderline;
        private boolean isSuperscript;
        private boolean isSubscript;
        private boolean isBold;
        private boolean isItalic;
        private boolean isBoldItalic;
        private String fontFamily;
        private Layout.Alignment align;

        private boolean imageIsBitmap;
        private Bitmap bitmap;
        private boolean imageIsDrawable;
        private Drawable drawable;
        private boolean imageIsUri;
        private Uri uri;
        private boolean imageIsResourceId;
        private int resourceId;

        private ClickableSpan clickSpan;
        private String url;

        private boolean isBlur;
        private float radius;
        private BlurMaskFilter.Blur style;

        private SpannableStringBuilder mBuilder;


        private Builder(CharSequence text) {
            this.text = text;
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
            foregroundColor = defaultValue;
            backgroundColor = defaultValue;
            quoteColor = defaultValue;
            proportion = -1;
            xProportion = -1;
            mBuilder = new SpannableStringBuilder();
        }

        /**
         * 设置标识
         *
         * @param flag <ul>
         *             <li>{@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}</li>
         *             <li>{@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}</li>
         *             <li>{@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}</li>
         *             <li>{@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}</li>
         *             </ul>
         * @return {@link Builder}
         */
        public Builder setFlag(int flag) {
            this.flag = flag;
            return this;
        }

        /**
         * 设置前景色
         *
         * @param color 前景色
         * @return {@link Builder}
         */
        public Builder setForegroundColor(int color) {
            this.foregroundColor = color;
            return this;
        }

        /**
         * 设置背景色
         *
         * @param color 背景色
         * @return {@link Builder}
         */
        public Builder setBackgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        /**
         * 设置引用线的颜色
         *
         * @param color 引用线的颜色
         * @return {@link Builder}
         */
        public Builder setQuoteColor(int color) {
            this.quoteColor = color;
            return this;
        }

        /**
         * 设置缩进
         *
         * @param first 首行缩进
         * @param rest  剩余行缩进
         * @return {@link Builder}
         */
        public Builder setLeadingMargin(int first, int rest) {
            this.first = first;
            this.rest = rest;
            isLeadingMargin = true;
            return this;
        }

        /**
         * 设置列表标记
         *
         * @param gapWidth 列表标记和文字间距离
         * @param color    列表标记的颜色
         * @return {@link Builder}
         */
        public Builder setBullet(int gapWidth, int color) {
            this.gapWidth = gapWidth;
            bulletColor = color;
            isBullet = true;
            return this;
        }

        /**
         * 设置字体比例
         *
         * @param proportion 比例
         * @return {@link Builder}
         */
        public Builder setProportion(float proportion) {
            this.proportion = proportion;
            return this;
        }

        /**
         * 设置字体横向比例
         *
         * @param proportion 比例
         * @return {@link Builder}
         */
        public Builder setXProportion(float proportion) {
            this.xProportion = proportion;
            return this;
        }

        /**
         * 设置删除线
         *
         * @return {@link Builder}
         */
        public Builder setStrikethrough() {
            this.isStrikethrough = true;
            return this;
        }

        /**
         * 设置下划线
         *
         * @return {@link Builder}
         */
        public Builder setUnderline() {
            this.isUnderline = true;
            return this;
        }

        /**
         * 设置上标
         *
         * @return {@link Builder}
         */
        public Builder setSuperscript() {
            this.isSuperscript = true;
            return this;
        }

        /**
         * 设置下标
         *
         * @return {@link Builder}
         */
        public Builder setSubscript() {
            this.isSubscript = true;
            return this;
        }

        /**
         * 设置粗体
         *
         * @return {@link Builder}
         */
        public Builder setBold() {
            isBold = true;
            return this;
        }

        /**
         * 设置斜体
         *
         * @return {@link Builder}
         */
        public Builder setItalic() {
            isItalic = true;
            return this;
        }

        /**
         * 设置粗斜体
         *
         * @return {@link Builder}
         */
        public Builder setBoldItalic() {
            isBoldItalic = true;
            return this;
        }

        /**
         * 设置字体
         *
         * @param fontFamily 字体
         *                   <ul>
         *                   <li>monospace</li>
         *                   <li>serif</li>
         *                   <li>sans-serif</li>
         *                   </ul>
         * @return {@link Builder}
         */
        public Builder setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }

        /**
         * 设置对齐
         * <ul>
         * <li>{@link Layout.Alignment#ALIGN_NORMAL}正常</li>
         * <li>{@link Layout.Alignment#ALIGN_OPPOSITE}相反</li>
         * <li>{@link Layout.Alignment#ALIGN_CENTER}居中</li>
         * </ul>
         *
         * @return {@link Builder}
         */
        public Builder setAlign(Layout.Alignment align) {
            this.align = align;
            return this;
        }

        /**
         * 设置图片
         *
         * @param bitmap 图片位图
         * @return {@link Builder}
         */
        public Builder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            imageIsBitmap = true;
            return this;
        }

        /**
         * 设置图片
         *
         * @param drawable 图片资源
         * @return {@link Builder}
         */
        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            imageIsDrawable = true;
            return this;
        }

        /**
         * 设置图片
         *
         * @param uri 图片uri
         * @return {@link Builder}
         */
        public Builder setUri(Uri uri) {
            this.uri = uri;
            imageIsUri = true;
            return this;
        }

        /**
         * 设置图片
         *
         * @param resourceId 图片资源id
         * @return {@link Builder}
         */
        public Builder setResourceId(int resourceId) {
            this.resourceId = resourceId;
            imageIsResourceId = true;
            return this;
        }

        /**
         * 设置点击事件
         * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
         *
         * @param clickSpan 点击事件
         * @return {@link Builder}
         */
        public Builder setClickSpan(ClickableSpan clickSpan) {
            this.clickSpan = clickSpan;
            return this;
        }

        /**
         * 设置超链接
         * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
         *
         * @param url 超链接
         * @return {@link Builder}
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置模糊
         * <p>尚存bug，其他地方存在相同的字体的话，相同字体出现在之前的话那么就不会模糊，出现在之后的话那会一起模糊</p>
         * <p>推荐还是把所有字体都模糊这样使用</p>
         *
         * @param radius 模糊半径（需大于0）
         * @param style  模糊样式<ul>
         *               <li>{@link BlurMaskFilter.Blur#NORMAL}</li>
         *               <li>{@link BlurMaskFilter.Blur#SOLID}</li>
         *               <li>{@link BlurMaskFilter.Blur#OUTER}</li>
         *               <li>{@link BlurMaskFilter.Blur#INNER}</li>
         *               </ul>
         * @return {@link Builder}
         */
        public Builder setBlur(float radius, BlurMaskFilter.Blur style) {
            this.radius = radius;
            this.style = style;
            this.isBlur = true;
            return this;
        }

        /**
         * 追加样式字符串
         *
         * @param text 样式字符串文本
         * @return {@link Builder}
         */
        public Builder append(Context context, CharSequence text) {
            setSpan(context);
            this.text = text;
            return this;
        }

        /**
         * 创建样式字符串
         *
         * @return 样式字符串
         */
        public SpannableStringBuilder create(Context context) {
            setSpan(context);
            return mBuilder;
        }

        /**
         * 设置样式
         */
        private void setSpan(Context context) {
            int start = mBuilder.length();
            mBuilder.append(this.text);
            int end = mBuilder.length();
            if (foregroundColor != defaultValue) {
                mBuilder.setSpan(new ForegroundColorSpan(foregroundColor), start, end, flag);
                foregroundColor = defaultValue;
            }
            if (backgroundColor != defaultValue) {
                mBuilder.setSpan(new BackgroundColorSpan(backgroundColor), start, end, flag);
                backgroundColor = defaultValue;
            }
            if (isLeadingMargin) {
                mBuilder.setSpan(new LeadingMarginSpan.Standard(first, rest), start, end, flag);
                isLeadingMargin = false;
            }
            if (quoteColor != defaultValue) {
                mBuilder.setSpan(new QuoteSpan(quoteColor), start, end, 0);
                quoteColor = defaultValue;
            }
            if (isBullet) {
                mBuilder.setSpan(new BulletSpan(gapWidth, bulletColor), start, end, 0);
                isBullet = false;
            }
            if (proportion != -1) {
                mBuilder.setSpan(new RelativeSizeSpan(proportion), start, end, flag);
                proportion = -1;
            }
            if (xProportion != -1) {
                mBuilder.setSpan(new ScaleXSpan(xProportion), start, end, flag);
                xProportion = -1;
            }
            if (isStrikethrough) {
                mBuilder.setSpan(new StrikethroughSpan(), start, end, flag);
                isStrikethrough = false;
            }
            if (isUnderline) {
                mBuilder.setSpan(new UnderlineSpan(), start, end, flag);
                isUnderline = false;
            }
            if (isSuperscript) {
                mBuilder.setSpan(new SuperscriptSpan(), start, end, flag);
                isSuperscript = false;
            }
            if (isSubscript) {
                mBuilder.setSpan(new SubscriptSpan(), start, end, flag);
                isSubscript = false;
            }
            if (isBold) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
                isBold = false;
            }
            if (isItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
                isItalic = false;
            }
            if (isBoldItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, flag);
                isBoldItalic = false;
            }
            if (fontFamily != null) {
                mBuilder.setSpan(new TypefaceSpan(fontFamily), start, end, flag);
                fontFamily = null;
            }
            if (align != null) {
                mBuilder.setSpan(new AlignmentSpan.Standard(align), start, end, flag);
                align = null;
            }
            if (imageIsBitmap || imageIsDrawable || imageIsUri || imageIsResourceId) {
                if (imageIsBitmap) {
                    mBuilder.setSpan(new ImageSpan(context, bitmap), start, end, flag);
                    bitmap = null;
                    imageIsBitmap = false;
                } else if (imageIsDrawable) {
                    mBuilder.setSpan(new ImageSpan(drawable), start, end, flag);
                    drawable = null;
                    imageIsDrawable = false;
                } else if (imageIsUri) {
                    mBuilder.setSpan(new ImageSpan(context, uri), start, end, flag);
                    uri = null;
                    imageIsUri = false;
                } else {
                    mBuilder.setSpan(new ImageSpan(context, resourceId), start, end, flag);
                    resourceId = 0;
                    imageIsResourceId = false;
                }
            }
            if (clickSpan != null) {
                mBuilder.setSpan(clickSpan, start, end, flag);
                clickSpan = null;
            }
            if (url != null) {
                mBuilder.setSpan(new URLSpan(url), start, end, flag);
                url = null;
            }
            if (isBlur) {
                mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(radius, style)), start, end, flag);
                isBlur = false;
            }
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        }
    }

    /**
     * 获取SpannableStringBuilder对象
     *
     * @return SpannableStringBuilder
     */
    public SpannableStringBuilder getBuilder() {
        return this.stringBuilder;
    }

    /**
     * 获取建造者
     *
     * @return {@link Builder}
     */
    public Builder getBuilder(CharSequence text) {
        return new Builder(text);
    }

    public interface OnClickableSpanListener<T> {
        /**
         * 在超链接文本上点击监听
         *
         * @param extras 扩展数据
         */
        public void onSpannableClick(T extras);
    }

    private class OnClickableSpan<T> extends ClickableSpan {

        private T extras;
        private int color = 0;
        private boolean isEnableUnderline = false;

        public OnClickableSpan(T extras, int color, boolean isEnableUnderline) {
            this.extras = extras;
            this.color = color;
            this.isEnableUnderline = isEnableUnderline;
        }

        @Override
        public void onClick(View widget) {
            if (onClickableSpanListener != null) {
                onClickableSpanListener.onSpannableClick(extras);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (color != 0) {
                ds.setColor(color);
            }
            ds.setUnderlineText(isEnableUnderline);
        }
    }
}
