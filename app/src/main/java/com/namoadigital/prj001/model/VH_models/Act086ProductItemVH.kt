package com.namoadigital.prj001.model.VH_models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086ProductItemBinding
import com.namoadigital.prj001.model.Act086ProductItem
import com.namoadigital.prj001.util.ToolBox_Inf

class Act086ProductItemVH(
    private val binding: Act086ProductItemBinding,
    private val onProductItemClick: (position: Int, productItem: Act086ProductItem) -> Unit,
    private val onDeleteIconClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(productItem: Act086ProductItem){
        //Clicks
        binding.act086ProductItemIvDelete.setOnClickListener {
            onDeleteIconClick(adapterPosition)
        }
        //
        binding.act086ProductItemClProductInfo.setOnClickListener {
            onProductItemClick(adapterPosition,productItem)
        }
        //Bind das infos
        with(binding){
            act086ProductItemTvProductDesc.text = ToolBox_Inf.getFormattedGenericIdDesc(
                productItem.productId,
                productItem.productDesc
            )
            //
            act086ProductItemTvProductQty.apply {
                text = productItem.getFormttedQty()
                visibility = if(text.toString().trim().isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}