package com.namoadigital.prj001.ui.act091.util

import android.annotation.SuppressLint
import android.view.View
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act091BottomSheetListItemBinding
import com.namoadigital.prj001.model.SoPackExpressServicesLocal
import com.namoadigital.prj001.ui.act091.mvp.Utils.onHide
import com.namoadigital.prj001.util.ToolBox_Inf

sealed class BottomListEvent {

    data class changePriceColor(val value: Boolean, val hmAux: HMAux) : BottomListEvent()
    data class stateWhenIsPackage(val item: SoPackExpressServicesLocal, val showPrice: Boolean, val hmAux: HMAux) : BottomListEvent() {
        val manual_price = item.manual_price == 1
    }
}

@SuppressLint("ResourceType", "UseCompatLoadingForColorStateLists")
fun Act091BottomSheetListItemBinding.onEvent(event: BottomListEvent) {

    when (event) {

        is BottomListEvent.changePriceColor -> {
            if (event.value) {
                act091BottomSheetServiceTextLayoutPrice.changeColorTextLayout(R.color.namoa_light_blue)
                act091BottomSheetServiceTextLayoutPrice.isHelperTextEnabled = false
            } else {
                act091BottomSheetServiceTextLayoutPrice.isHelperTextEnabled = true
                act091BottomSheetServiceTextLayoutPrice.helperText =
                    "${event.hmAux["required_lbl"]}"
                act091BottomSheetServiceTextLayoutPrice.changeColorTextLayout(R.color.edit_text_color_required)
            }
        }


        is BottomListEvent.stateWhenIsPackage -> {

            val item = event.item
            onEvent(BottomListEvent.changePriceColor(event.manual_price && item.price != null , event.hmAux))

            with(act091ServiceQtyBindings) {
                act091BottomSheetLess.onHide()
                act091BottomSheetMost.onHide()
                act091BottomSheetQty.setText("${event.item.qty}")
                act091BottomSheetQty.background = null
                act091BottomSheetQty.setTextColor(root.resources.getColor(R.color.namoa_color_gray_8))
                act091BottomSheetQty.isEnabled = false
            }
            act091BottomSheetServiceTextLayoutPrice.apply {
                placeholderTextColor = root.resources.getColorStateList(R.color.namoa_color_gray_9)

                isHelperTextEnabled = event.manual_price && item.price == null

                isEnabled = event.manual_price

                visibility = if (item.manual_price == 0 && !event.showPrice) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            }
            act091BottomSheetServicePrice.apply {
                isEnabled = event.manual_price

                if (event.manual_price && item.price == null) {
                    setText("")
                } else {
                    setText(ToolBox_Inf.formatDoublePriceToScreen(item.price).toString())
                    setSelectAllOnFocus(true)
                    if (!event.manual_price) {
                        setTextColor(root.resources.getColor(R.color.namoa_color_gray_7))
                    }
                }
            }
        }
    }


}
