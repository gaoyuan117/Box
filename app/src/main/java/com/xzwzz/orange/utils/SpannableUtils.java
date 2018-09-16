package com.xzwzz.orange.utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;

/*
 * @Project_Name :Securities
 * @package_Name :com.xzwzz.securities.util
 * @AUTHOR      :xzwzz@vip.qq.com
 * @DATE        :2018/5/18
 */
public class SpannableUtils {
    static SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

    public static SpannableStringBuilder showDiffColor(String str, int start, int end, @ColorInt int color) {
        if (spannableStringBuilder.length() > 0) {
            spannableStringBuilder.clear();
        }
        spannableStringBuilder.append(str);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(color), start,
                end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableStringBuilder;
    }

    public static SpannableStringBuilder showDiffSize(String str, int start, int end, int size) {
        if (spannableStringBuilder.length() > 0) {
            spannableStringBuilder.clear();
        }
        spannableStringBuilder.append(str);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableStringBuilder;
    }

    public static SpannableStringBuilder showDiffStyle(String str, int start, int end, Context context, @StyleRes int styles) {
        if (spannableStringBuilder.length() > 0) {
            spannableStringBuilder.clear();
        }
        spannableStringBuilder.append(str);
        spannableStringBuilder.setSpan(new TextAppearanceSpan(context, styles), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }
}
