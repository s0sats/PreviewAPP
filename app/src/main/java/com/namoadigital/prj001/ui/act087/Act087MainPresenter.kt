package com.namoadigital.prj001.ui.act087

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Backup
import com.namoadigital.prj001.service.SO_PRODUCT_CODE
import com.namoadigital.prj001.service.SO_SERIAL_CODE
import com.namoadigital.prj001.service.WS_Product_Serial_Backup
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File
import kotlin.collections.ArrayList

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
    private val measureTpDao: MeMeasureTpDao,
    private val serialDeviceTpDao: MD_Product_Serial_Tp_DeviceDao,
    private val serialDeviceItemDao: MD_Product_Serial_Tp_Device_ItemDao,
    private val serialDeviceItemHistDao: MD_Product_Serial_Tp_Device_Item_HistDao,
    private val osDeviceDao: GeOsDeviceDao,
    private val osDeviceItemDao: GeOsDeviceItemDao,
    private val osDeviceItemHistDao: GeOsDeviceItemHistDao

): Act087MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    private val serialObj : MD_Product_Serial by lazy{
        getSerialObj(productCode, serialId)
    }

    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act087_title",
            "alert_error_on_create_os_form_ttl",
            "alert_error_on_create_os_form_msg",
            "alert_bkp_serial_error_ttl",
            "alert_no_bkp_serial_found_offline_msg",
            "alert_no_bkp_serial_found_msg",
            "alert_error_on_open_bkp_list_msg",
            "alert_error_no_data_return_msg",
            "dialog_bkp_machine_search_ttl",
            "dialog_bkp_machine_search_start",
            "alert_form_parameter_error_ttl",
            "alert_form_parameter_error_msg",
        )
        //
        val actAuxTrans = ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
        //
        val formOsFragTransient = ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            ToolBox_Inf.getResourceCode(
                context,
                mModule_Code,
                FormOsHeaderFrg.mResource_Name
            ),
            ToolBox_Con.getPreference_Translate_Code(context),
            FormOsHeaderFrg.getFragTranslationsVars()
        )
        //
        actAuxTrans.putAll(formOsFragTransient)
        //
        return actAuxTrans
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
            process_type = orderType?.processType?:"",
            display_option = orderType?.displayOption?:"",
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
            value_sufix = measureTp?.valueSufix,
            restriction_decimal = measureTp?.restrictionDecimal,
            date_start = null,
            last_cycle_value = serialObj.last_cycle_value,
            last_measure_value = serialObj.last_measure_value?.toFloat(),
            last_measure_date = serialObj.last_measure_date,
            so_edit_start_end = form.so_edit_start_end,
            so_order_type_code_default = form.so_order_type_code_default,
            so_allow_change_order_type = form.so_allow_change_order_type,
            device_tp_code_main = serialObj.device_tp_code_main,
            so_allow_backup =  form.so_allow_backup
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

    override fun executeWsBkpMachine(bkpProductCode: Long, bkpSerialId: String) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Product_Serial_Backup::class.java.name)
            //
            mView.showPD(
                ttl = hmAuxTrans["dialog_bkp_machine_search_ttl"],
                msg = hmAuxTrans["dialog_bkp_machine_search_start"]
            )
            //
            val mIntent = Intent(context, WBR_Product_Serial_Backup::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtras(
                    Bundle().apply {
                        putLong(SO_PRODUCT_CODE, serialObj.product_code)
                        putLong(SO_SERIAL_CODE, serialObj.serial_code)
                        putLong(MD_Product_SerialDao.PRODUCT_CODE, bkpProductCode)
                        putString(MD_Product_SerialDao.SERIAL_ID, bkpSerialId)
                        putInt(
                            MD_Product_SerialDao.SITE_CODE,
                            ToolBox_Con.getPreference_Site_Code(context).toInt()
                        )
                    }
                )
            }
            //
            context.sendBroadcast(mIntent)
        }else{
            searchBkpMachineInDb(bkpProductCode, bkpSerialId)
        }
    }

    private fun searchBkpMachineInDb(bkpProductCode: Long, bkpSerialId: String) {
        //
        val bkpSerialItemList: List<FormOsHeaderFrgSerialBkpItem>? = serialDao.query(
            Act087Sql_001(
                serialObj.customer_code,
                serialObj.product_code,
                serialObj.serial_id,
                bkpProductCode,
                bkpSerialId,
                ToolBox_Con.getPreference_Site_Code(context).toInt()
            ).toSqlQuery()
        )?.map { bkpOffline ->
                FormOsHeaderFrgSerialBkpItem(
                    bkpOffline.product_code.toInt(),
                    bkpOffline.serial_code.toInt(),
                    bkpOffline.serial_id,
                    bkpOffline.site_code,
                    bkpOffline.site_desc
                )
        }
        //
        if(bkpSerialItemList.isNullOrEmpty()){
            mView.showAlert(
                hmAuxTrans["alert_bkp_serial_error_ttl"],
                hmAuxTrans["alert_no_bkp_serial_found_offline_msg"],
            )
        } else{
            mView.reportSerialBkpMachineToFrag(
                serialBkpMachineList = bkpSerialItemList,
                onlineSearch = false
            )
        }
    }

    override fun processWsBkpMachineResult(mLink: String?) {
        if (mLink != null && mLink.isNotEmpty()){
            try{
                val rec = GsonBuilder().serializeNulls().create().fromJson(
                    mLink,
                    T_MD_Product_Serial_Backup_Rec::class.java
                )
                //
                if(rec.records != null && rec.records.isNotEmpty()){
                    processSerialBkpMachine(rec.records,page = rec.record_page?:-1, foundQty= rec.record_count?:-1,true)
                }else{
                    mView.showAlert(
                        hmAuxTrans["alert_bkp_serial_error_ttl"],
                        hmAuxTrans["alert_no_bkp_serial_found_msg"],
                    )
                }
            }catch (e: Exception){
                ToolBox_Inf.registerException(javaClass.name,e)
                mView.showAlert(
                    hmAuxTrans["alert_bkp_serial_error_ttl"],
                    hmAuxTrans["alert_error_on_open_bkp_list_msg"],
                )
            }
        }else{
            mView.showAlert(
                hmAuxTrans["alert_bkp_serial_error_ttl"],
                hmAuxTrans["alert_error_no_data_return_msg"],
            )
        }
    }

    private fun processSerialBkpMachine(
        records: List<T_MD_Product_Serial_Backup_Record>,
        page: Int,
        foundQty: Int,
        onlineSearch: Boolean
    ) {
        val bkpSerialItemList: MutableList<FormOsHeaderFrgSerialBkpItemAbs> = records.map { bkp ->
            FormOsHeaderFrgSerialBkpItem(
                bkp.productCode,
                bkp.serialCode,
                bkp.serialId,
                bkp.siteCode,
                bkp.siteDesc
            )
        }.toMutableList()
        //
        if(foundQty > records.size ){
            bkpSerialItemList.add(
                FormOsHeaderFrgSerialBkpExceededItem(
                    hmAuxTrans["alert_qty_records_exceeded_msg"]!!,
                    hmAuxTrans["records_display_limit_lbl"]!!,
                    page,
                    hmAuxTrans["records_found_lbl"]!!,
                    foundQty
                )
            )
        }
        //
        mView.reportSerialBkpMachineToFrag(
            serialBkpMachineList = bkpSerialItemList.toList(),
            onlineSearch = onlineSearch
        )
    }

    override fun createOsHeader(formOsHeader: GeOs) {
        formOsHeader.custom_form_data = getNextFormData(formOsHeader)
        val daoObjReturn = geOsDao.createGeOsStructure(formOsHeader, serialObj)
        if(!daoObjReturn.hasError()){
            mView.callAct011(
                getAct011Bundle(
                    formOsHeader
                )
            )
        }else{
            mView.showAlert(
                ttl =hmAuxTrans["alert_error_on_create_os_form_ttl"],
                msg= hmAuxTrans["alert_error_on_create_os_form_msg"]
            )
        }
    }

    private fun getAct011Bundle(formOsHeader: GeOs): Bundle {
        return Bundle().apply {
            putString(MD_ProductDao.PRODUCT_CODE, serialObj.product_code.toString())
            putString(MD_ProductDao.PRODUCT_DESC, serialObj.product_desc)
            putString(MD_ProductDao.PRODUCT_ID, serialObj.product_id)
            putString(MD_Product_SerialDao.SERIAL_ID, serialObj.serial_id)
            putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, formOsHeader.custom_form_type.toString())
            putString(GE_Custom_FormDao.CUSTOM_FORM_CODE,formOsHeader.custom_form_code.toString())
            putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, formOsHeader.custom_form_version.toString())
            putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, formOsHeader.custom_form_data.toString())
            //putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, formOsHeader.custom_form_type.toString())
            //putString(ConstantBaseApp.MAIN_REQUESTING_ACT,ConstantBaseApp.ACT005);

        }
    }
    //todo apagar
    private fun createFormLocal(formOsHeader: GeOs) {
       formOsHeader.custom_form_data = getNextFormData(formOsHeader)
       geOsDao.createGeOsStructure(formOsHeader,serialObj)
       /*
//        val geCustomForm = getForm(
//            formOsHeader.custom_form_type,
//            formOsHeader.custom_form_code,
//            formOsHeader.custom_form_version,
//        )
//        val mdProduct = getProductInfo(
//            productCode
//        )
//        //
//        GE_Custom_Form_Local().apply {
//            customer_code = formOsHeader.customer_code
//            custom_form_type = formOsHeader.custom_form_type
//            custom_form_code = formOsHeader.custom_form_code
//            custom_form_version = formOsHeader.custom_form_version
//            custom_form_data = getNextFormData(geCustomForm)
//            custom_form_pre = ToolBox_Inf.getPrefix(context)
//            custom_form_status = ConstantBaseApp.SYS_STATUS_IN_PROCESSING
//            require_signature = geCustomForm.require_signature
//            require_location = geCustomForm.require_location
//            require_serial_done = geCustomForm.require_serial_done
//            automatic_fill = geCustomForm.automatic_fill
//            custom_product_code = mdProduct!!.product_code.toInt()
//            custom_product_desc = mdProduct.product_desc
//            custom_product_id = mdProduct.product_id
//            custom_product_icon_name = mdProduct.product_icon_name
//            custom_product_icon_url =  mdProduct.product_icon_url
//            custom_product_icon_url_local =  mdProduct.product_icon_url_local
//            custom_form_desc =
//            serial_id =
//            schedule_date_start_format =
//            schedule_date_end_format =
//            schedule_date_start_format_ms =
//            schedule_date_end_format_ms =
//            require_serial =
//            allow_new_serial_cl =
//            all_site =
//            all_operation =
//            all_product =
//
//            site_code =
//            site_id =
//            site_desc =
//            io_control =
//            inbound_auto_create =
//            operation_code =
//            operation_id =
//            operation_desc =
//            local_control =
//            product_io_control =
//            site_restriction =
//            serial_rule =
//            serial_min_length =
//            serial_max_length =
//            schedule_comments =
//
//            schedule_prefix =
//            schedule_code =
//            schedule_exec =
//
//            ticket_prefix =
//            ticket_code =
//            ticket_seq =
//            ticket_seq_tmp =
//            step_code =
//            tag_operational_code =
//            tag_operational_id =
//            tag_operational_desc =
//            is_so =
//            so_edit_start_end =
//            so_order_type_code_default =
//            so_allow_change_order_type =
//            so_allow_backup =
//        }
*/
    }

    private fun getNextFormData(geOs: GeOs): Int {
        val nextDataAux = formDao.getByStringHM(
            GE_Custom_Form_Local_Sql_002(
                geOs.customer_code.toString(),
                geOs.custom_form_type.toString(),
                geOs.custom_form_code.toString(),
                geOs.custom_form_version.toString()
            ).toSqlQuery().toLowerCase()
        )
        //
        return nextDataAux[GE_Custom_Form_Local_Sql_002.ID]!!.toInt()
    }
}