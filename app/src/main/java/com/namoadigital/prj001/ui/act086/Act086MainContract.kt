package com.namoadigital.prj001.ui.act086

import android.content.DialogInterface
import androidx.fragment.app.FragmentManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceItemHist

interface Act086MainContract {

    interface I_View{
        fun showAlert(
            ttl: String?,
            msg: String?,
            positeClickListener: DialogInterface.OnClickListener? = null,
            negativeBtn: Int = 0
        )

        fun updateScrollPosition(newScrollTop: Int)
        fun callAct011()
        fun popToVerificationFrag()
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

        fun getPrefixPhoto(
            deviceItem: GeOsDeviceItem
        ): String
        fun validBundleParams(isNewVerification: Boolean): Boolean
        fun getDeviceItem(newVerification: Boolean): GeOsDeviceItem?
        fun onBackPressedClicked(
            supportFragmentManager: FragmentManager,
            deviceItem: GeOsDeviceItem
        )
        fun getDeviceItemHist(isNewVerification: Boolean):  ArrayList<GeOsDeviceItemHist>?
    }
}