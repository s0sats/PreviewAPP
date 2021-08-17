package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086ProductItemBinding
import com.namoadigital.prj001.model.Act086ProductItem
import com.namoadigital.prj001.model.VH_models.Act086ProductItemVH

class Act086ProductItemAdapter(
    private val onProductItemClick: (productItem: Act086ProductItem) -> Unit,
    private val onDeleteIconClick: (position: Int) -> Unit
) : RecyclerView.Adapter<Act086ProductItemVH>() {

    var sourceList = mutableListOf<Act086ProductItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act086ProductItemVH {
        val binding =
            Act086ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Act086ProductItemVH(binding,onProductItemClick, onDeleteIconClick)
    }

    override fun onBindViewHolder(holder: Act086ProductItemVH, position: Int) {
        holder.bindData(sourceList[position])
    }

    override fun getItemCount() = sourceList.size
}