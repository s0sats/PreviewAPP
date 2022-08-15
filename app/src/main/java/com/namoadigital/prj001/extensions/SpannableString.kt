package com.namoadigital.prj001.extensions

import android.content.res.Resources
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan


object SpannableStringStyle {

    var customText: List<String?>? = null

    fun spanStyleWith(text: String, block: SpannableString.() -> Unit) : SpannableString {
        return SpannableString(text).also { block(it) }
    }


    inline fun SpannableString.applyColor(color: () -> Int){
        customText?.forEach { text ->
            text?.let {
                setSpan(
                    ForegroundColorSpan(color()),
                    (this.indexOf(it)),
                    (this.indexOf(it) + it.length),
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
    }

    inline fun SpannableString.fontSize(textSize: () -> Float){
        val size = textSize()
        customText?.forEach { text ->
            text?.let {
                setSpan(
                    RelativeSizeSpan(size),
                    (this.indexOf(it)),
                    (this.indexOf(it) + it.length),
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
    }

    inline fun SpannableString.textStyle(textStyle: () -> Int){
        customText?.forEach { text ->
            text?.let {
                setSpan(
                    StyleSpan(textStyle()),
                    (this.indexOf(it)),
                    (this.indexOf(it) + it.length),
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
    }

}

