package com.namoadigital.prj001.ui.act027.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.namoadigital.prj001.databinding.Act027DialogServiceExecBinding

class ServiceExecConfirmationDialog(context: Context): Dialog(context)  {

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
        TODO("Not yet implemented")
    }

    private fun setLabels() {
        TODO("Not yet implemented")
    }
}