package com.namoadigital.prj001.extensions

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun ImageView.applyTintColor(color: Int){
    setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_ATOP)
}

/**
 * Kotlin extension para ImageView que seta o source e visibilidade caso exista.
 */
fun ImageView.applyVisibilityIfSourceExists(@DrawableRes source: Int?){
    this.visibility = if (source != null){
        setImageDrawable(ContextCompat.getDrawable(this.context, source))
        View.VISIBLE
    }else{
        View.GONE
    }
}