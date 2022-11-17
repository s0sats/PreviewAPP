package com.namoadigital.prj001.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

abstract class BaseActivityMvp<PRESENTER, BINDING : ViewBinding> : Base_Activity() {

    protected abstract val binding: BINDING
    protected abstract val presenter: PRESENTER


    fun showSnackbar(message: String = "Em manutenção", customView: Int? = null) {
        Snackbar.make(
            findViewById(customView ?: android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
            .setTextColor(resources.getColor(R.color.m3_namoa_surface))
            .setBackgroundTint(resources.getColor(R.color.m3_namoa_inverseSurface))
            .show()
    }


    protected fun iniUIFooter(act: String, hmAux: HMAux) {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = act
        mAct_Title = act + "_" + "title"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux)
        setMenuLanguage(hmAux)
        setFooter()
    }


    protected fun callAct(to: Class<*>, bundle: Bundle? = null) {
        Intent(context, to).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            bundle?.let { b ->
                it.putExtras(b)
            }
            startActivity(it)
            finish()
        }
    }
}