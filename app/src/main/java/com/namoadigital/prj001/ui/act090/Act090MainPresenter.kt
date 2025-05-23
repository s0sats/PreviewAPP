package com.namoadigital.prj001.ui.act090

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.dao.GeOsDeviceMaterialDao
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceMaterial
import com.namoadigital.prj001.model.masterdata.ge_os.toUiMaterialItem
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_001
import com.namoadigital.prj001.sql.GeOsDeviceMaterialSql_003
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act090MainPresenter(
    private val context: Context,
    private val mView: Act090MainContract.IView,
    private val bundle: Bundle,
    private val mModule_Code: String,
    private val mResource_Code: String,
    private val geOsDeviceItemDao: GeOsDeviceItemDao,
    private val geOsDeviceItemMaterialDao: GeOsDeviceMaterialDao
) : Act090MainContract.IPresenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }
    //

    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act090_title",
            "planned_qty_lbl",
            "applied_qty_lbl",
            "request_qty_lbl",
            "alert_form_parameter_error_ttl",
            "alert_form_parameter_error_msg",
            "btn_apply_material",
            "empty_list_lbl",
            "alert_unsaved_data_will_be_lost_ttl",
            "alert_unsaved_data_will_be_lost_confirm",
            "alert_error_on_save_material_list_msg",
            "alert_no_data_changed_msg",
            "alert_invalid_material_qty_msg",
            "adjust_lbl",
            "change_lbl",
            "fixed_lbl",
            "still_with_problem_lbl",
            "has_problem_lbl"
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

    override fun getTranslation() = hmAuxTrans

    override fun validBundleParams(): Boolean {
        if (bundle.containsKey(ConstantBaseApp.DEVICE_BUNDLE)
            && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE) != null
            && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!
                .containsKey(ConstantBaseApp.DEVICE_ITEM_PK)
        ) {
            val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(
                ConstantBaseApp.DEVICE_ITEM_PK
            )
            deviceItemRawPk?.let {
                //Se tiver
                return try {
                    //Se tiver "." e o split tiver 10 elementos
                    it.contains(".") && validPkSize(it)
                } catch (e: Exception) {
                    ToolBox_Inf.registerException(javaClass.name, e)
                    false
                }
            }
        }
        return false
    }

    /**
     * Valida se o split possui o tamanho esperado: 10
     */
    private fun validPkSize(it: String): Boolean {
        return it.split(".").size == 10
    }

    override fun getDeviceItem(): GeOsDeviceItem? {
        val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(ConstantBaseApp.DEVICE_ITEM_PK)
        deviceItemRawPk?.let {
            try {
                val splitedPK = it.split(".")
                return geOsDeviceItemDao.getByString(
                        GeOsDeviceItem_Sql_001(
                            splitedPK[0],
                            splitedPK[1],
                            splitedPK[2],
                            splitedPK[3],
                            splitedPK[4],
                            splitedPK[5],
                            splitedPK[6],
                            splitedPK[7],
                            splitedPK[8],
                            splitedPK[9]
                        ).toSqlQuery()
                )
            }catch (e: Exception){
                ToolBox_Inf.registerException(javaClass.name,e)
            }
        }
        return null
    }

    /**
     * Fun que resgata itens planejados da tabela.
     */
    override fun getGeOsDeviceMaterialList(geOsDeviceMaterial: MutableList<GeOsDeviceMaterial>) {
        val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!
            .getString(ConstantBaseApp.DEVICE_ITEM_PK)
        deviceItemRawPk?.let {
            try {
                val splitedPK = it.split(".")
                val plannedMaterialList = geOsDeviceItemMaterialDao.query(
                    GeOsDeviceMaterialSql_003(
                        splitedPK[0],
                        splitedPK[1],
                        splitedPK[2],
                        splitedPK[3],
                        splitedPK[4],
                        splitedPK[5],
                        splitedPK[6],
                        splitedPK[7],
                        splitedPK[8],
                        splitedPK[9]
                    ).toSqlQuery()
                )
                //
                if (plannedMaterialList.isNotEmpty()) {
                    geOsDeviceMaterial.clear()
                    //
                    geOsDeviceMaterial.addAll(
                        plannedMaterialList
                    )
                }
            } catch (e: Exception) {
                ToolBox_Inf.registerException(javaClass.name, e)
            }
        }
    }

    /**
     * Fun que transform obj do banco em obj de tela.
     */
    override fun getItemPlannedMaterialList(
        geOsDeviceMaterial: MutableList<GeOsDeviceMaterial>,
        itemPlannedMaterialList: MutableList<Act086MaterialItem>
    ) {
        itemPlannedMaterialList.clear()
        //
        geOsDeviceMaterial.forEach {
            itemPlannedMaterialList.add(
                it.toUiMaterialItem()
            )
        }
    }

    override fun hasAnyItemChanged(
        geOsDeviceMaterial: List<GeOsDeviceMaterial>,
        itemPlannedMaterialList: List<Act086MaterialItem>
    ): Boolean {
        //Se listas tam diferente retorna falso.
        //Logicamente isso não deve acontecer, mas.....
        if (geOsDeviceMaterial.size != itemPlannedMaterialList.size) {
            return false
        }
        //valida item a item de houve mudança e para na primeira que encontrar
        geOsDeviceMaterial.forEachIndexed { index, dbMaterial ->
            val itemMaterialUI = itemPlannedMaterialList[index]
            //
            if (itemMaterialUI.materialPlannedUsed != dbMaterial.material_planned_used
                || itemMaterialUI.productQty != dbMaterial.material_qty
            ) {
                return true
            }
        }
        //
        return false
    }

    override fun savePlannedMaterialChangesIntoDb(
        geOsDeviceMaterial: GeOsDeviceMaterial,
        itemPlannedMaterialList: MutableList<Act086MaterialItem>
    ) {
        val geOsDeviceMaterialList = itemPlannedMaterialList.map {
                GeOsDeviceMaterial(
                    geOsDeviceMaterial.customer_code,
                    geOsDeviceMaterial.custom_form_type,
                    geOsDeviceMaterial.custom_form_code,
                    geOsDeviceMaterial.custom_form_version,
                    geOsDeviceMaterial.custom_form_data,
                    geOsDeviceMaterial.product_code,
                    geOsDeviceMaterial.serial_code,
                    geOsDeviceMaterial.device_tp_code,
                    geOsDeviceMaterial.item_check_code,
                    geOsDeviceMaterial.item_check_seq,
                    it.productCode,
                    it.productId,
                    it.productDesc,
                    it.productQty,
                    it.productUnit,
                    it.creationMs,
                    it.materialPlanned,
                    it.materialPlannedUsed,
                    it.materialPlannedQty,
                    it.origin
                )
        }.toMutableList()
        //
        val daoObjReturn = geOsDeviceItemMaterialDao.addUpdate(geOsDeviceMaterialList, false)
        //
        if(!daoObjReturn.hasError()){
            onBackPressedClicked(true)
        }else{
            ToolBox.toastMSG(
                context,
                hmAuxTrans["alert_error_on_save_material_list_msg"]
            )
        }
    }

    override fun onBackPressedClicked(skipBackValidation: Boolean) {
        if(skipBackValidation) {
            mView.callAct086()
        }else{
            ToolBox.alertMSG_YES_NO(
                context,
                hmAuxTrans["alert_unsaved_data_will_be_lost_ttl"],
                hmAuxTrans["alert_unsaved_data_will_be_lost_confirm"],
                DialogInterface.OnClickListener { _, _ ->
                    onBackPressedClicked(true)
                },
                1
            )
        }
    }
}