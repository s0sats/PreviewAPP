package com.namoadigital.prj001.ui.act027.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act027DialogServiceExecBinding
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class ServiceExecConfirmationDialog(
    context: Context,
    private val mListener: OnServiceTypeSelectListener,
    private val sService: HMAux
): Dialog(context)  {

    private val mResourceName = "service_exec_confirmation_dialog"
    private val hmAux_trans  by lazy{
        var transListDialog = ArrayList<String>();
        //
        transListDialog.add("dialog_service_execution_type_ttl")
        transListDialog.add("dialog_service_execution_direct_opt")
        transListDialog.add("dialog_service_execution_with_attachments_opt")

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
                mListener.onSelected(sService, rbStartExecution.isChecked)
                dismiss()
            }
            //
            btnCancel.setOnClickListener {
                dismiss()
            }
        }

    }

    private fun setLabels() {
        binding.apply {
            btnOk.text = hmAux_trans["sys_alert_btn_ok"]
            btnCancel.text = hmAux_trans["sys_alert_btn_cancel"]
            tvServiceExecTtl.text = hmAux_trans["dialog_service_execution_type_ttl"]
            rbDirectExecution.text = hmAux_trans["dialog_service_execution_direct_opt"]
            rbStartExecution.text = hmAux_trans["dialog_service_execution_with_attachments_opt"]
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    interface OnServiceTypeSelectListener {
        fun onSelected(sService: HMAux, addAttachments: Boolean)
    }
}