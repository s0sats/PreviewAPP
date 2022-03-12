package com.namoadigital.prj001.ui.act090

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.GeOsDeviceMaterialDao
import com.namoadigital.prj001.model.GeOsDeviceMaterial
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
    private val geOsDeviceItemMaterialDao: GeOsDeviceMaterialDao
): Act090MainContract.IPresenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }
    //

    private fun loadTranslation() : HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act090_title",
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
        if( bundle.containsKey(ConstantBaseApp.DEVICE_BUNDLE)
            && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE) != null
            && bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.containsKey(ConstantBaseApp.DEVICE_ITEM_PK)
        ){
            val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(
                ConstantBaseApp.DEVICE_ITEM_PK)
            deviceItemRawPk?.let {
                //Se tiver
                return try {
                    //Se tiver "." e o split tiver 10 elementos
                    it.contains(".")  && validPkSize( it)
                }catch (e: Exception){
                    ToolBox_Inf.registerException(javaClass.name,e)
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

    override fun getItemPlannedMaterialList(itemPlannedMaterialList: MutableList<GeOsDeviceMaterial>) {
        val deviceItemRawPk = bundle.getBundle(ConstantBaseApp.DEVICE_BUNDLE)!!.getString(ConstantBaseApp.DEVICE_ITEM_PK)
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
                if(plannedMaterialList.isNotEmpty()){
                    itemPlannedMaterialList.clear()
                    //
                    itemPlannedMaterialList.addAll(
                        plannedMaterialList
                    )
                }
            }catch (e: Exception){
                ToolBox_Inf.registerException(javaClass.name,e)
            }
        }
    }
}