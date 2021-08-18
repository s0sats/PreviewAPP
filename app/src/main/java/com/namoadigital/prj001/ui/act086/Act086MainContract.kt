package com.namoadigital.prj001.ui.act086

import android.content.DialogInterface
import android.content.Intent
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.model.Act086ProductItem

interface Act086MainContract {

    interface I_View{
        fun callCameraAct(photoName: String, newPhoto: Boolean)
        fun showAlert(
            ttl: String?,
            msg: String?,
            positeClickListener: DialogInterface.OnClickListener? = null,
            negativeBtn: Int = 0
        )
        fun updatePhotoListIntoAdapter()
        fun callProductAct(listOfProduct: ArrayList<Int>)
        fun addProductToListAndShowDialog(productItem: Act086ProductItem)
    }

    interface I_Presenter{
        fun getTranslation() : HMAux
        fun handleAddPhoto(prefixPhoto: String, photoList: MutableList<String>, photoLimit: Int)
        fun reviewPhotoExists(photoList: MutableList<String>)
        fun deleteOldPhoto(prefixPhoto: String)
        fun prepareCallProductAct(productInputList: MutableList<Act086ProductItem>)
        fun processProductSelecionResult(data: Intent?)
    }
}