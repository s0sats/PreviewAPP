package com.namoadigital.prj001.ui.act086.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act086BottomSheetBinding
import com.namoadigital.prj001.ui.act086.bottomsheet.model.BottomState
import kotlin.reflect.KMutableProperty0

class Act086_BottomSheet constructor(
    private val callAct090: (String) -> Unit,
    private val onRollBackRadioGroup: () -> Unit
)  : BottomSheetDialogFragment() {


    private val binding: Act086BottomSheetBinding by lazy {
        Act086BottomSheetBinding.inflate(layoutInflater)
    }

    private var bottomState = BottomState()

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
        initActions()
    }

    override fun getTheme() = R.style.BottomSheetDialog_Rounded;

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(bottomState.buttonSelected.isEmpty()){
            onRollBackRadioGroup()
            dialog.dismiss()
        }
    }


    private fun initActions(){
        with(binding){
            act086BottomSheetButtonAdjust.setOnClickListener {
                bottomState = bottomState.copy(
                    buttonSelected = act086BottomSheetButtonAdjust.text as String
                )
                dialog?.dismiss()
                callAct090(bottomState.buttonSelected)
            }
        }
    }

    companion object {
        fun getInstance(
            callAct090: (String) -> Unit,
            onRollBackRadioGroup: () -> Unit
        ) = Act086_BottomSheet(callAct090, onRollBackRadioGroup)

    }
}