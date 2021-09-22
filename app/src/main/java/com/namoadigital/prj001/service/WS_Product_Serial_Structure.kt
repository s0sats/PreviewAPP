package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Structure
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_009
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

class WS_Product_Serial_Structure : IntentService("WS_Product_Serial_Structure") {

    private val hmAux_Trans by lazy {

        val transList: MutableList<String> = ArrayList()
        transList.add("msg_no_data_returned")
        transList.add("generic_sending_data_msg")
        transList.add("generic_receiving_data_msg")
        transList.add("generic_processing_data")
        transList.add("generic_process_finalized_msg")

        mResource_Code = ToolBox_Inf.getResourceCode(
            applicationContext,
            mModule_Code,
            mResource_Name
        )

        ToolBox_Inf.setLanguage(
            applicationContext,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(applicationContext),
            transList
        )
    }
    //
    private val serialDao: MD_Product_SerialDao by lazy {
        MD_Product_SerialDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            ConstantBaseApp.DB_VERSION_CUSTOM
        )
    }
    //
    private val tpDeviceDao: MD_Product_Serial_Tp_DeviceDao by lazy {
        MD_Product_Serial_Tp_DeviceDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        )
    }



    private val mModule_Code: String = Constant.APP_MODULE
    private var mResource_Code = "0"
    private val mResource_Name = "ws_product_serial_structure"
    private val gson = GsonBuilder().serializeNulls().create()

    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        val bundle = intent!!.extras
        //
        try {
            val customerCode = bundle!!.getLong(MD_Product_SerialDao.CUSTOMER_CODE)
            val productCode = bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE)
            val serialCode = bundle.getLong(MD_Product_SerialDao.SERIAL_CODE)
            val scnItemCheck = bundle.getInt(MD_Product_SerialDao.SCN_ITEM_CHECK)

            processSerialStructure(customerCode, productCode, serialCode, scnItemCheck)
        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            ToolBox_Inf.registerException(javaClass.name, e)
            ToolBox_Inf.sendBCStatus(applicationContext, "CUSTOM_ERROR", sb.toString(), "", "0")
        } finally {
            WBR_Product_Serial_Structure.completeWakefulIntent(intent)
        }
    }


    private fun processSerialStructure(
        customerCode: Long,
        productCode: Long,
        serialCode: Long,
        scnItemCheck: Int
    ) {
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAux_Trans["generic_sending_data_msg"],
            "",
            "0"
        )
        //
        val env = T_MD_Product_Serial_Structure()
        env.app_code = Constant.PRJ001_CODE
        env.app_version = Constant.PRJ001_VERSION
        env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
        env.app_type = Constant.PKG_APP_TYPE_DEFAULT
        env.getSearch().add(
            T_MD_Product_Serial_Structure_Env(
                customerCode,
                productCode,
                serialCode,
                scnItemCheck
            )
        )
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_PRODUCT_SERIAL_STRUCTURE_SEARCH,
            gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAux_Trans["generic_receiving_data_msg"],
            "",
            "0"
        )
        //
        //
        val rec = gson.fromJson(
            resultado,
            T_MD_Product_Serial_Structure_Rec::class.java
        )
        //
        //
        if (!ToolBox_Inf.processWSCheckValidation(
                applicationContext,
                rec.validation,
                rec.error_msg,
                rec.link_url,
                1,
                1
            )
            ||
            !ToolBox_Inf.processoOthersError(
                applicationContext,
                resources.getString(R.string.generic_error_lbl),
                rec.error_msg
            )
        ) {
            return
        }
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAux_Trans["generic_processing_data"],
            "",
            "0"
        )
        //
        //
        if (rec.structure != null) {
            processSerialStructureReturn(rec.structure!!)
        }
    }

    private fun processSerialStructureReturn(structures: List<MD_Product_Serial_Structure>) {

        for (serialStructure in structures) {
            //
            val daoObjReturn = tpDeviceDao.addUpdate(serialStructure.device_tp, true)
            //
            if(!daoObjReturn.hasError()){
                val serialUpdateInfo = serialUpdateInfo(serialStructure)
                val result = gson.toJson(serialUpdateInfo)
                ToolBox.sendBCStatus(applicationContext, "CLOSE_ACT", hmAux_Trans["generic_process_finalized_msg"], result, "0")
            }else{
                ToolBox.sendBCStatus(applicationContext, "ERROR_1", hmAux_Trans["msg_error_on_insert_ticket"], "", "0")
            }
        }

    }

    private fun serialUpdateInfo(serialStructure: MD_Product_Serial_Structure): MD_Product_Serial? {
        val serial = serialDao.getByString(
            MD_Product_Serial_Sql_009(
                serialStructure.customer_code,
                serialStructure.product_code,
                serialStructure.serial_code
            ).toSqlQuery()
        )
        //
        serial.has_item_check = serialStructure.has_item_check
        serial.scn_item_check = serialStructure.scn_item_check
        serial.measure_tp_code = serialStructure.measure_tp_code
        serial.last_measure_value = serialStructure.last_measure_value
        serial.last_measure_date = serialStructure.last_measure_date
        serial.last_cycle_value = serialStructure.last_cycle_value
        //
        serialDao.addUpdate(serial)
        //
        return serial
    }


}