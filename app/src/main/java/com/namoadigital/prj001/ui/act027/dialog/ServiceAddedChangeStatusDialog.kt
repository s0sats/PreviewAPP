package com.namoadigital.prj001.ui.act027.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.namoadigital.prj001.databinding.Act027DialogServiceExecBinding
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class ServiceAddedChangeStatusDialog (
    context: Context,
    private val radioListener: OnServiceTypeSelectListener,
) : AlertDialog(context) {

    private val mResourceName = DIALOG_RESOURCE_NAME
    private val hmAux_trans by lazy {
        val transListDialog = ArrayList<String>()
        //
        transListDialog.add(DIALOG_TITLE)
        transListDialog.add(DIALOG_RADIO_OPT)
        transListDialog.add(DIALOG_RADIO_OPT_TWO)
        transListDialog.add(DIALOG_BUTTON_OK)
        //
        val mResourceCodeDialog = ToolBox_Inf.getResourceCode(
            context,
            ConstantBaseApp.APP_MODULE,
            mResourceName
        )
        //
        ToolBox_Inf.setLanguage(
            context,
            ConstantBaseApp.APP_MODULE,
            mResourceCodeDialog,
            ToolBox_Con.getPreference_Translate_Code(context),
            transListDialog
        )
    }

    private var _binding: Act027DialogServiceExecBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        _binding = Act027DialogServiceExecBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        setLabels()
        //
        iniActions()
    }

    private fun iniActions() {
        binding.apply {
            btnOk.setOnClickListener {
                radioListener.onSelected(rbStartExecution.isChecked)
                dismiss()
            }
        }
    }

    private fun setLabels() {
        with(binding) {
            btnOk.text = hmAux_trans[DIALOG_BUTTON_OK]
            tvServiceExecTtl.text = hmAux_trans[DIALOG_TITLE]
            tvServiceExecLbl.visibility = View.GONE
            rbDirectExecution.text = hmAux_trans[DIALOG_RADIO_OPT]
            rbStartExecution.text = hmAux_trans[DIALOG_RADIO_OPT_TWO]
            btnCancel.visibility = View.GONE
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    interface OnServiceTypeSelectListener {
        fun onSelected(activeTask: Boolean)
    }


    companion object {
        private const val DIALOG_RESOURCE_NAME = "service_added_change_status_dialog"
        private const val DIALOG_BUTTON_OK = "dialog_service_change_status_btn_continue"
        private const val DIALOG_TITLE = "dialog_service_change_status_ttl"
        private const val DIALOG_RADIO_OPT = "dialog_service_change_status_active_opt"
        private const val DIALOG_RADIO_OPT_TWO = "dialog_service_change_status_keep_status_opt"
    }
}