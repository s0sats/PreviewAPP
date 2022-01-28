package com.namoadigital.prj001.ui.act088

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.dao.CH_RoomDao
import com.namoadigital.prj001.databinding.Act088MainBinding
import com.namoadigital.prj001.databinding.Act088MainContentBinding
import com.namoadigital.prj001.ui.act001.Act001_Main
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act034.Act034_Main
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

/**
 * Act chamada ao clicar na notificação do chat.
 * Substitui o Notification receiver, pois a partir do android 12, um notificaão chamar um service ou
 * receiver que posteriormente chama um act, configurando uma notificação trampolin, não pode mais
 * ser usada.
 * Como existia uma logica para configurar como chamar a act034, foi criada essa act que define a con
 * figuração de chamada da act34...
 */

class Act088Main
    : Base_Activity(),
    Act088MainContract.I_View
{
    private lateinit var binding: Act088MainContentBinding
    private val mPresenter : Act088MainContract.I_Presenter by lazy{
        Act088MainPresenter(
            context,
            this,
            mModule_Code,
            mResource_Code
        )
    }

    private var customerCode = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act088MainBinding.inflate(layoutInflater)
        binding = mainBinding.act088MainContent
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        iniSetup()
        recoverIntentsInfo()
        iniTrans()
        iniUIFooter()
        processNotificationInfo()
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT088
        )
        //10/06/2021 - Add recolhimento do teclado
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                    or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }


    private fun recoverIntentsInfo() {
        intent?.let {
           customerCode = intent.getLongExtra(CH_RoomDao.CUSTOMER_CODE,-1L)
        }

    }

    private fun iniTrans() {
        hmAux_Trans = mPresenter.getTranslation()
    }

    private fun processNotificationInfo() {
        mPresenter.defineFlow(customerCode,hmActivityStatus)
    }

    override fun callAct001() {
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act088Main, Act001_Main::class.java)
            }
        )
        //
        finish()
    }

    private fun callAct005() {
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act088Main, Act005_Main::class.java)
            }
        )
        //
        finish()
    }

    override fun callAct034(bundle: Bundle, sendBroadcast: Boolean) {
        startActivity(
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                setClass(this@Act088Main, Act034_Main::class.java)
                putExtras(bundle)
            }
        )
        if(sendBroadcast){
            ToolBox.sendBCSpecialStatus(
                context,
                ConstantBase.MSG_SPECIAL_NOTIFICATION_CLOSE,
                "",
                "camera" + "#" + Constant.ACT035
            )
        }
        //
        finish()
    }


    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT088
        mAct_Title = "${Constant.ACT088}${ConstantBaseApp.title_lbl}"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux_Trans)
        setMenuLanguage(hmAux_Trans)
        setTitleLanguage()
        setFooter()
    }

    override fun footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context)
    }

    override fun onBackPressed() {
        callAct005()
    }
}