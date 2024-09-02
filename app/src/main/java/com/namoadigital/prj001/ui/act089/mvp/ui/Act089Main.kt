package com.namoadigital.prj001.ui.act089.mvp.ui

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act089MainBinding
import com.namoadigital.prj001.databinding.Act089MainContentBinding
import com.namoadigital.prj001.extensions.logout
import com.namoadigital.prj001.ui.act005.Act005_Main
import com.namoadigital.prj001.ui.act089.Act089MainContract
import com.namoadigital.prj001.ui.act089.mvp.presenter.Act089MainPresenter
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.text.SimpleDateFormat
import java.util.*

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
        /*setSupportActionBar(mainBinding.toolbar)*/
        //
        iniSetup()
        iniLabels()
        initActions()
    }

    private fun iniLabels() {
        binding.apply {
            tvSupportDisclaimer.setText(R.string.act089_database_error_ttl)
            tvSupportAccord.setText(R.string.act089_database_error_msg)
            btnRecoveryData.setText(R.string.act089_database_error_reset_btn)
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

        binding.btnRecoveryData.setOnClickListener {
            showConfirmDeleteDialog()
        }
    }

    override fun processCloseACT(mLink: String?, mRequired: String?) {
        super.processCloseACT(mLink, mRequired)
        if(wsProcess == Act005_Main.WS_PROCESS_SUPPORT){
            mPresenter.rebuildDatabase()
            ToolBox.alertMSG(
                context,
                context.getString(R.string.alert_support_finish_ttl),
                context.getString(R.string.alert_support_finish_msg),
                { dialog, _ ->
                    dialog.dismiss()
                    if(progressDialog.isShowing) progressDialog.dismiss()
                    logout(false)

                },
                0
            )
        }
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


    private fun showConfirmDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.act089_dialog_delete_warning, null)
        var drawable_ic: Drawable? = null
        val tv_title = view.findViewById<TextView>(R.id.act011_dialog_tv_title)
        val tv_msg = view.findViewById<TextView>(R.id.act011_dialog_tv_msg)
        val btn_ok = view.findViewById<TextView>(R.id.act011_dialog_btn_ok)
        val btn_cancel = view.findViewById<TextView>(R.id.act011_dialog_btn_cancel)
        val iv_error = view.findViewById<ImageView>(R.id.act011_dialog_iv_error)
        drawable_ic = context.resources.getDrawable(com.namoa_digital.namoa_library.R.drawable.ic_error_black_24dp)
        drawable_ic.setColorFilter(
            context.resources.getColor(R.color.namoa_color_danger_red),
            PorterDuff.Mode.SRC_ATOP
        )
        iv_error.setImageDrawable(drawable_ic)
        tv_title.text = getString(R.string.act089_dialog_database_reset_ttl)
        tv_msg.text = getString(R.string.act089_dialog_database_reset_msg)
        btn_ok.text = getString(R.string.act089_dialog_database_reset_confirm_btn)
        btn_cancel.text = getString(R.string.act089_dialog_database_reset_cancel_btn)
        builder.setView(view)
        builder.setCancelable(false)
        val show = builder.show()
        btn_cancel.setOnClickListener { show.dismiss() }
        btn_ok.setOnClickListener {
            val date = SimpleDateFormat("dd/MM/yyyy").format(Date())
            val hour = SimpleDateFormat("HH:mm:ss").format(Date())
            show.dismiss()
            mPresenter.sendSupport(
                "DATABASE_CRASH_AUTOMATIC_MESSAGE",
                "\nUser: ${ToolBox_Con.getPreference_User_Code(context)} -/- " +
                        "\nDatabase: ${mPresenter.getDbError()} -/- " +
                        "\nDate: $date -/- " +
                        "\nHour: $hour"
            )

        }
    }

}