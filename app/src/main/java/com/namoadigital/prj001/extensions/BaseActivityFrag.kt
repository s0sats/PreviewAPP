package com.namoadigital.prj001.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag

fun <T : Fragment?> Base_Activity_Frag.setFrag(type: T, sTag: String,@IdRes placeHolderId: Int, replaceEvenCreated: Boolean = false, addToBackStack: Boolean = true){
    if (replaceEvenCreated || this.supportFragmentManager.findFragmentByTag(sTag) == null) {
        val ft = this.supportFragmentManager.beginTransaction()
        ft.replace(placeHolderId, type as Fragment, sTag)
        if(addToBackStack) {
            ft.addToBackStack(sTag)
        }
        ft.commit()
    }
}
