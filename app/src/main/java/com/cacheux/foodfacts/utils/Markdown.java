package com.cacheux.foodfacts.utils;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

/**
 * Convert strings with markdown into {@link Spanned} with bold italic
 * This is not a full implementation of markdown notation but it's enough for our needs here
 *
 * <p>Example :</p>
 *
 * <p>Chocolate _milk_</p>
 *
 * <p>will become :</p>
 *
 * <p>Chocolate <b><i>milk</i></b></p>
 */
public class Markdown {

    private Markdown() {
    }

    public static Spanned parseMarkdown(String input) {
        String[] parts = input.split("_");
        SpannableStringBuilder builder = new SpannableStringBuilder();
        boolean isMarked = false;
        for (String part : parts) {
            if (isMarked) {
                SpannableString str = new SpannableString(part);
                str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, str.length(), 0);
                builder.append(str);
            } else {
                builder.append(part);
            }
            isMarked = !isMarked;
        }

        return builder;
    }
}
