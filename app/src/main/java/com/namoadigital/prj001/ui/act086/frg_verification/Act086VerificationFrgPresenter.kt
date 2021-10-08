package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.Context
import android.content.Intent
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.MD_All_ProductDao
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceMaterial
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class Act086VerificationFrgPresenter(
    private val context: Context,
    private val mView: Act086VerificationFrgContract.I_View,
    private val hmAuxTrans: HMAux,
    private val deviceItemDao: GeOsDeviceItemDao
): Act086VerificationFrgContract.I_Presenter {

    override fun handleAddPhoto(
        prefixPhoto: String,
        photoList: MutableList<String>,
        photoLimit: Int
    ) {
        if(photoList.size < photoLimit){
            mView.callCameraAct(photoName = getPhotoName(prefixPhoto), newPhoto = true)
        }else{
            mView.showAlertFrg(
                ttl = hmAuxTrans["alert_photo_limit_reached_ttl"],
                msg = hmAuxTrans["alert_photo_limit_reached_msg"]
            )
        }
    }

    private fun getPhotoName(prefixPhoto: String): String {
        return "$prefixPhoto${
            ToolBox.dateToMilliseconds(
            ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
        )}.png"
    }

    override fun reviewPhotoExists(photoList: MutableList<String>) {
        val filter = photoList.filter { photo ->
            File(ConstantBaseApp.CACHE_PATH_PHOTO, photo).exists()
        }
        photoList.clear()
        photoList.addAll(filter)
        mView.updatePhotoListIntoAdapter()
    }

    override fun prepareCallProductAct(materialInputList: MutableList<Act086MaterialItem>) {
        val listOfProduct = materialInputList.map {
            it.productCode
        } as ArrayList<Int>
        //
        mView.callProductAct(listOfProduct)
    }

    override fun processProductSelecionResult(data: Intent?) {
        data?.extras?.let{
            val act086ProductItem = Act086MaterialItem(
                it.getInt(MD_All_ProductDao.PRODUCT_CODE),
                it.getString(MD_All_ProductDao.PRODUCT_ID, ""),
                it.getString(MD_All_ProductDao.PRODUCT_DESC, ""),
                it.getString(MD_All_ProductDao.UN, ""),
                creationMs = Calendar.getInstance().timeInMillis
            )
            //
            mView.addProductToListAndShowDialog(act086ProductItem)
        }
    }

    override fun getGeOsDeviceMaterialList(
        geOsDeviceItem: GeOsDeviceItem,
        materialFragList: MutableList<Act086MaterialItem>
    ) {
        val newMaterialItemList = materialFragList.map {
            GeOsDeviceMaterial(
                geOsDeviceItem.customer_code,
                geOsDeviceItem.custom_form_type,
                geOsDeviceItem.custom_form_code,
                geOsDeviceItem.custom_form_version,
                geOsDeviceItem.custom_form_data,
                geOsDeviceItem.product_code,
                geOsDeviceItem.serial_code,
                geOsDeviceItem.device_tp_code,
                geOsDeviceItem.item_check_code,
                geOsDeviceItem.item_check_seq,
                it.productCode,
                it.productId,
                it.productDesc,
                it.productQty.toFloat(),
                it.productUnit,
                it.creationMs
            )
        }
        //
        geOsDeviceItem.materialList.clear()
        geOsDeviceItem.materialList.addAll(newMaterialItemList)
    }

    override fun updateDeviceItemIntoBd(geOsDeviceItem: GeOsDeviceItem) {
        val daoObjReturn = deviceItemDao.addUpdate(geOsDeviceItem)
        if(daoObjReturn.hasError()){
            //O QUE FAZER?
            ToolBox.toastMSG(
                context,
                hmAuxTrans["Erro ao salvar"]
            )
        }
    }

    override fun buildAdapterMaterialFragList(
        materialList: MutableList<GeOsDeviceMaterial>,
        materialFragList: MutableList<Act086MaterialItem>
    ) {
        materialList.forEach {
            materialFragList.add(
                Act086MaterialItem(
                    it.material_code,
                    it.material_id,
                    it.material_desc,
                    it.material_unit?:"",
                    it.material_qty.toInt(),
                    it.creation_ms
                )
            )
        }
    }

    override fun deleteOldPhoto(prefixPhoto: String){
        ToolBox_Inf.deleteFileListExceptionSafe(ConstantBaseApp.CACHE_PATH_PHOTO,prefixPhoto)
    }
}