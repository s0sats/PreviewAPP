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
import java.util.ArrayList

class Act087MainPresenter(
    private val context: Context,
    private val mView: Act087MainContract.I_View,
    private val mModule_Code: String,
    private val mResource_Code: String,
    private val customFormType: Int,
    private val customFormCode: Int,
    private val customFormVersion: Int,
    private val productCode: Int,
    private val serialId: String,
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

    private val serialObj : MD_Product_Serial by lazy{
        getSerialObj(productCode, serialId)
    }

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

    override fun validateBundleParams(): Boolean {
        if(customFormType > -1 && customFormCode > -1 && customFormVersion > -1 && productCode > -1 && serialId.isNotEmpty()){
            return true
        }
        return false
    }

    override fun getSerialInfo() = serialObj

    private fun getSerialObj(productCode: Int, serialId: String) : MD_Product_Serial{
        return serialDao.getByString(
            MD_Product_Serial_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                productCode.toLong(),
                serialId
            ).toSqlQuery()
        )
    }


    override fun getProductIcon(): Bitmap? {
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

    override fun getProductInfo(productCode: Int) : MD_Product? = productDao.getByString(
        MD_Product_Sql_001(
            ToolBox_Con.getPreference_Customer_Code(context),
            productCode.toLong()
        ).toSqlQuery()
    )

    override fun getOsHeaderObj(): GeOs {
        var orderType : MdOrderType? = null
        var measureTp : MeMeasureTp? = null
        //
        val form: GE_Custom_Form = getForm(customFormCode,customFormType,customFormVersion)
        form.so_order_type_code_default?.let {
            orderType = getOrderType(form.customer_code,form.so_order_type_code_default)
        }
        getSerialInfo()
        serialObj.measure_tp_code?.let {
            measureTp = getMeasureTp(serialObj.customer_code,serialObj.measure_tp_code)
        }
        //
        return GeOs(
            customer_code = form.customer_code,
            custom_form_type = form.custom_form_type,
            custom_form_code = form.custom_form_code,
            custom_form_version = form.custom_form_version,
            custom_form_data = 0,
            order_type_code = orderType?.orderTypeCode?:-1,
            order_type_id = orderType?.orderTypeId?:"",
            order_type_desc = orderType?.orderTypeDesc?:"",
            backup_product_code = null,
            backup_product_id = null,
            backup_product_desc = null,
            backup_serial_code = null,
            backup_serial_id = null,
            measure_tp_code = measureTp?.measureTpCode,
            measure_tp_id = measureTp?.measureTpId,
            measure_tp_desc = measureTp?.measureTpDesc,
            measure_value = null,
            measure_cycle_value = serialObj.last_cycle_value,
            last_measure_value = serialObj.last_measure_value?.toFloat(),
            last_measure_date = serialObj.last_measure_date,
            so_edit_start_end = form.so_edit_start_end,
            so_order_type_code_default = form.so_order_type_code_default,
            so_allow_change_order_type = form.so_allow_change_order_type
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

    override fun getOrderTypeList(orderTypeCode: Int): ArrayList<MdOrderType> {
        val orderTypeQuery =
            if(orderTypeCode == -1){
                MdOrderTypeSql_002(
                    ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
            } else{
                MdOrderTypeSql_001(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    orderTypeCode
                ).toSqlQuery()
            }
        //
        return orderTypeDao.query(orderTypeQuery) as ArrayList<MdOrderType>
    }

    override fun getMeasure(measureCode: Int): MeMeasureTp? {
        return getMeasureTp(ToolBox_Con.getPreference_Customer_Code(context),measureCode)
    }
}