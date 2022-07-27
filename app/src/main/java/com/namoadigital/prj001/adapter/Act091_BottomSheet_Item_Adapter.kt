package com.namoadigital.prj001.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.Act091BottomSheetListItemBinding
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj
import com.namoadigital.prj001.ui.act091.util.BottomEvent
import com.namoadigital.prj001.ui.act091.util.BottomListEvent
import com.namoadigital.prj001.ui.act091.util.onEvent
import java.text.NumberFormat

class Act091_BottomSheet_Item_Adapter constructor(
    private val dataset: List<TSO_Service_Search_Detail_Obj>,
    private val type: String,
    private val hmAux: HMAux,
    private val onUpdateList: (List<TSO_Service_Search_Detail_Obj>) -> Unit,
) : RecyclerView.Adapter<Act091_BottomSheet_Item_Adapter.ItemViewHolder>() {


    val listCustomEdit = dataset.toMutableList()


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
        fun onBinding(item: TSO_Service_Search_Detail_Obj, position: Int){
            with(binding) {
                setLabels(this)

                act091BottomSheetServiceTitle.text = item.service_desc_full
                if(type == "P"){
                    onEvent(BottomListEvent.stateWhenIsPackage(item, hmAux))
                }


                act091BottomSheetServicePrice.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                        onEvent(BottomListEvent.changePriceColor(char?.isNotEmpty()!!, hmAux))
                    }

                    override fun afterTextChanged(editable: Editable) {
                        editable.toString().let {
                            if (it.contains(",")) {
                                binding.act091BottomSheetServicePrice.keyListener =
                                    DigitsKeyListener.getInstance("0123456789")
                            } else {
                                binding.act091BottomSheetServicePrice.keyListener =
                                    DigitsKeyListener.getInstance("0123456789,")
                            }

                            if (it.isEmpty()) {
                                listCustomEdit[position].price = null
                            } else {
                                listCustomEdit[position].price =
                                    it.toString().replace(",", ".").toDouble()
                            }
                            onUpdateList(listCustomEdit)
                        }
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
