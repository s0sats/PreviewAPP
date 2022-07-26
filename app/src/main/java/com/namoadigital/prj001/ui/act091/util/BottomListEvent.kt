package com.namoadigital.prj001.ui.act091.util

import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.onHide
import com.namoadigital.prj001.databinding.Act091BottomSheetListItemBinding
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj
import com.namoadigital.prj001.util.ToolBox_Inf

sealed class BottomListEvent {

    data class changePriceColor(val value: Boolean) : BottomListEvent()
    data class stateWhenIsPackage(val item: TSO_Service_Search_Detail_Obj) : BottomListEvent()
}

fun Act091BottomSheetListItemBinding.onEvent(event: BottomListEvent){

    when(event){

        is BottomListEvent.changePriceColor -> {
            if(event.value){
                act091BottomSheetServiceTextLayoutPrice.changeColorTextLayout(R.color.namoa_light_blue)
            }else{
                act091BottomSheetServiceTextLayoutPrice.changeColorTextLayout(R.color.edit_text_color_required)
            }
        }


        is BottomListEvent.stateWhenIsPackage -> {

            onEvent(BottomListEvent.changePriceColor(event.item.manual_price == 0))

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
