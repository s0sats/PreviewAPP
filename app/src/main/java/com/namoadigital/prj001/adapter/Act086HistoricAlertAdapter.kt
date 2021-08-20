package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086HistoricFrgAlertItemBinding
import com.namoadigital.prj001.model.Act086HistoricAlert
import com.namoadigital.prj001.model.VH_models.Act086HistoricAlertVH

class Act086HistoricAlertAdapter(
    private val source: ArrayList<Act086HistoricAlert>
) : RecyclerView.Adapter<Act086HistoricAlertVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act086HistoricAlertVH {
        return Act086HistoricAlertVH(
                Act086HistoricFrgAlertItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )
    }

    override fun onBindViewHolder(holder: Act086HistoricAlertVH, position: Int) {
        holder.bindData(source[position])
    }

    override fun getItemCount() = source.size
}