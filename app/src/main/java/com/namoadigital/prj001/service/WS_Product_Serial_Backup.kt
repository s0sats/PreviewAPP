package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.model.T_MD_Product_Serial_Backup_Env
import com.namoadigital.prj001.model.T_MD_Product_Serial_Backup_Rec
import com.namoadigital.prj001.receiver.WBR_Product_Serial_Backup
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

const val SO_PRODUCT_CODE = "SO_PRODUCT_CODE"
const val SO_SERIAL_CODE = "SO_SERIAL_CODE"

class WS_Product_Serial_Backup : IntentService("WS_Product_Serial_Backup") {

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
    private val mModule_Code: String = Constant.APP_MODULE
    private var mResource_Code = "0"
    private val mResource_Name = "ws_product_serial_backup"
    private val gson = GsonBuilder().serializeNulls().create()
    //
    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()

        //
        try {
            val bundle = intent!!.extras!!
            val soProductCode = bundle.getLong(SO_PRODUCT_CODE)
            val soSerialCode = bundle.getLong(SO_SERIAL_CODE)
            val productCode = bundle.getLong(MD_Product_SerialDao.PRODUCT_CODE)
            val serialId = bundle.getString(MD_Product_SerialDao.SERIAL_ID)
            val siteCode = bundle.getInt(MD_Product_SerialDao.SITE_CODE)

            processSerialBackup(soProductCode, soSerialCode, productCode, serialId, siteCode)
        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            ToolBox_Inf.registerException(javaClass.name, e)
            ToolBox_Inf.sendBCStatus(applicationContext, "CUSTOM_ERROR", sb.toString(), "", "0")
        } finally {
            WBR_Product_Serial_Backup.completeWakefulIntent(intent)
        }
    }

    private fun processSerialBackup(
        soProductCode: Long,
        soSerialCode: Long,
        productCode: Long,
        serialId: String?,
        siteCode: Int
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
        val env =  T_MD_Product_Serial_Backup_Env(
            soProductCode,
            soSerialCode,
            productCode,
            serialId,
            siteCode
        )
        env.app_code = Constant.PRJ001_CODE
        env.app_version = Constant.PRJ001_VERSION
        env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
        env.app_type = Constant.PKG_APP_TYPE_DEFAULT
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_PRODUCT_SERIAL_BACKUP,
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
        val rec = gson.fromJson(
            resultado,
            T_MD_Product_Serial_Backup_Rec::class.java
        )
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
        processSerialBackupRecords(rec)
        //
    }

    private fun processSerialBackupRecords(response: T_MD_Product_Serial_Backup_Rec) {
        val result = gson.toJson(response)
        ToolBox.sendBCStatus(applicationContext, "CLOSE_ACT", hmAux_Trans["generic_process_finalized_msg"], result, "0")
    }

}