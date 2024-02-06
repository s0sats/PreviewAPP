package com.namoadigital.prj001.service

import android.app.IntentService
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.receiver.WBR_So_Status_Change
import com.namoadigital.prj001.service.base.BaseWsIntentService
import com.namoadigital.prj001.sql.SM_SO_Sql_001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.IOException

class WSSoStatusChange :
    BaseWsIntentService("WS_So_Status_Change", IntentServiceMode.UPLOAD_DATA()) {
    //
    private val soDao by lazy {
        SM_SODao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        )
    }

    //
    private val serialDao by lazy {
        MD_Product_SerialDao(
            getApplicationContext(),
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(getApplicationContext())),
            Constant.DB_VERSION_CUSTOM
        )
    }

    //
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
            val so_prefix = bundle.getInt(SM_SODao.SO_PREFIX, 0)
            val so_code = bundle.getInt(SM_SODao.SO_CODE, 0)
            val so_scn = bundle.getInt(SM_SODao.SO_SCN, 0)
            val action = bundle.getString(WS_BUNDLE_ACTION, "")
            val return_so = bundle.getString(WS_BUNDLE_RETURN_SO, "1")
            val token = bundle.getString(WS_BUNDLE_SO_TOKEN, "")
            //
            processSOStatusChange(
                so_prefix,
                so_code,
                so_scn,
                action,
                return_so,
                token
            )

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
            WBR_So_Status_Change.completeWakefulIntent(intent)
        }
    }

    @Throws(IOException::class)
    private fun processSOStatusChange(
        so_prefix: Int,
        so_code: Int,
        so_scn: Int,
        action: String,
        return_so: String,
        token: String
    ) {
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAuxTrans["generic_sending_data_msg"],
            "",
            "0"
        )
        //
        val env = SoStatusChangeEnv(
            app_code = Constant.PRJ001_CODE,
            app_version = Constant.PRJ001_VERSION,
            session_app = ToolBox_Con.getPreference_Session_App(applicationContext),
            app_type = Constant.PKG_APP_TYPE_DEFAULT,
            token = token,
            so_prefix = so_prefix,
            so_code = so_code,
            so_scn = so_scn,
            action = action,
            return_so = return_so,
        )
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_SO_STATUS_CHANGE,
            gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAuxTrans["generic_receiving_data_msg"],
            "",
            "0"
        )
        //
        val rec = gson.fromJson(resultado, SoStatusChangeRec::class.java)
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
        if ("0" == return_so) {
            val soStatus = rec.so_status[0]
            val recHmAux = HMAux()
            recHmAux["so_status"] = soStatus.so_status
            recHmAux["scn_code"] = soStatus.so_scn.toString()

            if (soStatus.ret_status != "OK") {
                ToolBox.sendBCStatus(
                    applicationContext,
                    ConstantBase.PD_TYPE_CUSTOM_ERROR,
                    soStatus.ret_msg,
                    "",
                    "0"
                )
            } else {
                ToolBox.sendBCStatus(
                    applicationContext,
                    "CLOSE_ACT",
                    hmAuxTrans["msg_no_so_to_send"],
                    recHmAux,
                    "",
                    "0"
                )
            }
        } else {
            processSOStatusChange(rec)
        }
    }

    @Throws(Exception::class)
    private fun processSOStatusChange(result: SoStatusChangeRec) {
        val hmAux = HMAux()
        val soStatus = result.so_status[0]
        hmAux[WS_RETURN_SO_STATUS] = soStatus.ret_status
        hmAux[WS_RETURN_SO_MSG] = soStatus.ret_msg

        result.so?.let {
            //
            it.forEach { soFull ->
                //Apaga SO completa
                soDao.removeFull(soFull)
                //
                soFull.setPK()
            }
            soDao.addUpdate(it,false)
            //
            if ("OK".equals(soStatus.ret_status, true)) {
                ToolBox.sendBCStatus(
                    applicationContext,
                    "CLOSE_ACT",
                    hmAuxTrans["generic_process_finalized_msg"],
                    hmAux,
                    "",
                    "0"
                )
            }else{
                ToolBox.sendBCStatus(
                    applicationContext,
                    ConstantBase.PD_TYPE_CUSTOM_ERROR,
                    soStatus.ret_msg,
                    "",
                    "0"
                )
            }
            //
        } ?: run {

            if ("OK".equals(soStatus.ret_status, true)) {
                result.so_status.forEach { item ->
                    val smSo = soDao.getByString(
                        SM_SO_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(applicationContext),
                            item.so_prefix,
                            item.so_code,
                        ).toSqlQuery()
                    )
                    smSo.status = item.so_status
                    soDao.addUpdate(smSo)
                }

                ToolBox.sendBCStatus(
                    applicationContext,
                    "CLOSE_ACT",
                    hmAuxTrans["generic_process_finalized_msg"],
                    hmAux,
                    "",
                    "0"
                )
            } else {
                ToolBox.sendBCStatus(
                    applicationContext,
                    ConstantBase.PD_TYPE_CUSTOM_ERROR,
                    soStatus.ret_msg,
                    "",
                    "0"
                )
            }
        }
        //
    }

    private fun loadTranslation(): HMAux {
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

    companion object {
        const val WS_BUNDLE_ACTION = "ACTION"
        const val WS_BUNDLE_RETURN_SO = "RETURN_SO"
        const val WS_RETURN_SO_STATUS = "RETURN_SO_STATUS"
        const val WS_RETURN_SO_MSG = "RETUN_SO_MSG"
        const val WS_BUNDLE_SO_TOKEN = "RETURN_SO_TOKEN"

        const val WS_ACTION_SO_EDIT = "EDIT"
        const val WS_ACTION_SO_PROCESS = "PROCESS"
        const val WS_ACTION_SO_STOP = "STOP"


    }
}