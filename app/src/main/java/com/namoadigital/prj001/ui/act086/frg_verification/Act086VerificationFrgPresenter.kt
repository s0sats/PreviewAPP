package com.namoadigital.prj001.ui.act086.frg_verification

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GeOsDao
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.MD_All_ProductDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_Device_ItemDao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceMaterial
import com.namoadigital.prj001.model.masterdata.ge_os.toUiMaterialItem
import com.namoadigital.prj001.sql.GeOsSql_001
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import java.util.*

class Act086VerificationFrgPresenter(
    private val context: Context,
    private val mView: Act086VerificationFrgContract.I_View,
    private val hmAuxTrans: HMAux,
    private val geOsDao: GeOsDao,
    private val deviceItemDao: GeOsDeviceItemDao,
    private val mdProductSerialTpDeviceItemDao: MD_Product_Serial_Tp_Device_ItemDao
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

    override fun processProductSelecionResult(
        data: Intent?,
        geOsMaterialList: MutableList<GeOsDeviceMaterial>
    ) {
        data?.extras?.let{
            val plannedMaterialItem = geOsMaterialList.find { materialItem ->
                materialItem.material_code == it.getInt(MD_All_ProductDao.PRODUCT_CODE)
            }
            //
            val act086ProductItem =
                //Se item planejado, gera item a partir dele, se null, gera item como insumo nao previsto
                plannedMaterialItem?.toUiMaterialItem()?.apply {
                    materialPlannedUsed = 1
                } ?: Act086MaterialItem(
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

    /**
     * Fun que transforma lista de materal da tela, para lista de obj do banco.
     * Add filtro para que os itens, com valor 0 nao sejam incluidos.
     */
    override fun getGeOsDeviceMaterialList(
        geOsDeviceItem: GeOsDeviceItem,
        materialFragList: MutableList<Act086MaterialItem>
    ) {
        val newMaterialItemList = materialFragList.filter {
            it.productQty > 0f || it.materialPlanned == 1
        }.map {
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
                it.creationMs,
                it.materialPlanned,
                it.materialPlannedUsed,
                it.materialPlannedQty,
                it.origin
            )
        }.toMutableList()
        //Copia itens planjeados para nova lista evitando perde-los no map.
        geOsDeviceItem.materialList.filter {
            it.material_planned == 1 && isDbPlannedMaterialNotListed(it.material_code,newMaterialItemList)
        }.forEach {
            newMaterialItemList.add(it)
        }
        //Limpa
        geOsDeviceItem.materialList.clear()
        //Adiciona nova lista com os itens que planejados que ja estavam aqui
        geOsDeviceItem.materialList.addAll(newMaterialItemList)
    }

    /**
     * Fun que verifica se o insumo do db não existe na lista da u.i
     */
    private fun isDbPlannedMaterialNotListed(
        materialCode: Int,
        newMaterialItemList: MutableList<GeOsDeviceMaterial>
    ): Boolean {
       return !newMaterialItemList.any {
           it.material_code == materialCode
       }
    }

    override fun updateDeviceItemIntoBd(geOsDeviceItem: GeOsDeviceItem) {
        val daoObjReturn = deviceItemDao.addUpdate(geOsDeviceItem)
        if(daoObjReturn.hasError()){
            //O QUE FAZER?
            // TODO O QUE FAZER
            ToolBox.toastMSG(
                context,
                hmAuxTrans["alert_error_on_save_item_msg"]
            )
        }
    }

    override fun buildAdapterMaterialFragList(
        materialList: MutableList<GeOsDeviceMaterial>,
        materialFragList: MutableList<Act086MaterialItem>
    ) {
        materialFragList.clear()
        //Com o advento do material planejado, foi aplicado filtrao para remover
        //insumos planejados sem "uso"  da lista exibida.
        materialList.filter{
            it.material_planned == 0 || it.material_planned_used == 1
        }.forEach {
            materialFragList.add(
                Act086MaterialItem(
                    it.material_code,
                    it.material_id,
                    it.material_desc,
                    it.material_unit?:"",
                    it.material_qty,
                    it.creation_ms,
                    it.material_planned,
                    it.material_planned_used,
                    it.material_planned_qty,
                    it.origin
                )
            )
        }
    }

    override fun deleteManualItem(geOsDeviceItem: GeOsDeviceItem) {

        val daoObjReturn = deviceItemDao.removeFull(geOsDeviceItem)
        if(!daoObjReturn.hasError()){
            mView.leaveWithoutSave()
        }else{
            mView.showAlertFrg(
                hmAuxTrans["alert_manual_item_delete_ttl"],
                hmAuxTrans["alert_error_on_manual_item_delete_msg"],
                DialogInterface.OnClickListener { dialog, which ->
                    mView.leaveWithoutSave()
                }
            )
        }
    }

    override fun hasMaterialPlanned(geOsDeviceItem: GeOsDeviceItem): Boolean {
       return geOsDeviceItem.materialList.any {
           it.material_planned == 1
       }
    }

    /**
     * Fun que reseta a flag material_planned_used para 0
     */
    override fun resetMaterialPlanned(
        materialList: MutableList<GeOsDeviceMaterial>,
        materialUIItem: Act086MaterialItem
    ) {
        //Busca na lista o material planejado apagado e material_planned_used para 0
        materialList.find {
            it.material_code == materialUIItem.productCode
        }?.let {
            resetMaterialPlannedMutableInfo(it)
        }
    }

    override fun getFormattedLastMeasureInfo(
        lastFixed: Float,
        measureValueSufix: String?
    ): String {
        return "${ToolBox_Inf.convertFloatToBigDecimalString(
            lastFixed,true
        )}${if(measureValueSufix != null) " ".plus(measureValueSufix) else ""}"
    }

    /**
     * Fun que reseta proprieades mutaveis pela u.i para estado original
     */
    private fun resetMaterialPlannedMutableInfo(it: GeOsDeviceMaterial) {
        it.material_planned_used = 0
        it.material_qty = 0f
    }

    /**
     * Fun que varre a lista de insumos previstos e reseta as proprieades mutaveis.
     */
    override fun resetMaterialPlannedList(materialList: MutableList<GeOsDeviceMaterial>) {
        materialList.forEach {
            if(it.material_planned == 1){
                resetMaterialPlannedMutableInfo(it)
            }
        }
    }

    override fun deleteOldPhoto(prefixPhoto: String){
        ToolBox_Inf.deleteFileListExceptionSafe(ConstantBaseApp.CACHE_PATH_PHOTO,prefixPhoto)
    }

    override fun getMaxMeasureValue(geOsDeviceItem: GeOsDeviceItem):Float{
        val geOs = geOsDao.getByString(
            GeOsSql_001(
                geOsDeviceItem.customer_code,
                geOsDeviceItem.custom_form_type,
                geOsDeviceItem.custom_form_code,
                geOsDeviceItem.custom_form_version,
                geOsDeviceItem.custom_form_data
            ).toSqlQuery()
        )
        return geOs?.maxMeasureValue()?:0f
    }
    override fun getGeOs(geOsDeviceItem: GeOsDeviceItem): GeOs {
        val geOs = geOsDao.getByString(
            GeOsSql_001(
                geOsDeviceItem.customer_code,
                geOsDeviceItem.custom_form_type,
                geOsDeviceItem.custom_form_code,
                geOsDeviceItem.custom_form_version,
                geOsDeviceItem.custom_form_data
            ).toSqlQuery()
        )
        return geOs!!
    }

}