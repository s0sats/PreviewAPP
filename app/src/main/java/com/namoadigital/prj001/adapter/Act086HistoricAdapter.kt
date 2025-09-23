package com.namoadigital.prj001.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086HistoricFrgAlertItemBinding
import com.namoadigital.prj001.model.Act086HistoricModel
import com.namoadigital.prj001.model.VH_models.Act086HistoricAlertVH

class Act086HistoricAdapter(
    private val context: Context,
    private val source: List<Act086HistoricModel>,
    private val onPhotoSelected: (drawable: Drawable) -> Unit,
) : RecyclerView.Adapter<Act086HistoricAlertVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act086HistoricAlertVH {
        return Act086HistoricAlertVH(
            context,
            Act086HistoricFrgAlertItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: Act086HistoricAlertVH, position: Int) {
        holder.bindData(source[position], onPhotoSelected)
    }

    override fun getItemCount() = source.size
}