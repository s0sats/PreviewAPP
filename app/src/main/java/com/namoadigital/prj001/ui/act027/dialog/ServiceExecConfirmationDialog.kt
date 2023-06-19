package com.namoadigital.prj001.ui.act027.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.databinding.Act027DialogServiceExecBinding
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf

class ServiceExecConfirmationDialog(
    context: Context,
    private val mListener: OnServiceTypeSelectListener
): Dialog(context)  {

    private val mResourceName = "service_exec_confirmation_dialog"
    private val hmAux_trans  by lazy{
        var transListDialog = ArrayList<String>();
        //
        transListDialog.add("filter_lbl")
        transListDialog.add("rg_period_lbl")
        transListDialog.add("until_next_action_opt")
        transListDialog.add("until_next_week_opt")
        transListDialog.add("until_today_opt")
        transListDialog.add("all_periods_opt")
        transListDialog.add("current_site_opt")
        transListDialog.add("all_sites_opt")
        transListDialog.add("only_my_action_opt")
        transListDialog.add("all_action_opt")
        transListDialog.add("period_filter_lbl")
        transListDialog.add("site_filter_lbl")
        transListDialog.add("focus_filter_lbl")
        transListDialog.add("btn_save_lbl")
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
                mListener.onSelected(rbStartExecution.isChecked)
            }
            //
            btnCancel.setOnClickListener {
                dismiss()
            }
        }

    }

    private fun setLabels() {
        binding.apply {
            btnOk.text = hmAux_trans[""]
            btnCancel.text = hmAux_trans[""]
            tvServiceExecTtl.text = hmAux_trans[""]
            rbDirectExecution.text = hmAux_trans[""]
            rbStartExecution.text = hmAux_trans[""]
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    interface OnServiceTypeSelectListener {
        fun onSelected(addAttachments: Boolean)
    }
}