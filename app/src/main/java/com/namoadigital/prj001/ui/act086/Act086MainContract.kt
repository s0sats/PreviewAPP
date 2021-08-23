package com.namoadigital.prj001.ui.act086

import android.content.DialogInterface
import com.namoa_digital.namoa_library.util.HMAux

interface Act086MainContract {

    interface I_View{
        fun showAlert(
            ttl: String?,
            msg: String?,
            positeClickListener: DialogInterface.OnClickListener? = null,
            negativeBtn: Int = 0
        )

        fun updateScrollPosition(newScrollTop: Int)
    }

    interface I_Presenter{
        fun getTranslation() : HMAux
        fun checkViewPositionIsVisible(
            viewBottomPosition: Int,
            heightToAdd: Int,
            scrollTop: Int,
            actionBarHeight: Int,
            footerHeight: Int
        )
    }
}