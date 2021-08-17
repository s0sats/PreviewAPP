package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086PhotoItemBinding
import com.namoadigital.prj001.model.VH_models.Act086PhotoVH

class Act086PhotoAdapter(
    private val onPhotoClick: (photoName: String, postion: Int) -> Unit
) : RecyclerView.Adapter<Act086PhotoVH>() {
    var sourceList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act086PhotoVH {
        val vhBinding =
            Act086PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Act086PhotoVH(vhBinding,onPhotoClick)
    }

    override fun onBindViewHolder(holder: Act086PhotoVH, position: Int) {
        holder.bindData(sourceList[position])
    }

    override fun getItemCount() = sourceList.size

}