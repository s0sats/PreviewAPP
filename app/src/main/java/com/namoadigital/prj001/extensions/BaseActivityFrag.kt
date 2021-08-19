package com.namoadigital.prj001.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag

fun <T : BaseFragment?> Base_Activity_Frag.setFrag(type: T, sTag: String,@IdRes placeHolderId: Int){
    if (this.supportFragmentManager.findFragmentByTag(sTag) == null) {
        val ft = this.supportFragmentManager.beginTransaction()
        ft.replace(placeHolderId, type as Fragment, sTag)
        ft.addToBackStack(sTag)
        ft.commit()
    }
}