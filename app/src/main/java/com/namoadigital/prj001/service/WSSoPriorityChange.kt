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
import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.dao.SmPriorityDao
import com.namoadigital.prj001.model.SoPriorityChangeEnv
import com.namoadigital.prj001.model.SoPriorityChangeRec
import com.namoadigital.prj001.receiver.WBR_So_Status_Change
import com.namoadigital.prj001.sql.SM_SO_Sql_001
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.IOException

class WSSoPriorityChange:
    IntentService("WS_So_Priority_Change")
{
    //
    private val soDao by lazy{
        SM_SODao(
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
    private var priority_code: Int = 0
    private var priority_desc: String = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //
        val nm = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = ToolBox_Inf.getLowImportanceBuilder(
            applicationContext, nm
        )
        builder.setOngoing(true)
        builder.setContentTitle(applicationContext.getString(R.string.title_notification_generic))
        builder.setContentText(applicationContext.getString(R.string.generic_sending_data_msg))
        builder.setSmallIcon(R.drawable.upload_animation)
        val notification = builder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && notification != null
        ) {
            startForeground(ConstantBaseApp.NOTIFICATION_SYNC_ID, notification)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        //
        try {
            Thread.sleep(40000)
            val bundle = intent?.extras ?: Bundle()
            val so_prefix = bundle.getInt(SM_SODao.SO_PREFIX, 0)
            val so_code = bundle.getInt(SM_SODao.SO_CODE, 0)
            val so_scn = bundle.getInt(SM_SODao.SO_SCN, 0)
            priority_code = bundle.getInt(SmPriorityDao.PRIORITY_CODE, 0)
            priority_desc = bundle.getString(SmPriorityDao.PRIORITY_DESC, "")
            val token = bundle.getString(WS_SO_TOKEN, "")
            //
            processSOStatusChange(
                so_prefix,
                so_code,
                so_scn,
                priority_code,
                token
            )

        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            //
            ToolBox_Inf.registerException(javaClass.name, e)
            //
            if (ToolBox_Con.isHttpError(e)) {
                ToolBox_Inf.sendBCStatus(
                    applicationContext,
                    ConstantBase.PD_TYPE_ERROR_HTTP,
                    sb.toString(),
                    "",
                    "0"
                )
            } else {
                ToolBox_Inf.sendBCStatus(
                    applicationContext,
                    ConstantBase.PD_TYPE_ERROR_1,
                    sb.toString(),
                    "",
                    "0"
                )
            }
        } finally {
            WBR_So_Status_Change.completeWakefulIntent(intent)
        }
    }

    @Throws(IOException::class)
    private fun processSOStatusChange(
        so_prefix: Int,
        so_code: Int,
        so_scn: Int,
        priority_code: Int,
        token: String
    ) {
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_sending_data_msg"], "", "0")
        //
        val env = SoPriorityChangeEnv(
            app_code = Constant.PRJ001_CODE,
            app_version = Constant.PRJ001_VERSION,
            session_app = ToolBox_Con.getPreference_Session_App(applicationContext),
            app_type = Constant.PKG_APP_TYPE_DEFAULT,
            token = token,
            so_prefix = so_prefix,
            so_code = so_code,
            so_scn = so_scn,
            priority_code = priority_code
        )
        //
        val resultado = ToolBox_Con.connWebService(
            Constant.WS_SO_PRIROTY_CHANGE,
            gson.toJson(env)
        )
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_receiving_data_msg"], "", "0")
        //
        val rec = gson.fromJson(resultado, SoPriorityChangeRec::class.java)
        //
        if (!ToolBox_Inf.processWSCheckValidation(
                applicationContext,
                rec.validation,
                rec.error_msg,
                rec.link_url,
                1,
                1)
            ||
            !ToolBox_Inf.processoOthersError(
                applicationContext,
                resources.getString(R.string.generic_error_lbl),
                rec.error_msg)) {
            return
        }
        //
        ToolBox.sendBCStatus(applicationContext, "STATUS", hmAuxTrans["generic_processing_data"], "", "0")
        //
        processSOStatusChange(rec)
    }

    @Throws(Exception::class)
    private fun processSOStatusChange(result: SoPriorityChangeRec) {
        val hmAuxReturn = HMAux()
        hmAuxReturn[WS_RETURN_SO_PRIORITY] = result.so_status[0].ret_status
        hmAuxReturn[WS_RETURN_SO_MSG] = result.so_status[0].ret_msg
        if("OK".equals(result.so_status[0].ret_status, true)) {
            result.so_status.forEach { item ->
                val smSo = soDao.getByString(
                    SM_SO_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(applicationContext),
                        item.so_prefix,
                        item.so_code,
                    ).toSqlQuery()
                )
                smSo.priority_code = priority_code
                smSo.priority_desc = priority_desc
                smSo.so_scn = item.so_scn
                soDao.addUpdate(smSo)
            }
            //
            ToolBox.sendBCStatus(
                applicationContext,
                "CLOSE_ACT",
                result.so_status[0].ret_msg,
                hmAuxReturn,
                "",
                "0"
            )
        }else{
            result.so?.let{
                //
                it.forEach { soFull ->
                    //Apaga SO completa
                    soDao.removeFull(soFull)
                    //
                    soFull.setPK()
                }
                soDao.addUpdate(it,false)
                //
            }?:run{
                result.so_status.forEach {
                    val smSo = soDao.getByString(
                        SM_SO_Sql_001(
                            ToolBox_Con.getPreference_Customer_Code(applicationContext),
                            it.so_prefix,
                            it.so_code,
                        ).toSqlQuery()
                    )
                    smSo.sync_required = 1
                    soDao.addUpdate(smSo)
                }
            }
            //
            ToolBox.sendBCStatus(
                applicationContext,
                ConstantBase.PD_TYPE_CUSTOM_ERROR,
                result.so_status[0].ret_msg,
                hmAuxReturn,
                "",
                "0"
            )
        }
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
        const val WS_RETURN_SO_PRIORITY = "RETURN_SO_PRIORITY"
        const val WS_RETURN_SO_MSG = "RETUN_SO_MSG"
        const val WS_SO_TOKEN = "SO_TOKEN"

        const val WS_BUNDLE_PRIORITY_CODE = "PRIORITY_CODE"
        const val WS_BUNDLE_PRIORITY_DESC = "PRIORITY_DESC"
    }
}