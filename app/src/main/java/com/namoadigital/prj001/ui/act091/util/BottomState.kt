package com.namoadigital.prj001.ui.act091.util

import android.annotation.SuppressLint
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act091BottomSheetBinding
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.util.ToolBox_Inf

sealed class BottomState {

    data class ChangePriceColor(val value: Boolean, val hmAux: HMAux) : BottomState()
    data class ChangeButtonLessQtyColor(val value: Boolean) : BottomState()
    data class ChangeStatePrice(val value: Boolean) : BottomState()
    data class OnUpdateBottomSheet(val itemHeader: SoPackExpressPacksLocal, val hmAux: HMAux) : BottomState()
    data class HasPermissionShowPrice(val hasPermission: Boolean) : BottomState()
    data class ShowDelete(val show: Boolean) : BottomState()
}


@SuppressLint("UseCompatLoadingForColorStateLists", "UseCompatLoadingForDrawables", "ResourceType")
fun Act091BottomSheetBinding.onState(state: BottomState){

    when(state){
        is BottomState.ChangePriceColor -> {
            this.act091BottomSheetOk.isEnabled = state.value
            if(state.value){
                act091BottomSheetTextLayoutPrice.changeColorTextLayout(R.color.namoa_light_blue)
                act091BottomSheetTextLayoutPrice.isHelperTextEnabled = false
            }else{
                act091BottomSheetTextLayoutPrice.isHelperTextEnabled = true
                act091BottomSheetTextLayoutPrice.helperText = "${state.hmAux["required_lbl"]}"
                act091BottomSheetTextLayoutPrice.changeColorTextLayout(R.color.edit_text_color_required)
            }
        }

        is BottomState.ChangeButtonLessQtyColor -> {
            this.act091QtyBindings.act091BottomSheetLess.isEnabled = state.value
        }

        is BottomState.ChangeStatePrice -> {
            this.act091BottomSheetPrice.isEnabled = state.value
        }

        is BottomState.OnUpdateBottomSheet -> {
            var total = 0.0
            val item = state.itemHeader
            act091QtyBindings.act091BottomSheetQty.setText("${item.qty}")
            if (item.type_ps == "P") {
                act091BottomSheetPrice.isEnabled = false
                act091BottomSheetTextLayoutPrice.isHelperTextEnabled = true
                act091BottomSheetTextLayoutPrice.helperText = "${state.hmAux["services_below_lbl"]}"

                item.serviceList.forEach { obj ->
                    obj.price?.let {
                        total += it
                        act091BottomSheetOk.isEnabled = true
                    } ?: let {
                        act091BottomSheetTextLayoutPrice.placeholderText = "${state.hmAux["incomplete_placeholder"]}"
                        act091BottomSheetPrice.setText("")
                        act091BottomSheetOk.isEnabled = false
                    }
                }
                act091BottomSheetOk.isEnabled = true
                act091BottomSheetPrice.setText(
                    ToolBox_Inf.formatDoublePriceToScreen(total).toString()
                )
            }else{
                item.price?.let { price ->
                    act091BottomSheetPrice.setText(ToolBox_Inf.formatDoublePriceToScreen(price).toString())
                    return
                }
                act091BottomSheetTextLayoutPrice.isHelperTextEnabled = true
                act091BottomSheetTextLayoutPrice.helperText = "${state.hmAux["required_lbl"]}"
            }
        }

        is BottomState.HasPermissionShowPrice -> {
            if(state.hasPermission){
                act091BottomSheetTextLayoutPrice.visibility = View.VISIBLE
            }
        }

        is BottomState.ShowDelete -> {
            if(state.show){
                act091BottomSheetDelete.visibility = View.VISIBLE
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