package com.namoadigital.prj001.ui.act086

import com.namoa_digital.namoa_library.util.HMAux

interface Act086MainContract {

    interface I_View{
        fun callCameraAct(photoIdx: Int, newPhoto: Boolean)
        fun showAlert(ttl: String?, msg: String?)
    }

    interface I_Presenter{
        fun getTranslation() : HMAux
        fun handleAddPhoto(photoList: MutableList<String>, photoLimit: Int)
        fun reviewPhotoExists(photoList: MutableList<String>)
    }
}