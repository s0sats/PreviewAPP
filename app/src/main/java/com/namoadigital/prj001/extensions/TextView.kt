package com.namoadigital.prj001.extensions

import android.content.res.ColorStateList
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.namoadigital.prj001.R
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable

/**
 * Adiciona * laranja indicando que o campo é required
 */
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

/**
 * Add o texto passado como prefixo no texto da view.
 */
fun TextView.setPrefix(preffix: String){
    if(!this.text.startsWith(preffix)){
        this.text = preffix.plus(this.text)
    }
}

/**
 * Remove o texto passado como prefixo da view.
 */
fun TextView.removePrefix(preffix: String){
    if(this.text.startsWith(preffix)){
        this.text = this.text.toString().substring(preffix.lastIndex)
    }
}

fun TextView.applyVisibilityIfTextExists(value: String?) {
    this.visibility = if (value.isNullOrEmpty()) {
        View.GONE
    } else {
        this.text = value
        View.VISIBLE
    }
}

fun TextView.applyVisibilityIfTextExists(text: String?, lbl: String) {
    this.visibility = if (text.isNullOrEmpty()) {
        View.GONE
    } else {
        this.text = "$lbl: $text"
        View.VISIBLE
    }
}

fun TextView.highlightItem(cnt: Int?, color: Int) {
    val highlightItem = cnt?.let {
        it >= 1
    } ?: false
    if (highlightItem) {
        setTextColor(ColorStateList.valueOf(resources.getColor(color)))
    } else {
        setTextColor(ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_outlineVariant)))
    }
}

fun TextView.setLateColor(todayCnt: Int?, lateCnt:Int?) {
    if ((todayCnt ?: 0) == 0) {
        highlightItem(lateCnt, R.color.namoa_destination_tag_late)
    } else {
        highlightItem(lateCnt, R.color.m3_namoa_onSurfaceVariant)
    }
}

fun TextView.setNextColor(todayCnt: Int?, lateCnt:Int?, nextCnt: Int?) {
    if ((todayCnt ?: 0) == 0
        && (lateCnt ?: 0) == 0
    ) {
        highlightItem(nextCnt, R.color.namoa_destination_label_future)
    } else {
        highlightItem(nextCnt, R.color.m3_namoa_onSurfaceVariant)
    }
}