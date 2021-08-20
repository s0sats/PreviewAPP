package com.namoadigital.prj001.model.VH_models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086HistoricFrgAlertItemBinding
import com.namoadigital.prj001.model.Act086HistoricAlert


class Act086HistoricAlertVH(
        private val binding: Act086HistoricFrgAlertItemBinding
) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: Act086HistoricAlert){
                with(binding){
                        act086HistoricFrgAlertItemTvAdjustDate.text = item.date
                        act086HistoricFrgAlertItemTvLastMeasureVal.text = item.measure
                        act086HistoricFrgAlertItemTvComment.apply {
                                text = item.comment
                                visibility = if(text != null) View.VISIBLE else View.GONE
                        }
                }

        }
}