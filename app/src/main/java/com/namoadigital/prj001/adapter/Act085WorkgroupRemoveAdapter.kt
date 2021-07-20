package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act085WorkgroupRemoveListFrgCellBinding
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.model.VH_models.Act085WorkgroupRemoveVH

class Act085WorkgroupRemoveAdapter(
    var source: List<TWorkgroupObj>,
    val onRemoveItemClick : (action: Int, wgCode: TWorkgroupObj) -> Unit
) : RecyclerView.Adapter<Act085WorkgroupRemoveVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act085WorkgroupRemoveVH {
        return Act085WorkgroupRemoveVH(
            Act085WorkgroupRemoveListFrgCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: Act085WorkgroupRemoveVH, position: Int) {
        holder.onBindData(source[position])
    }

    override fun getItemCount(): Int {
       return source.size
    }
}