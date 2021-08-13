package com.namoadigital.prj001.ui.act086

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act086MainPresenter(
    private val context: Context,
    private val mView: Act086MainContract.I_View,
    private val bundle: Bundle,
    private val mModule_Code: String,
    private val mResource_Code: String
) : Act086MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    override fun getTranslation() = hmAuxTrans

    private fun loadTranslation() : HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act086_title",
            "dialog_user_search_ttl",
            "dialog_user_search_start",
            "workgroup_edit_ttl",
            "workgroup_edit_start",
            "workgroup_member_list_ttl",
            "workgroup_member_list_start",
            "alert_workgroup_list_not_found_tll",
            "alert_workgroup_list_not_found_msg",
            "alert_leave_without_save_ttl",
            "alert_leave_without_save_confirm",
            "alert_leave_remove_workgroup_ttl",
            "alert_leave_remove_workgroup_confirm"
        )
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }

    override fun handleAddPhoto(photoList: MutableList<String>, photoLimit: Int) {
        if(photoList.size <= photoLimit){
            mView.callCameraAct(photoIdx = photoList.size + 1, newPhoto = true)
        }else{
            mView.showAlert(
                ttl = hmAuxTrans["alert_photo_limit_reached_ttl"],
                msg = hmAuxTrans["alert_photo_limit_reached_msg"]
            )
        }
    }

    override fun reviewPhotoExists(photoList: MutableList<String>) {
        val filter = photoList.filter { photo ->
            File(ConstantBaseApp.CACHE_PATH_PHOTO, photo).exists()
        }
        photoList.clear()
        photoList.addAll(filter)
    }
}