package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.Context
import android.content.Intent
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.MD_All_ProductDao
import com.namoadigital.prj001.model.Act086ProductItem
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act086VerificationFrgPresenter(
    private val context: Context,
    private val mView: Act086VerificationFrgContract.I_View,
    private val hmAuxTrans : HMAux
): Act086VerificationFrgContract.I_Presenter {

    override fun handleAddPhoto(
        prefixPhoto: String,
        photoList: MutableList<String>,
        photoLimit: Int
    ) {
        if(photoList.size < photoLimit){
            mView.callCameraAct(photoName = getPhotoName(prefixPhoto), newPhoto = true)
        }else{
            mView.showAlert(
                ttl = hmAuxTrans["alert_photo_limit_reached_ttl"],
                msg = hmAuxTrans["alert_photo_limit_reached_msg"]
            )
        }
    }

    private fun getPhotoName(prefixPhoto: String): String {
        return "$prefixPhoto${
            ToolBox.dateToMilliseconds(
            ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
        )}"
    }

    override fun reviewPhotoExists(photoList: MutableList<String>) {
        val filter = photoList.filter { photo ->
            File(ConstantBaseApp.CACHE_PATH_PHOTO, photo).exists()
        }
        photoList.clear()
        photoList.addAll(filter)
        mView.updatePhotoListIntoAdapter()
    }

    override fun prepareCallProductAct(productInputList: MutableList<Act086ProductItem>) {
        val listOfProduct = productInputList.map {
            it.productCode
        } as ArrayList<Int>
        //
        mView.callProductAct(listOfProduct)
    }

    override fun processProductSelecionResult(data: Intent?) {
        data?.extras?.let{
            val act086ProductItem = Act086ProductItem(
                it.getInt(MD_All_ProductDao.PRODUCT_CODE),
                it.getString(MD_All_ProductDao.PRODUCT_ID, ""),
                it.getString(MD_All_ProductDao.PRODUCT_DESC, ""),
                it.getString(MD_All_ProductDao.UN, "")
            )
            //
            mView.addProductToListAndShowDialog(act086ProductItem)
        }
    }

    override fun deleteOldPhoto(prefixPhoto: String){
        ToolBox_Inf.deleteFileListExceptionSafe(ConstantBaseApp.CACHE_PATH_PHOTO,prefixPhoto)
    }
}