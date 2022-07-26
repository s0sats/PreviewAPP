package com.namoadigital.prj001.ui.act091.util

import android.annotation.SuppressLint
import com.google.android.material.textfield.TextInputLayout
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act091BottomSheetBinding
import com.namoadigital.prj001.model.Act091ServiceItem
import com.namoadigital.prj001.util.ToolBox_Inf

sealed class BottomEvent {

    data class changePriceColor(val value: Boolean) : BottomEvent()
    data class changeButtonLessQtyColor(val value: Boolean) : BottomEvent()
    data class changeStatePrice(val value: Boolean) : BottomEvent()
    data class OnUpdateBottomSheet(val item: Act091ServiceItem, val hmAux: HMAux) : BottomEvent()
}


@SuppressLint("UseCompatLoadingForColorStateLists", "UseCompatLoadingForDrawables", "ResourceType")
fun Act091BottomSheetBinding.onEvent(state: BottomEvent){

    when(state){
        is BottomEvent.changePriceColor -> {
            this.act091BottomSheetOk.isEnabled = state.value
            if(state.value){
                this.act091BottomSheetTextLayoutPrice.changeColorTextLayout(R.color.namoa_light_blue)
            }else{
                this.act091BottomSheetTextLayoutPrice.changeColorTextLayout(R.color.edit_text_color_required)
            }
        }

        is BottomEvent.changeButtonLessQtyColor -> {
            this.act091QtyBindings.act091BottomSheetLess.isEnabled = state.value
        }

        is BottomEvent.changeStatePrice -> {
            this.act091BottomSheetPrice.isEnabled = state.value
        }

        is BottomEvent.OnUpdateBottomSheet -> {
            var total = 0.0
            val item = state.item

            if (item.type_ps == "P") {
                act091BottomSheetPrice.isEnabled = false
                act091BottomSheetTextLayoutPrice.isHelperTextEnabled = true
                act091BottomSheetTextLayoutPrice.helperText = "${state.hmAux["services_below_lbl"]}"

                item.serviceList.forEach { obj ->
                    if (obj.price == null) {
                        act091BottomSheetTextLayoutPrice.placeholderText = "${state.hmAux["incomplete_placeholder"]}"
                        act091BottomSheetPrice.setText("")
                        act091BottomSheetOk.isEnabled = false
                        return
                    } else {
                        total += obj.price
                        act091BottomSheetOk.isEnabled = true
                    }
                }
                act091BottomSheetOk.isEnabled = true
                act091BottomSheetPrice.setText(
                    ToolBox_Inf.formatDoublePriceToScreen(total).toString()
                )
            }else{
                item.price?.let { price ->
                    act091BottomSheetPrice.setText(ToolBox_Inf.formatDoublePriceToScreen(price).toString())
                }
            }
        }
    }

}



@SuppressLint("UseCompatLoadingForColorStateLists")
fun TextInputLayout.changeColorTextLayout(color: Int = R.color.namoa_light_blue){
    this.boxStrokeColor = resources.getColor(color)
    this.hintTextColor = resources.getColorStateList(color)
    this.placeholderTextColor = resources.getColorStateList(color)

}