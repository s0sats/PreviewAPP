package com.namoadigital.prj001.model.VH_models

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namoadigital.prj001.databinding.Act086HistoricFrgAlertItemBinding
import com.namoadigital.prj001.model.Act086HistoricModel
import java.util.*


class Act086HistoricAlertVH(
        private val binding: Act086HistoricFrgAlertItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindData(item: Act086HistoricModel){
                with(binding){
                        act086HistoricFrgAlertItemIvLastAdjust.apply {
                            setImageDrawable(root.resources.getDrawable(item.icon.first))
                            setColorFilter(root.resources.getColor(item.icon.second))
                        }
                    act086HistoricFrgAlertItemTvAdjustLbl.text =
                        item.titleLbl.capitalize(Locale.getDefault())
                    act086HistoricFrgAlertItemTvAdjustDate.text = item.date.split(" ")[0]
                    act086HistoricFrgAlertItemTvLastMeasureVal.text = "( ${item.measure} )"
                    act086HistoricFrgAlertItemTvMaterialBl.text =
                        item.materialLbl.capitalize(Locale.getDefault())
                    act086HistoricFrgAlertItemTvComment.apply {
                        text = item.comment?.capitalize(Locale.getDefault())
                        visibility = if (text != null) View.VISIBLE else View.GONE
                    }
                    //
                    item.photo1?.let {
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto1)
                    }
                    //
                    item.photo2?.let {
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto2)
                    }
                    //
                    item.photo3?.let {
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto3)
                    }
                    //
                    item.photo4?.let {
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto4)
                    }
                }

        }
}