package com.namoadigital.prj001.ui.act088

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.dao.CH_RoomDao
import com.namoadigital.prj001.receiver.NotificationReceiver
import com.namoadigital.prj001.ui.act034.Act034_Main
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class Act088MainPresenter(
    private val context: Context,
    private val mView: Act088MainContract.I_View,
    private val mModule_Code: String,
    private val mResource_Code: String
    ) : Act088MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    override fun getTranslation(): HMAux {
        return hmAuxTrans
    }

    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act088_title",
        )
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }

    /**
     * Fun que define a configuração dos params para chamada da act034.
     * Logica igual a que existia antigo serviço que processava o clique na notificação.
     */
    override fun defineFlow(customerCode: Long, mActivity: HMAux?) {
        if (ToolBox_Con.getPreference_User_Code(context) != ""
            && ToolBox_Con.getPreference_Customer_Code(context) != -1L
            && ToolBox_Con.getPreference_Site_Code(context) != "-1"
            && ToolBox_Con.getPreference_Operation_Code(context) != -1L
        ) {
            /**
             * luche 27/11/2020
             * Modificado execução do onReciver, condicionando ação a data estar valida, ou seja, se data invalçida o clique da notificação naonavega pra act do chat
             */
            if (ToolBox_Inf.isLocalDatetimeOk(context)) {
                val mCamera = Camera_Activity.hmActivityStatus
                val mCam = mCamera != null && mCamera["camera"] != null
                val mAct = mActivity != null && mActivity[ConstantBaseApp.ACT035] != null
                //
                val bundle = Bundle()
                if (customerCode > -1) {
                    bundle.putLong(
                        CH_RoomDao.CUSTOMER_CODE,
                        customerCode
                    )
                }
                //Se não esta na tela de foto ou act035 exibi a notificação.
                if (!mCam && !mAct) {
                    bundle.putBoolean(NotificationReceiver.NOTIFICATION, true)
                    mView.callAct034(bundle)
                } else {
                    bundle.putBoolean(NotificationReceiver.NOTIFICATION, Act034_Main.bTT)
                    mView.callAct034(bundle, sendBroadcast = true)
                }
            } else{
                mView.callAct001()
            }
        } else{
            mView.callAct001()
        }
    }
}