package com.namoadigital.prj001.model.VH_models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086MaterialItemBinding
import com.namoadigital.prj001.model.Act086MaterialItem

class Act086MaterialItemVH(
    private val binding: Act086MaterialItemBinding,
    private val onProductItemClick: (position: Int, materialItem: Act086MaterialItem) -> Unit,
    private val onDeleteIconClick: (position: Int) -> Unit,
    private val inReadonly: Boolean,
    private val plannedQtyLbl: String,
    private val appliedQtyLbl: String
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(materialItem: Act086MaterialItem){
        //Clicks
        binding.act086ProductItemIvDelete.apply {
            if (!inReadonly) {
                setOnClickListener {
                    onDeleteIconClick(adapterPosition)
                }
            }else{
                visibility = View.GONE
            }
        }
        //
        binding.act086ProductItemClProductInfo.setOnClickListener {
            if(!inReadonly) {
                onProductItemClick(adapterPosition, materialItem)
            }
        }
        //Bind das infos
        with(binding){
            act086ProductItemTvProductDesc.text = materialItem.getFormattedMaterialDesc()
            //
            act086ProductItemTvMaterialPlannedQty.apply{
                text = materialItem.getFormttedPlannedQty(plannedQtyLbl)
                visibility = if(text.toString().trim().isNotEmpty()) View.VISIBLE else View.GONE
            }
            act086ProductItemTvMaterialAppliedQty.apply {
                text = materialItem.getFormttedQty(appliedQtyLbl)
                visibility = if(text.toString().trim().isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
    }