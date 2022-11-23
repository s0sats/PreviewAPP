package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.MyActionsCache
import com.namoadigital.prj001.model.TUnfocusAndHistoricEnv
import com.namoadigital.prj001.model.TUnfocusAndHistoricRec
import com.namoadigital.prj001.receiver.WBR_UnfocusAndHistoric
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WS_UnfocusAndHistoric : IntentService("WS_UnfocusAndHistoric") {

    private val mModuleCode = Constant.APP_MODULE
    private val mResourceName = "ws_generic_resource"
    private val gson = GsonBuilder().serializeNulls().create()
    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        //
        try {
            val bundle = intent?.extras ?: Bundle()
            val productCode = bundle.getInt(PRODUCT_CODE,-1)
            val serialCode = bundle.getLong(SERIAL_CODE,-1)
            //
            processUnfocusHistoricAction(productCode, serialCode)

        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            //
            ToolBox_Inf.registerException(javaClass.name, e)
            //
            ToolBox.sendBCStatus(
                applicationContext,
                ConstantBase.PD_TYPE_ERROR_1,
                sb.toString(),
                "",
                "0"
            )
        } finally {
            WBR_UnfocusAndHistoric.completeWakefulIntent(intent)
        }
    }

    private fun processUnfocusHistoricAction(productCode: Int, serialCode: Long) {
        val env = TUnfocusAndHistoricEnv(
            productCode,
            serialCode,
            Constant.PRJ001_CODE,
            Constant.PRJ001_VERSION,
            Constant.PKG_APP_TYPE_DEFAULT,
            ToolBox_Con.getPreference_Session_App(applicationContext)
        )

        ToolBox_Inf.sendBCStatus(
            applicationContext, "STATUS",
            hmAuxTrans["generic_receiving_data_msg"], "", "0"
        )
        //
        val resultado: String = ToolBox_Con.connWebService(
            Constant.WS_DOWNLOAD_NOT_FOCUS_AND_HISTORIC,
            gson.toJson(env)
        )
        //
        val rec: TUnfocusAndHistoricRec = gson.fromJson(
            resultado,
            TUnfocusAndHistoricRec::class.java
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
            hmAuxTrans["generic_processing_data"],
            "",
            "0"
        )
        //
        processReturn(rec.obj, productCode, serialCode)

    }

    private fun processReturn(rec: List<MyActionsCache>, productCode: Int, serialCode: Long) {
        val file_name = ToolBox_Inf.getOtherActionFileName(
            productCode,
            serialCode
        )
        //Chama metodo para criar arquivo
        ToolBox_Inf.createJsonFile(file_name, gson.toJson(rec), Constant.OTHER_ACTIONS_JSON_PATH)
        //
        ToolBox_Inf.sendBCStatus(
            applicationContext,
            "CLOSE_ACT",
            hmAuxTrans["generic_process_finalized_msg"],
            "",
            "0"
        )
        //
    }

    private fun loadTranslation() : HMAux {
        val translist = listOf<String>(
            "generic_sending_data_msg",
            "generic_receiving_data_msg",
            "generic_processing_data",
            "generic_process_finalized_msg",
            "msg_no_data_returned"
        )
        //
        val mResourceCode = ToolBox_Inf.getResourceCode(
            applicationContext,
            mModuleCode,
            mResourceName
        )
        //
        return ToolBox_Inf.setLanguage(
            applicationContext,
            mModuleCode,
            mResourceCode,
            ToolBox_Con.getPreference_Translate_Code(applicationContext),
            translist
        )
    }

    companion object{
        const val PRODUCT_CODE = "PRODUCT_CODE"
        const val SERIAL_CODE = "SERIAL_CODE"
    }
}