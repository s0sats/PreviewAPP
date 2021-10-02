package com.namoadigital.prj001.extensions

import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.applyTintColor(color: Int){
    setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_ATOP)
}