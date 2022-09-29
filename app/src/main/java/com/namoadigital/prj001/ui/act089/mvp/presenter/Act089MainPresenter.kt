package com.namoadigital.prj001.ui.act089.mvp.presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.namoadigital.prj001.R
import com.namoadigital.prj001.receiver.WBR_Upload_Support
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act089.Act089MainContract
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp.*
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act089MainPresenter(
    private val context: Context,
    private val mView: Act089MainContract.I_View
) : Act089MainContract.I_Presenter  {


    override fun sendSupport(support_contact:String, support_msg:String ) {
        if (ToolBox_Con.isOnline(context, true)) {
            //
            mView.setWsProcess(Act005_Main.WS_PROCESS_SUPPORT)
            mView.showPD(
                context.getString(R.string.progress_support_ttl),
                context.getString(R.string.progress_support_msg)
            )
            //
            val mIntent = Intent(context, WBR_Upload_Support::class.java)
            val bundle = Bundle()
            bundle.putString(WS_SUPPORT_MSG, support_msg)
            bundle.putString(WS_SUPPORT_CONTACT, support_contact)
            mIntent.putExtras(bundle)
            //
            context.sendBroadcast(mIntent)
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }


    private val dbListErrors = mutableListOf<Triple<String, String, Int>>(
        Triple(ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), DB_MULTI_STATUS_ERROR, Constant.DB_VERSION_CUSTOM),
        Triple(DB_FULL_BASE, DB_BASE_STATUS_ERROR, Constant.DB_VERSION_BASE),
        Triple(DB_FULL_CHAT, DB_CHAT_STATUS_ERROR, Constant.DB_VERSION_CHAT),
    )

    override fun getDbError(): String {

        val dbCorrupted = mutableListOf<String>()
        val separator = "/"

        dbListErrors.forEach { (db, status_error, version) ->

            if(checkIfDbCorrupted(status_error)){

                val list = db.split(separator)
                dbCorrupted.add("${list[list.lastIndex]} ~ |v$version|")

            }
        }

        return dbCorrupted.joinToString()
    }


    private fun checkIfDbCorrupted(status_error: String) = ToolBox_Con.getBooleanPreferencesByKey(context, status_error, false)

    override fun rebuildDatabase() {

        dbListErrors.forEach { (db, status_error, _) ->
            if(checkIfDbCorrupted(status_error)){
                context.deleteDatabase(db)
                ToolBox_Con.setBooleanPreference(
                    context,
                    status_error,
                    false
                )
            }
        }


/*        if(ToolBox_Con.getBooleanPreferencesByKey(
                context,
                DB_MULTI_STATUS_ERROR,
                false
        )){
            context.deleteDatabase(ToolBox_Con.customDBPath(
                ToolBox_Con.getPreference_Customer_Code(context)
            ));
            //
            ToolBox_Con.setBooleanPreference(
                context,
                DB_MULTI_STATUS_ERROR,
                false
            )
        }

        if(ToolBox_Con.getBooleanPreferencesByKey(
                context,
                DB_BASE_STATUS_ERROR,
                false
        )){

            context.deleteDatabase(
                DB_FULL_BASE
            );
            //
            ToolBox_Con.setBooleanPreference(
                context,
                DB_BASE_STATUS_ERROR,
                false
            )
        }

        if(ToolBox_Con.getBooleanPreferencesByKey(
                context,
                DB_CHAT_STATUS_ERROR,
                false
        )){
            context.deleteDatabase(DB_FULL_CHAT);
            //
            ToolBox_Con.setBooleanPreference(
                context,
                DB_CHAT_STATUS_ERROR,
                false
            )
        }*/
    }

}