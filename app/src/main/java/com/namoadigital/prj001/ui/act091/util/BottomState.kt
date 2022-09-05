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
    data class OnUpdateService(val itemHeader: SoPackExpressPacksLocal) : BottomState()
    data class OnUpdateBottomSheet(val itemHeader: SoPackExpressPacksLocal, val hmAux: HMAux) : BottomState()
    data class HasPermissionShowPrice(val hasPermission: Boolean) : BottomState()
    data class ShowDelete(val show: Boolean) : BottomState()
}


@SuppressLint("UseCompatLoadingForColorStateLists", "UseCompatLoadingForDrawables", "ResourceType")
fun Act091BottomSheetBinding.onState(state: BottomState){

    when(state){
        is BottomState.ChangePriceColor -> {
            act091BottomSheetOk.isEnabled = state.value
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
            act091QtyBindings.act091BottomSheetLess.isEnabled = state.value
        }

        is BottomState.ChangeStatePrice -> {
            act091BottomSheetPrice.isEnabled = state.value
        }

        is BottomState.OnUpdateService -> {
            val item = state.itemHeader

            if(item.type_ps == "S"){
                item.price?.let {
                    act091BottomSheetPrice.setText(ToolBox_Inf.formatDoublePriceToScreen(it).toString())
                }
            }
        }

        is BottomState.OnUpdateBottomSheet -> {
            var total = 0.0
            val item = state.itemHeader
            act091QtyBindings.act091BottomSheetQty.setText("${item.qty}")
            if (item.type_ps == "P") {
                act091BottomSheetPrice.isEnabled = false
                act091BottomSheetTextLayoutPrice.isHelperTextEnabled = true
                act091BottomSheetTextLayoutPrice.helperText = "${state.hmAux["services_below_lbl"]}"
                item.serviceList.let { service ->
                    if (service.isNotEmpty()) {
                        service.map { m -> m.price == null }.let { bl ->
                            if (bl.contains(true)) {
                                act091BottomSheetTextLayoutPrice.editText?.setText("${state.hmAux["incomplete_placeholder"]}")
                                act091BottomSheetOk.isEnabled = false
                            } else {
                                service.forEach { obj ->
                                    obj.price?.let {
                                        total += it
                                        act091BottomSheetOk.isEnabled = true
                                        act091BottomSheetPrice.setText(ToolBox_Inf.formatDoublePriceToScreen(total).toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                onState(BottomState.ChangePriceColor(item.price != null, state.hmAux))
            }
        }

        is BottomState.HasPermissionShowPrice -> {
            if(state.hasPermission){
                act091BottomSheetTextLayoutPrice.visibility = View.GONE
            }else{
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
    boxStrokeColor = resources.getColor(color)
    hintTextColor = resources.getColorStateList(color)
    placeholderTextColor = resources.getColorStateList(color)

}