package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.DialogInterface
import android.content.Intent
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceMaterial

interface Act086VerificationFrgContract {
    interface I_View{
        fun callCameraAct(photoName: String, newPhoto: Boolean)
        fun updatePhotoListIntoAdapter()
        fun callProductAct(listOfProduct: ArrayList<Int>)
        fun addProductToListAndShowDialog(materialItem: Act086MaterialItem)
        fun showAlertFrg(
            ttl: String?,
            msg: String?,
            positeClickListener: DialogInterface.OnClickListener? = null,
            negativeBtn: Int = 0
        )

        fun leaveWithoutSave()
    }

    interface I_Presenter{
        fun deleteOldPhoto(prefixPhoto: String)
        fun handleAddPhoto(prefixPhoto: String, photoList: MutableList<String>, photoLimit: Int)
        fun prepareCallProductAct(materialInputList: MutableList<Act086MaterialItem>)
        fun reviewPhotoExists(photoList: MutableList<String>)
        fun processProductSelecionResult(data: Intent?)
        fun getGeOsDeviceMaterialList(
            geOsDeviceItem: GeOsDeviceItem,
            materialFragList: MutableList<Act086MaterialItem>
        )
        fun updateDeviceItemIntoBd(geOsDeviceItem: GeOsDeviceItem)
        fun buildAdapterMaterialFragList(
            materialList: MutableList<GeOsDeviceMaterial>,
            materialFragList: MutableList<Act086MaterialItem>
        )

        fun deleteManualItem(geOsDeviceItem: GeOsDeviceItem)
    }

}