package com.namoadigital.prj001.ui.act011

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class FormViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    var isSwipeEnabled = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isSwipeEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isSwipeEnabled && super.onInterceptTouchEvent(ev)
    }
}