package com.namoadigital.prj001.ui.act079.view

import android.content.Context
import androidx.appcompat.widget.LinearLayoutCompat
import com.namoadigital.prj001.model.TkTicketOriginNc

abstract class Act079ViewNcBase(
    context: Context,
    protected val nc: TkTicketOriginNc
): LinearLayoutCompat(context){
    var mSequence : Int = -1
    interface onFieldClickListener{
        fun onFieldClick(itemPositionIdx: Int)
        fun onGalleryClick(imageNames: String)
    }
}