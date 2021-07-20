package com.namoadigital.prj001.service

import android.app.IntentService
import android.content.Intent
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_List
import com.namoadigital.prj001.util.ToolBox_Inf

class WS_Workgroup_Member_List :
    IntentService("WS_Workgroup_Member_List")
{
    override fun onHandleIntent(intent: Intent?) {
        var sb = StringBuilder()
        //
        try {

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
            WBR_Workgroup_Member_List.completeWakefulIntent(intent)
        }
    }
}