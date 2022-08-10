package com.namoadigital.prj001.extensions

import android.content.res.Resources
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan


object SpannableStringStyle {

    var customText = ""

    fun spanStyleWith(text: String, block: SpannableString.() -> Unit) : SpannableString {
        return SpannableString(text).also { block(it) }
    }


    inline fun SpannableString.applyColor(color: () -> Int){
            setSpan(
                ForegroundColorSpan(color()),
                (this.indexOf(customText)),
                (this.indexOf(customText) + customText.length),
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )
    }

    inline fun SpannableString.fontSize(textSize: () -> Float){
        val size = textSize()
        setSpan(
            RelativeSizeSpan(size),
            (this.indexOf(customText)),
            (this.indexOf(customText) + customText.length),
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
    }

    inline fun SpannableString.textStyle(textStyle: () -> Int){
        setSpan(
            StyleSpan(textStyle()),
            (this.indexOf(customText)),
            (this.indexOf(customText) + customText.length),
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
    }

}

