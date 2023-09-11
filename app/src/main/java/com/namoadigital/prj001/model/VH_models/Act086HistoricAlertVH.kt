package com.namoadigital.prj001.model.VH_models

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act086HistoricFrgAlertItemBinding
import com.namoadigital.prj001.model.Act086HistoricModel
import com.namoadigital.prj001.model.MaterialHistItemModel
import java.util.*


class Act086HistoricAlertVH(
    private val binding: Act086HistoricFrgAlertItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: Act086HistoricModel,
                     onPhotoSelected: (drawable: Drawable) -> Unit){
                with(binding){
                        act086HistoricFrgAlertItemIvLastAdjust.apply {
                            setImageDrawable(root.resources.getDrawable(item.icon.first))
                            setColorFilter(root.resources.getColor(item.icon.second))
                        }
                    act086HistoricFrgAlertItemTvAdjustLbl.text =
                        item.titleLbl.capitalize(Locale.getDefault())
                    act086HistoricFrgAlertItemTvAdjustDate.text = item.date.split(" ")[0]
                    act086HistoricFrgAlertItemTvLastMeasureVal.text = "(${item.measure})"

                    act086HistoricFrgAlertItemTvMaterialAppliedLbl.text =
                        """${item.materialAppliedLbl.capitalize(Locale.getDefault())}: """
                    val materialListAppliedFormatted = formatMaterialList(item.materialList, 1, binding.root.context)

                    frgAlertItemTvMaterialAppliedListItem.visibility = View.GONE
                    act086HistoricFrgAlertItemTvMaterialAppliedLbl.visibility = View.GONE
                    if(!materialListAppliedFormatted.isNullOrEmpty()) {
                        act086HistoricFrgAlertItemTvMaterialAppliedLbl.visibility = View.VISIBLE
                        frgAlertItemTvMaterialAppliedListItem.visibility = View.VISIBLE
                        frgAlertItemTvMaterialAppliedListItem.text = materialListAppliedFormatted
                    }

                    act086HistoricFrgAlertItemTvMaterialRequestLbl.text =
                        """${item.materialAppliedLbl.capitalize(Locale.getDefault())}: """
                    val materialRequestListFormatted = formatMaterialList(item.materialList, 2, binding.root.context)

                    frgAlertItemTvMaterialRequestListItem.visibility = View.GONE
                    act086HistoricFrgAlertItemTvMaterialRequestLbl.visibility = View.GONE
                    if(!materialRequestListFormatted.isNullOrEmpty()) {
                        act086HistoricFrgAlertItemTvMaterialRequestLbl.visibility = View.VISIBLE
                        frgAlertItemTvMaterialRequestListItem.visibility = View.VISIBLE
                        frgAlertItemTvMaterialRequestListItem.text = materialRequestListFormatted
                    }


                    act086HistoricFrgAlertItemTvComment.apply {
                        text = item.comment?.capitalize(Locale.getDefault())
                        visibility = if (!text.isNullOrBlank()) View.VISIBLE else View.GONE
                    }
                    //
                    frgAlertItemCvPhoto1.visibility = View.GONE
                    if(!item.photo1.isNullOrBlank()) {

                        frgAlertItemCvPhoto1.visibility = View.VISIBLE
                        frgAlertItemIvPhoto1.setOnClickListener {
                            onPhotoSelected(frgAlertItemIvPhoto1.drawable )
                        }

                        Glide.with(root.context)
                            .load(item.photo1)
                            .placeholder(R.drawable.sand_watch_transp)
                            .error(R.drawable.sand_watch_transp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(frgAlertItemIvPhoto1)
                    }

                    //
                    frgAlertItemCvPhoto2.visibility = View.GONE
                    if(!item.photo2.isNullOrBlank()) {
                        frgAlertItemCvPhoto2.visibility = View.VISIBLE
                        frgAlertItemIvPhoto2.setOnClickListener {
                            onPhotoSelected(frgAlertItemIvPhoto2.drawable )
                        }

                        Glide.with(root.context)
                            .load(item.photo2)
                            .placeholder(R.drawable.sand_watch_transp)
                            .error(R.drawable.sand_watch_transp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(frgAlertItemIvPhoto2)
                    }
                    //
                    frgAlertItemCvPhoto3.visibility = View.GONE
                    if(!item.photo3.isNullOrBlank()) {
                        frgAlertItemCvPhoto3.visibility = View.VISIBLE
                        frgAlertItemIvPhoto3.setOnClickListener {
                            onPhotoSelected(frgAlertItemIvPhoto3.drawable )
                        }

                        Glide.with(root.context)
                            .load(item.photo3)
                            .placeholder(R.drawable.sand_watch_transp)
                            .error(R.drawable.sand_watch_transp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(frgAlertItemIvPhoto3)
                    }
                    //
                    frgAlertItemCvPhoto4.visibility = View.GONE
                    if(!item.photo4.isNullOrBlank()) {
                        frgAlertItemCvPhoto4.visibility = View.VISIBLE
                        frgAlertItemIvPhoto4.setOnClickListener {
                            onPhotoSelected(frgAlertItemIvPhoto4.drawable )
                        }

                        Glide.with(root.context)
                            .load(item.photo4)
                            .placeholder(R.drawable.sand_watch_transp)
                            .error(R.drawable.sand_watch_transp)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(frgAlertItemIvPhoto4)
                    }
                }

        }

    private fun formatMaterialList(
        list: List<MaterialHistItemModel>?,
        materialAction: Int,
        context: Context
    ): String {
        val sb = StringBuilder("")
        list?.filter {
            it.materialAction == materialAction
        }?.forEach {
            sb.append(context.getString(R.string.unicode_bullet) + " " + it.formatMaterialHistItem())
            sb.append("\n")
        }
        sb.removeSuffix("\n")
        return sb.toString()
    }
}