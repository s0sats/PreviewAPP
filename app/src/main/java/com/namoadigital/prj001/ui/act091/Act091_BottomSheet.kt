package com.namoadigital.prj001.ui.act091

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act091BottomSheetBinding
import com.namoadigital.prj001.model.Act091ServiceItem
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlin.properties.Delegates

class Act091_BottomSheet constructor(
) : BottomSheetDialogFragment(){

    private val binding: Act091BottomSheetBinding by lazy {
        Act091BottomSheetBinding.inflate(layoutInflater)
    }

    private lateinit var contentItem: Act091ServiceItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contentItem = Gson().fromJson(it.getString(SERVICE_ITEM), Act091ServiceItem::class.java)
        }
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
        initLabels()
        initVars()
        initAction()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog_Rounded;
    }


    private fun initLabels(){
        with(binding){
            act091BottomSheetTitle.text = contentItem.name
            act091BottomSheetPrice.setText(ToolBox_Inf.formatDoublePriceToScreen(contentItem.price))
            act091QtyBindings.act091BottomSheetQty.setText("${contentItem.qty}")
        }

    }

    private fun initVars(){
    }

    private fun initAction(){
        with(binding.act091QtyBindings){
            act091BottomSheetMost.setOnClickListener {
                val currentValue = act091BottomSheetQty.text.toString().toInt()
                act091BottomSheetQty.setText(currentValue.mostQty())
            }

            act091BottomSheetLess.setOnClickListener{
                val currentValue = act091BottomSheetQty.text.toString().toInt()
                act091BottomSheetQty.setText(currentValue.lessQty())
            }
        }

    with(binding){
        act091BottomSheetCancel.setOnClickListener {
            dismiss()
        }
    }

    }

    private fun Int.lessQty(): String{
        var default = this
        default -= 1

        return if(default != -1) {
            "$default"
        }else{
            "$this"
        }
    }


    private fun Int.mostQty(): String{
        return "${this + 1}"
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialog.dismiss()
    }

    companion object {

        const val SERVICE_ITEM = "SERVICE_ITEM"

        fun getInstance(
            serviceItem: String
        ) = Act091_BottomSheet().apply {
            arguments = Bundle().apply {
                putString(SERVICE_ITEM, serviceItem)
            }
        }

    }
}