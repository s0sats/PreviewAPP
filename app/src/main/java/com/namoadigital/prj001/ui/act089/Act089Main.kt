package com.namoadigital.prj001.ui.act089

import android.os.Bundle
import android.view.WindowManager
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act089MainBinding
import com.namoadigital.prj001.databinding.Act089MainContentBinding
import com.namoadigital.prj001.extensions.logout
import com.namoadigital.prj001.model.SupportDialogFields
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.view.dialog.SupportDialog

class Act089Main : Base_Activity(), Act089MainContract.I_View {

    private var wsProcess: String = ""
    private lateinit var binding: Act089MainContentBinding
    private val mPresenter: Act089MainContract.I_Presenter by lazy {
        Act089MainPresenter(
            context,
            this
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act089MainBinding.inflate(layoutInflater)
        binding = mainBinding.act089MainContent
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        iniSetup()
        iniLabels()
        initActions()
    }

    private fun iniLabels() {
        binding.apply {
            tvSupportDisclaimer.setText(R.string.database_error_msg)
            tvRecoveryDataBtn.setText(R.string.recovery_database_btn)
            tvSupportDataBtn.setText(R.string.send_support_btn)
        }
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
                    or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    fun initActions() {
        super.iniActions()
        val supportDialogFields = SupportDialogFields(
            context.getString(R.string.alert_support_msg),
            context.getString(R.string.alert_support_contact),
            context.getString(R.string.alert_support_hint),
            context.getString(R.string.alert_support_contact_hint),
            context.getString(R.string.alert_support_ttl),
            context.getString(R.string.alert_support_empty_contact),
            context.getString(R.string.alert_support_empty_msg)
        )
        binding.tvSupportDataBtn.setOnClickListener {
            SupportDialog(
                context,
                supportDialogFields
            ) { support_contact: String, support_msg: String ->
                mPresenter.sendSupport(
                    support_msg.trim { it <= ' ' },
                    support_contact.trim { it <= ' ' })

            }.show()

        }
        binding.tvRecoveryDataBtn.setOnClickListener {
            mPresenter.rebuildDatabase(context)
        }
    }

    override fun processCloseACT(mLink: String?, mRequired: String?) {
        super.processCloseACT(mLink, mRequired)
        if(wsProcess.equals(Act005_Main.WS_PROCESS_SUPPORT)){
            ToolBox.alertMSG(
                context,
                context.getString(R.string.alert_support_finish_ttl),
                context.getString(R.string.alert_support_finish_msg),
                null,
                0
            )
        }
        progressDialog.dismiss()
    }

    override fun callLogout() {
        logout()
    }

    override fun setWsProcess(wsProcessSupport: String) {
        wsProcess = wsProcessSupport
    }

    override fun showPD(ttl: String, msg: String) {
        //
        enableProgressDialog(
            ttl,
            msg,
            context.getString(R.string.sys_alert_btn_cancel),
            context.getString(R.string.sys_alert_btn_ok)
        )
    }

}