package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act090MaterialItemBinding
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.VH_models.Act090MaterialItemVH

class Act090MaterialAdapter(
    private val onMaterialItemClick : (position: Int, materialItem: Act086MaterialItem) -> Unit,
    private val onToogleChange: (position: Int,  materialItem: Act086MaterialItem, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<Act090MaterialItemVH>() {

    var sourceList = mutableListOf<Act086MaterialItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act090MaterialItemVH {
        val binding = Act090MaterialItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Act090MaterialItemVH(
            binding, onMaterialItemClick, onToogleChange
        )
    }

    override fun onBindViewHolder(holder: Act090MaterialItemVH, position: Int) {
        holder.bindData(sourceList[position])
    }

    override fun getItemCount() = sourceList.size
}