package com.keven.widget;

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

import com.keven.utils.DisplayUtils;

public class SpannableStringBuilderHelp {

    /**
     * 替换颜色
     *
     * @param builder
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder hightColor(SpannableStringBuilder builder, int color, int start, int end) {
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 设置文字字体
     *
     * @param builder
     * @param size
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder hightSize(SpannableStringBuilder builder, int size, int start, int end) {
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size,true);
        builder.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }

    /**
     * 增加下划�???
     *
     * @param builder
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder underline(SpannableStringBuilder builder, int start, int end) {
        CharacterStyle span = new UnderlineSpan();
        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 文字大小
     * @param dip  单位是否为
     */
    public static SpannableStringBuilder TextSize(SpannableStringBuilder builder,int size, boolean dip, int start, int end) {
        builder.setSpan(new AbsoluteSizeSpan(size ,dip), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 增加粗体
     *
     * @param builder
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder blod(SpannableStringBuilder builder, int start, int end) {
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 添加超链�???
     *
     * @param builder
     * @param url
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder urlspan(
            SpannableStringBuilder builder, String url, int start, int end) {
        builder.setSpan(new URLSpan(url), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 样式标记文本
     *
     * @param builder
     * @param type    Typeface类型 可为斜体和其他字�???
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder stylespan(
            SpannableStringBuilder builder, int type, int start, int end) {
        builder.setSpan(new StyleSpan(type), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 用删除线标记文本
     *
     * @param builder
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder strikethrough(
            SpannableStringBuilder builder, int start, int end) {
        builder.setSpan(new StrikethroughSpan(), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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