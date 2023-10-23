package com.namoadigital.prj001.service

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.dao.SM_SODao
import com.namoadigital.prj001.extensions.formatSyncSoList
import com.namoadigital.prj001.model.SM_SO
import com.namoadigital.prj001.model.TSO_Search_Env
import com.namoadigital.prj001.model.TSO_Search_Rec
import com.namoadigital.prj001.receiver.WBR_SO_Sync
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class WS_SO_Sync: IntentService("WS_SO_Sync") {

    private var processedSoSize = 0
    private val mModuleCode = Constant.APP_MODULE
    private val mResourceName = "ws_generic_resource"
    private val gson = GsonBuilder().serializeNulls().create()
    private val env = TSO_Search_Env()
    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }
    private val soDao by lazy {
        SM_SODao(applicationContext)
    }
    val soList: MutableList<SM_SO> = java.util.ArrayList()
    var pageNumber=0
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = ToolBox_Inf.getLowImportanceBuilder(applicationContext, nm)
        builder.setOngoing(true)
        builder.setContentTitle(applicationContext.getString(R.string.title_notification_generic))
        builder.setContentText(applicationContext.getString(R.string.msg_download_so_data))
        builder.setSmallIcon(R.drawable.download_animation)
        val notification = builder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && notification != null
        ) {
            startForeground(ConstantBaseApp.NOTIFICATION_SO_ID, notification)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        val bundle = intent!!.extras
        try {
            val product_code = bundle!!.getLong(WS_BUNDLE_PRODUCT_CODE, -1L)
            val serial_id = bundle.getString(WS_BUNDLE_SERIAL_CODE, "")
            val profile_check = bundle.getInt(
                WS_BUNDLE_PROFILE_CHECK,
                0
            ) //
            //
            processSO_Sync(ToolBox_Con.getPreference_Customer_Code(applicationContext), product_code, serial_id, profile_check)
        } catch (e: Exception) {
            sb = ToolBox_Inf.wsExceptionTreatment(applicationContext, e)
            ToolBox_Inf.registerException(javaClass.name, e)
            ToolBox_Inf.sendBCStatus(applicationContext, "ERROR_1", sb.toString(), "", "0")
        } finally {
            WBR_SO_Sync.completeWakefulIntent(intent)
        }
    }

    private fun processSO_Sync(customerCode: Long,productCode: Long, serialId: String?, profileCheck: Int) {
        val soDao = SM_SODao(applicationContext)
        soList.addAll(soDao.getSoSyncList(customerCode))
        //
        processedSoSize = 0
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            """${hmAuxTrans["generic_sending_data_msg"]}""",
            "",
            "0"
        )

        if (!sendSoToSync(
                productCode,
                serialId,
                soDao,
                customerCode,
                profileCheck,
                soList.size,
                getNextSoPage()
            )
        ){
            return
        }
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            hmAuxTrans["msg_processing_list"],
            "",
            "0"
        )
        //
        ToolBox_Inf.scheduleDownloadPdfWork(applicationContext)
        ToolBox_Inf.scheduleDownloadPictureWork(applicationContext)
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "CLOSE_ACT",
            hmAuxTrans["generic_process_finalized_msg"],
            HMAux(),
            "",
            "0"
        )
    }

    private fun sendSoToSync(
        productCode: Long,
        serialId: String?,
        soDao: SM_SODao,
        customerCode: Long,
        profileCheck: Int,
        soSyncListSize: Int,
        processList: List<SM_SO>
    ): Boolean {
        //
        if(processList.isNotEmpty()) {
            env.app_code = Constant.PRJ001_CODE
            env.app_version = Constant.PRJ001_VERSION
            env.session_app = ToolBox_Con.getPreference_Session_App(applicationContext)
            env.product_code = productCode
            env.serial_id = serialId
            env.so_mult = formatSyncSoList(processList)
            env.setProfile_check(profileCheck)
            env.app_type = Constant.PKG_APP_TYPE_DEFAULT
            ToolBox.sendBCStatus(
                applicationContext,
                "STATUS",
                """${hmAuxTrans["generic_sending_data_msg"]} ($processedSoSize/${soSyncListSize})""",
                "",
                "0"
            )
            //
            val resultado = ToolBox_Con.connWebService(
                Constant.WS_SO_SEARCH,
                gson.toJson(env)
            )
            //
            //
            val rec = gson.fromJson(
                resultado,
                TSO_Search_Rec::class.java
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
                return false
            }
            //
            updateLocalSo(rec, soDao, soSyncListSize)
            //
            return sendSoToSync(
                productCode,
                serialId,
                soDao,
                customerCode,
                profileCheck,
                soSyncListSize,
                getNextSoPage()
            )
        } else{
            return true
        }
    }

    private fun updateLocalSo(
        rec: TSO_Search_Rec,
        soDao: SM_SODao,
        soSyncListSize: Int
    ) {
        for (sm_so in rec.so) {
            //Apaga SO completa
            soDao.removeFull(sm_so)
            //
            sm_so.setPK()
            //
            processedSoSize++
        }
        soDao.addUpdate(rec.so, false)
        //
        ToolBox.sendBCStatus(
            applicationContext,
            "STATUS",
            """${hmAuxTrans["generic_processing_data"]} ($processedSoSize/${soSyncListSize})""",
            "",
            "0"
        )
        //
    }

    private fun loadTranslation() : HMAux {
        val translist = listOf(
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
        const val WS_SO_SYNC_PAGE = 10
        const val WS_BUNDLE_PRODUCT_CODE = "PRODUCT_CODE"
        const val WS_BUNDLE_SERIAL_CODE = "SERIAL_CODE"
        const val WS_BUNDLE_PROFILE_CHECK = "PROFILE_CHECK"
    }

    private fun getNextSoPage():List<SM_SO>{
        val startIndex = pageNumber * WS_SO_SYNC_PAGE
        val endIndex = minOf(startIndex + WS_SO_SYNC_PAGE, soList.size)
        pageNumber+=1
        return if (startIndex < endIndex) {
            soList.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
    }
}