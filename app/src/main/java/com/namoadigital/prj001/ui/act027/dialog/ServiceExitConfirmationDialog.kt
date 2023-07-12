package com.namoadigital.prj001.ui.act027.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act027DialogServiceExecBinding
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class ServiceExitConfirmationDialog(
    context: Context,
    private val radioListener: OnServiceTypeSelectListener,
) : AlertDialog(context) {

    private val mResourceName = DIALOG_RESOURCE_NAME
    private val hmAux_trans by lazy {
        val transListDialog = ArrayList<String>()
        //
        transListDialog.add(DIALOG_BUTTON_OK)
        transListDialog.add(DIALOG_BUTTON_CANCEL)
        transListDialog.add(DIALOG_TITLE)
        transListDialog.add(DIALOG_SUBTITLE)
        transListDialog.add(DIALOG_RADIO_OPT)
        transListDialog.add(DIALOG_RADIO_OPT_TWO)
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
            //
            btnCancel.setOnClickListener {
                dismiss()
            }
        }

    }

    private fun setLabels() {
        with(binding) {
            btnOk.text = hmAux_trans[DIALOG_BUTTON_OK]
            btnCancel.text = hmAux_trans[DIALOG_BUTTON_CANCEL]
            tvServiceExecTtl.text = hmAux_trans[DIALOG_TITLE]
            tvServiceExecLbl.text = hmAux_trans[DIALOG_SUBTITLE]
            tvServiceExecLbl.visibility = View.VISIBLE
            tvServiceExecLbl.setTextColor(
                ResourcesCompat.getColor(
                    context.resources,
                    R.color.m3_namoa_onSurfaceVariant,
                    null
                )
            )
            rbDirectExecution.text = hmAux_trans[DIALOG_RADIO_OPT_TWO]
            rbStartExecution.text = hmAux_trans[DIALOG_RADIO_OPT]
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
        private const val DIALOG_RESOURCE_NAME = "service_exit_confirmation_dialog"
        private const val DIALOG_BUTTON_OK = "dialog_service_exit_btn_continue"
        private const val DIALOG_BUTTON_CANCEL = "dialog_service_exit_btn_cancel"
        private const val DIALOG_TITLE = "dialog_service_exit_type_ttl"
        private const val DIALOG_SUBTITLE = "dialog_service_exit_type_lbl"
        private const val DIALOG_RADIO_OPT = "dialog_service_exit_direct_opt"
        private const val DIALOG_RADIO_OPT_TWO = "dialog_service_exit_and_active_opt"
    }
}