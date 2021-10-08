package com.namoadigital.prj001.ui.act011.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act011CheckDialogBinding

class Act011CheckDialog(
    context: Context,
    hmAuxTrans: HMAux,
    val listener: OnInteractListener
) : AlertDialog(context) {

    private var _binding: Act011CheckDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        _binding = Act011CheckDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        setLabels()
        //
        initValues()
        //
        iniActions()
    }

    private fun iniActions() {
        TODO("Not yet implemented")
    }

    private fun initValues() {
        TODO("Not yet implemented")
    }

    private fun setLabels() {
        TODO("Not yet implemented")
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    interface OnInteractListener {
        fun onApply(periodFilter: String, siteFilter: String, focusFilter: String)
    }
}