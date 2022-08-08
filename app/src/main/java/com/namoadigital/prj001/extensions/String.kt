package com.namoadigital.prj001.extensions

import android.content.res.Resources
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan

fun String.changeTextWithStringBuild(
    indexOf: String = this,
    color: Int? = null,
    textSize: Float? = null,
    textStyle: Int? = null,
    resources: Resources,
) : SpannableString {
    val label = this
    val start = (label.indexOf(indexOf))
    val end = (label.indexOf(indexOf) + indexOf.length)
    return SpannableString(label).apply {
        textStyle?.let {
            setSpan(
                StyleSpan(it),
                start,
                end,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        textSize?.let {
            setSpan(
                RelativeSizeSpan(it),
                start,
                end,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        color?.let {
            setSpan(
                ForegroundColorSpan(resources.getColor(it)),
                start,
                end,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )
        }

    }
}

