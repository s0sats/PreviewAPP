package com.namoadigital.prj001.model.VH_models

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namoadigital.prj001.databinding.Act086HistoricFrgAlertItemBinding
import com.namoadigital.prj001.model.Act086HistoricModel
import com.namoadigital.prj001.model.MaterialHistItemModel
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
                    val materialListFormatted = formatMaterialList(item.materialList)
                    frgAlertItemTvMaterialListItem.visibility = View.GONE
                    if(!materialListFormatted.isNullOrEmpty()) {
                        frgAlertItemTvMaterialListItem.visibility = View.VISIBLE
                        frgAlertItemTvMaterialListItem.text = materialListFormatted
                    }
                    //
                    frgAlertItemIvPhoto1.visibility = View.GONE
                    item.photo1?.let {
                        frgAlertItemIvPhoto1.visibility = View.VISIBLE
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto1)
                    }
                    //
                    frgAlertItemIvPhoto2.visibility = View.GONE
                    item.photo2?.let {
                        frgAlertItemIvPhoto2.visibility = View.VISIBLE
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto2)
                    }
                    //
                    frgAlertItemIvPhoto3.visibility = View.GONE
                    item.photo3?.let {
                        frgAlertItemIvPhoto3.visibility = View.VISIBLE
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto3)
                    }
                    //
                    frgAlertItemIvPhoto4.visibility = View.GONE
                    item.photo4?.let {
                        frgAlertItemIvPhoto4.visibility = View.VISIBLE
                        Glide.with(root.context)
                            .load(it)
                            .into(frgAlertItemIvPhoto4)
                    }
                }

        }

    private fun formatMaterialList(list: List<MaterialHistItemModel>?): String {
        val sb = StringBuilder("")
        list?.forEach {
            sb.append(it.formatMaterialHistItem())
            sb.append("\n")
        }
        sb.removeSuffix("\n")
        return sb.toString()
    }
}