package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086MaterialItemBinding
import com.namoadigital.prj001.model.Act086MaterialItem
import com.namoadigital.prj001.model.VH_models.Act086MaterialItemVH

class Act086MaterialItemAdapter(
    private val onProductItemClick: (position: Int, materialItem: Act086MaterialItem) -> Unit,
    private val onDeleteIconClick: (position: Int) -> Unit,
    private val inReadonly: Boolean
) : RecyclerView.Adapter<Act086MaterialItemVH>() {

    var sourceList = mutableListOf<Act086MaterialItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act086MaterialItemVH {
        val binding =
            Act086MaterialItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Act086MaterialItemVH(binding,onProductItemClick, onDeleteIconClick,inReadonly)
    }

    override fun onBindViewHolder(holder: Act086MaterialItemVH, position: Int) {
        holder.bindData(sourceList[position])
    }

    override fun getItemCount() = sourceList.size
}