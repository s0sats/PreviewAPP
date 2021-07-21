package com.namoadigital.prj001.model.VH_models

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act085WorkgroupRemoveListFrgCellBinding
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.util.ToolBox_Inf

class Act085WorkgroupRemoveVH(
    val binding: Act085WorkgroupRemoveListFrgCellBinding,
    val onRemoveItemClick: (action: Int, wgCode: TWorkgroupObj) -> Unit,
    val untilExpireDateLbl: String
) : RecyclerView.ViewHolder(binding.root)
{
    fun onBindData(
        item: TWorkgroupObj
    ){
        val context = binding.root.context
        with(binding){
            act085WorkgroupRemoveListFrgCellIvRemove.setOnClickListener { _->
                onRemoveItemClick(0,item)
            }
            act085WorkgroupRemoveListFrgCellTvWorkgroupDesc.text = item.groupDesc
            act085WorkgroupRemoveListFrgCellTvExpireDate.apply {
                text = ""
                visibility = View.GONE
                //
                if(!item.dateExpire.isNullOrEmpty()){
                    visibility = View.VISIBLE
                    text = formatExpireDateInfo(item, context)
                }
            }
            //
            val placeholderAndErroDrawable = ContextCompat.getDrawable(context, R.drawable.ic_room_group)
            act085WorkgroupRemoveListFrgCellIvWgImg.setImageDrawable(placeholderAndErroDrawable)
            item.groupImage?.let{
                Glide
                    .with(context)
                    .load(it)
                    .placeholder(placeholderAndErroDrawable)
                    .into(act085WorkgroupRemoveListFrgCellIvWgImg)
            }

        }
    }

    /**
     * LUCHE -21/07/2021
     * <p></p>
     * Fun que retorna o texto formtado com o lbl passado e data de expiracao formatada.
     */
    private fun formatExpireDateInfo(
        item: TWorkgroupObj,
        context: Context?
    ) :String{
        return "$untilExpireDateLbl ${ToolBox_Inf.millisecondsToString(
                                    ToolBox_Inf.dateToMilliseconds(item.dateExpire),
                                    ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                    )}"
    }
}