package com.keven.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

public class SpannableStringBuilderHelp {

    /**
     * 替换颜色
     */
    public static SpannableStringBuilder hightColor(String text, int color, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 设置文字字体
     */
    public static SpannableStringBuilder hightSize(String text, int size, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size,true);
        builder.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }

    /**
     * 增加下划线
     */
    public static SpannableStringBuilder underline(String text, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        CharacterStyle span = new UnderlineSpan();
        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 文字大小
     */
    public static SpannableStringBuilder TextSize(String text,int size, boolean dip, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new AbsoluteSizeSpan(size ,dip), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 增加粗体
     */
    public static SpannableStringBuilder blod(String text, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 添加超链
     */
    public static SpannableStringBuilder urlspan(String text, String url, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new URLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 样式标记文本
     * @param type    Typeface类型 可为斜体和其他字体
     */
    public static SpannableStringBuilder stylespan(String text, int type, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new StyleSpan(type), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 用删除线标记文本
     */
    public static SpannableStringBuilder strikethrough(String text, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 加图片文本
     */
    public static SpannableString inBoundDrawable(Context context, String text, int includePosition, int excludePosition,
                                                  @DrawableRes int drawableId, int widthDp, int heightDp) {
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new DynamicDrawableSpan(DynamicDrawableSpan.ALIGN_BASELINE) {
            @Override
            public Drawable getDrawable() {
                Drawable d = context.getResources().getDrawable(drawableId);
                d.setBounds(0, 0, DisplayUtils.dpToPx(widthDp), DisplayUtils.dpToPx(heightDp));
                return d;
            }
        }, includePosition, excludePosition, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }
}