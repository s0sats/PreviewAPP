package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.SerialSiteEnv
import com.namoadigital.prj001.model.SiteSerialInvRec
import com.namoadigital.prj001.receiver.WBR_Serial_Site_Inv
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp.GC_STATUS
import com.namoadigital.prj001.util.ConstantBaseApp.GC_STATUS_JUMP
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WsSerialSiteInventory : IntentService("WS_Serial_Site_Inv") {


    private var hmAux_Trans = HMAux()

    override fun onHandleIntent(intent: Intent?) {
        intent?.extras?.let {
            try {
                process(it.getInt(SITE_CODE), it.getInt(GC_STATUS_JUMP), it.getInt(GC_STATUS))
            } catch (e: Exception) {
                ToolBox_Inf.wsExceptionTreatment(applicationContext, e).let { string ->
                    ToolBox_Inf.registerException(javaClass.name, e)
                    ToolBox.sendBCStatus(
                        applicationContext,
                        "ERROR_1",
                        string.toString(),
                        "",
                        ""
                    )
                }
            } finally {
                ToolBox_Inf.callPendencyNotification(applicationContext)
                WBR_Serial_Site_Inv.completeWakeFulService(intent)
            }
        }
    }

    private fun process(siteCode: Int, statusJump: Int, status: Int) {

        loadTranslation()

        val gson = GsonBuilder().serializeNulls().create()


        val env = SerialSiteEnv(siteCode)
        env.app_code = Constant.PRJ001_CODE
        env.app_version = Constant.PRJ001_VERSION
        env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
        env.app_type = Constant.PKG_APP_TYPE_DEFAULT

        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            MSG_CHECKING_SITE,
            "",
            "0"
        )

        val result = ToolBox_Con.connWebService(
            Constant.WS_SERIAL_SITE_INV,
            gson.toJson(env)
        )

        val rec = gson.fromJson(result, SiteSerialInvRec::class.java)

        if (!ToolBox_Inf.processWSCheckValidation(
                applicationContext,
                rec.validation,
                rec.error_msg,
                rec.link_url,
                statusJump,
                status
            ) || !ToolBox_Inf.processoOthersError(
                applicationContext,
                resources.getString(R.string.generic_error_lbl),
                rec.error_msg
            )
        ) {
            return
        }


        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            MSG_CHECKING_OK,
            "",
            "0"
        )

        ToolBox_Inf.createJsonFile(
            FILE_NAME,
            gson.toJson(rec),
            Constant.SERIAL_SITE_INV_JSON_PATH
        )

        ToolBox.sendBCStatus(
            applicationContext,
            "CLOSE_ACT",
            MSG_CHECKING_END_PROCESS,
            "${rec.serialSiteInventory.size}",
            "0"
        )

    }


    private fun loadTranslation() {
        listOf(
            MSG_CHECKING_SITE,
            MSG_CHECKING_OK,
            MSG_CHECKING_ERROR,
        ).let { list ->
            resourceCode = ToolBox_Inf.getResourceCode(
                applicationContext,
                moduleCode,
                resourceName
            )

            hmAux_Trans = ToolBox_Inf.setLanguage(
                applicationContext,
                moduleCode,
                resourceCode,
                ToolBox_Con.getPreference_Translate_Code(applicationContext),
                list
            )
        }
    }

    companion object {

        var resourceCode = "0"
        val resourceName = "WS_Serial_Site_Inv"
        val moduleCode = Constant.APP_MODULE
        const val SITE_CODE = "SITE_CODE1"

        const val FILE_NAME = "serial_site_inventory.json"

        const val MSG_CHECKING_SITE = "msg_checking_site"
        const val MSG_CHECKING_OK = "msg_checking_ok"
        const val MSG_CHECKING_END_PROCESS = "msg_checking_end_process"
        const val MSG_CHECKING_ERROR = "msg_checking_error"

    }
}