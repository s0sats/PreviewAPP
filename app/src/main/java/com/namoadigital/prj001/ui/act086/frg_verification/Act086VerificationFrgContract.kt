package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.DialogInterface
import android.content.Intent
import com.namoadigital.prj001.model.Act086ProductItem

interface Act086VerificationFrgContract {
    interface I_View{
        fun callCameraAct(photoName: String, newPhoto: Boolean)
        fun updatePhotoListIntoAdapter()
        fun callProductAct(listOfProduct: ArrayList<Int>)
        fun addProductToListAndShowDialog(productItem: Act086ProductItem)
        fun showAlert(
            ttl: String?,
            msg: String?,
            positeClickListener: DialogInterface.OnClickListener? = null,
            negativeBtn: Int = 0
        )
    }

    interface I_Presenter{
        fun deleteOldPhoto(prefixPhoto: String)
        fun handleAddPhoto(prefixPhoto: String, photoList: MutableList<String>, photoLimit: Int)
        fun prepareCallProductAct(productInputList: MutableList<Act086ProductItem>)
        fun reviewPhotoExists(photoList: MutableList<String>)
        fun processProductSelecionResult(data: Intent?)
    }

}