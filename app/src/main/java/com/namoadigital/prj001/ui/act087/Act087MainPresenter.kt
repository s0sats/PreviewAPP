package com.namoadigital.prj001.ui.act087

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act087MainPresenter(
    private val context: Context,
    private val mView: Act087MainContract.I_View,
    private val mModule_Code: String,
    private val mResource_Code: String,
    private val productDao: MD_ProductDao,
    private val serialDao: MD_Product_SerialDao,
    private val formDao: GE_Custom_FormDao,
    private val geOsDao: GeOsDao,
    private val orderTypeDao: MdOrderTypeDao,
    private val measureTpDao: MeMeasureTpDao
): Act087MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    private lateinit var serialObj : MD_Product_Serial


    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act087_title"
        )
        transList.addAll(FormOsHeaderFrg.getFragTranslationsVars())
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }

    override fun getTranslation(): HMAux {
        return hmAuxTrans
    }

    override fun getSerialInfo(
        productCode: Int,
        serialId: String,
        serialCode: Int
    ): MD_Product_Serial {
        if(!::serialObj.isInitialized){
            serialObj = serialDao.getByString(
                MD_Product_Serial_Sql_002(
                   ToolBox_Con.getPreference_Customer_Code(context),
                    productCode.toLong(),
                    serialId
                ).toSqlQuery()
            )
        }
        return serialObj

    }

    override fun getProductIcon(productCode: Int): Bitmap? {
        getProductInfo(productCode)?.let {
            if (!it.product_icon_name.isNullOrEmpty()) {
                if (ToolBox_Inf.verifyDownloadFileInf(it.product_icon_name, Constant.CACHE_PATH)) {
                    val imgFile = File(Constant.CACHE_PATH + "/" + it.product_icon_name)
                    if (imgFile.exists()) {
                        return BitmapFactory.decodeFile(imgFile.absolutePath)
                    }
                }
            }
        }
        return null
    }

    private fun getProductInfo(productCode: Int) = productDao.getByString(
        MD_Product_Sql_001(
            ToolBox_Con.getPreference_Customer_Code(context),
            productCode.toLong()
        ).toSqlQuery()
    )

    override fun getOsHeaderObj(
        customFormCode: Int,
        customFormType: Int,
        customFormVersion: Int,
        productCode: Int,
        serialId: String
    ): GeOs {
        var orderType : MdOrderType? = null
        var measureTp : MeMeasureTp? = null
        val form: GE_Custom_Form = getForm(customFormCode,customFormType,customFormVersion)
        form.so_order_type_code_default?.let {
            orderType = getOrderType(form.customer_code,form.so_order_type_code_default)
        }
        getSerialInfo(productCode,serialId)
        serialObj.measure_tp_code?.let {
            measureTp = getMeasureTp(serialObj.customer_code,serialObj.measure_tp_code)
        }
        //
        return GeOs(
            form.customer_code,
            form.custom_form_type,
            form.custom_form_code,
            form.custom_form_version,
            0,
            form.so_order_type_code_default?:-1,
            orderType?.orderTypeId?:"",
            orderType?.orderTypeDesc?:"",
            null,
            null,
            measureTp?.measureTpCode?:-1,
            measureTp?.measureTpId?:"",
            measureTp?.measureTpDesc?:"",
            null,
            serialObj.last_cycle_value,
            serialObj.last_measure_value?.toFloat(),
            serialObj.last_measure_date
        )
    }

    private fun getMeasureTp(customerCode: Long, measureTpCode: Int): MeMeasureTp? {
        return measureTpDao.getByString(
            MeMeasureTpSql_001(
                customerCode,
                measureTpCode
            ).toSqlQuery()
        )
    }

    private fun getOrderType(customerCode: Long, soOrderTypeCodeDefault: Int): MdOrderType? {
        return orderTypeDao.getByString(
                MdOrderTypeSql_001(
                    customerCode,
                    soOrderTypeCodeDefault
                ).toSqlQuery()
        )
    }

    private fun getForm(customFormCode: Int, customFormType: Int, customFormVersion: Int): GE_Custom_Form {
        return formDao.getByString(
            GE_Custom_Form_Sql_001_TT(
                ToolBox_Con.getPreference_Customer_Code(context).toString(),
                customFormType.toString(),
                customFormCode.toString(),
                customFormVersion.toString()
            ).toSqlQuery()
        )
    }
}