package com.namoadigital.prj001.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.namoadigital.prj001.R

fun TextView.setAsRequired(required: Boolean = true) {
    val symbol = " *"
    if(this.text.subSequence(this.text.lastIndex,this.text.length).toString() != "*"){
       if(required){
           val finalText = this.text.toString().plus(symbol)
           val spannableString = SpannableString(finalText)
           //
           this.text = spannableString.apply {
               setSpan(
                   ForegroundColorSpan(ContextCompat.getColor(context,R.color.font_required)),
                   finalText.length - symbol.length,
                   finalText.length,
                   Spanned.SPAN_INCLUSIVE_INCLUSIVE
               )
           }
       }
    }else{
        if(!required){
            var lastValidIndex = 0
            this.text.toString().lastIndexOf(symbol).let {
                lastValidIndex = if(it != -1) it else this.text.lastIndex
            }
            this.text = this.text.subSequence(0,lastValidIndex).toString()
        }
    }
}