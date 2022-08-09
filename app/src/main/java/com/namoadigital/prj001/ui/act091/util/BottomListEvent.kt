package com.namoadigital.prj001.ui.act091.util

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.onHide
import com.namoadigital.prj001.databinding.Act091BottomSheetListItemBinding
import com.namoadigital.prj001.model.SOExpressItemDetail
import com.namoadigital.prj001.model.SoPackExpressServicesLocal
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj
import com.namoadigital.prj001.util.ToolBox_Inf

sealed class BottomListEvent {

    data class changePriceColor(val value: Boolean, val hmAux: HMAux) : BottomListEvent()
    data class stateWhenIsPackage(val item: SoPackExpressServicesLocal, val hmAux: HMAux) : BottomListEvent()
}

fun Act091BottomSheetListItemBinding.onEvent(event: BottomListEvent){

    when(event){

        is BottomListEvent.changePriceColor -> {
            if(event.value){
                act091BottomSheetServiceTextLayoutPrice.changeColorTextLayout(R.color.namoa_light_blue)
                act091BottomSheetServiceTextLayoutPrice.isHelperTextEnabled = false
            }else{
                act091BottomSheetServiceTextLayoutPrice.isHelperTextEnabled = true
                act091BottomSheetServiceTextLayoutPrice.helperText = "${event.hmAux["required_lbl"]}"
                act091BottomSheetServiceTextLayoutPrice.changeColorTextLayout(R.color.edit_text_color_required)
            }
        }


        is BottomListEvent.stateWhenIsPackage -> {

            onEvent(BottomListEvent.changePriceColor(event.item.manual_price == 0, event.hmAux))

            act091ServiceQtyBindings.act091BottomSheetLess.onHide()
            act091ServiceQtyBindings.act091BottomSheetMost.onHide()

            event.item.price?.let {
                act091BottomSheetServicePrice.setText(ToolBox_Inf.formatDoublePriceToScreen(it))
                act091BottomSheetServicePrice.isEnabled = false
            }
            act091ServiceQtyBindings.act091BottomSheetQty.setText("${event.item.qty}")
            act091ServiceQtyBindings.act091BottomSheetQty.isEnabled = false


        }


    }

}
