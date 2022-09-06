package com.namoadigital.prj001.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act091BottomSheetListItemBinding
import com.namoadigital.prj001.extensions.MaskOnlyNumber
import com.namoadigital.prj001.model.SoPackExpressServicesLocal
import com.namoadigital.prj001.ui.act091.util.BottomListEvent
import com.namoadigital.prj001.ui.act091.util.onEvent

class Act091_BottomSheet_Item_Adapter constructor(
    private val dataset: List<SoPackExpressServicesLocal>,
    private val type: String,
    private val hmAux: HMAux,
    private val showPrice: Boolean,
    private val onUpdateList: () -> Unit,
) : RecyclerView.Adapter<Act091_BottomSheet_Item_Adapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(Act091BottomSheetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        return holder.onBinding(dataset[position], position)
    }

    override fun getItemCount() = dataset.size

    inner class ItemViewHolder constructor(
        private val binding: Act091BottomSheetListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun onBinding(item: SoPackExpressServicesLocal, position: Int) {
            with(binding) {
                setLabels(this)
                act091BottomSheetServiceComment.setText(item.comments)
                act091BottomSheetServiceTitle.text = item.service_desc_full
                if (type == "P") {
                    onEvent(BottomListEvent.stateWhenIsPackage(item, hmAux))
                }
                //


                if (item.manual_price == 0 && !showPrice) {
                    act091BottomSheetServicePrice.visibility = View.GONE
                } else {
                    act091BottomSheetServicePrice.visibility = View.VISIBLE
                }
                //
                act091BottomSheetServicePrice.apply {
                    setOnReportTextChangeListner(MaskOnlyNumber(this) {
                        if (it.isEmpty() || it == ".") {
                            item.price = null
                        } else {
                            item.price = it.replace(",", ".").toDouble()
                        }
                        onEvent(BottomListEvent.changePriceColor(it.isNotEmpty(), hmAux))
                        onUpdateList()

                    })
                }


                act091BottomSheetServiceComment.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
                    override fun reportTextChange(p0: String?) {
                    }

                    override fun reportTextChange(s: String?, p1: Boolean) {
                        item.comments = s.toString()

                    }

                })
            }
        }

        private fun setLabels(binding: Act091BottomSheetListItemBinding) {
            with(binding) {
                act091BottomSheetServiceTextLayoutComment.hint = hmAux["comment_hint"]
                act091BottomSheetServiceTextLayoutComment.placeholderText =
                    hmAux["insert_comment_placeholder"]

                act091BottomSheetServiceTextLayoutPrice.hint = hmAux["price_hint"]
                act091BottomSheetServiceTextLayoutPrice.placeholderText =
                    hmAux["insert_price_placeholder"]

                act091ServiceQtyBindings.act091BottomSheetQtyText.text = hmAux["qty_lbl"]
                act091ServiceQtyBindings.act091BottomSheetQtyText.background = null
            }
        }

    }
}


fun View.onHide() {
    this.visibility = View.GONE
}

fun View.onShow(){
    this.visibility = View.VISIBLE
}
