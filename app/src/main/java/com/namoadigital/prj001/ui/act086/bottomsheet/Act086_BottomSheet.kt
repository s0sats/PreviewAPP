package com.namoadigital.prj001.ui.act086.bottomsheet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act086BottomSheetBinding
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem

class Act086_BottomSheet constructor(
    private val buttonSelected: String,
    private val hmAux: HMAux,
    private val callAct090: (String) -> Unit,
    private val onRollBackRadioGroup: () -> Unit
)  : BottomSheetDialogFragment() {


    private val binding: Act086BottomSheetBinding by lazy {
        Act086BottomSheetBinding.inflate(layoutInflater)
    }

    private var exec_type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLabels()
        initActions()
    }

    override fun getTheme() = R.style.BottomSheetDialog_Rounded;

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(exec_type.isEmpty()){
            onRollBackRadioGroup()
            dialog.dismiss()
        }
    }


    private fun setLabels(){
        with(binding){
            act086BottomSheetTitle.text = hmAux["select_type_maintenance_lbl"]
            act086BottomSheetButtonAdjust.text = hmAux["adjust_lbl"]
            act086BottomSheetButtonChange.text = hmAux["change_lbl"]
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initActions(){
        with(binding){

            when(buttonSelected){
                GeOsDeviceItem.EXEC_TYPE_FIXED -> {
                    act086BottomSheetButtonChange.changeColorButtons()
                }

                GeOsDeviceItem.EXEC_TYPE_ADJUST -> {
                    act086BottomSheetButtonAdjust.changeColorButtons()
                }
            }

            act086BottomSheetButtonAdjust.setOnClickListener {
                exec_type = GeOsDeviceItem.EXEC_TYPE_ADJUST
                callAct090(GeOsDeviceItem.EXEC_TYPE_ADJUST)
                dialog?.dismiss()
            }

            act086BottomSheetButtonChange.setOnClickListener {
                exec_type = GeOsDeviceItem.EXEC_TYPE_FIXED
                callAct090(GeOsDeviceItem.EXEC_TYPE_FIXED)
                dialog?.dismiss()
            }
        }
    }

    private fun MaterialButton.changeColorButtons(){
        setBackgroundColor(resources.getColor(R.color.namoa_color_light_blue3))
    }

    companion object {


        fun getInstance(
            buttonSelected: String,
            hmAux: HMAux,
            callAct090: (String) -> Unit,
            onRollBackRadioGroup: () -> Unit
        ) = Act086_BottomSheet(buttonSelected, hmAux, callAct090, onRollBackRadioGroup)

    }
}