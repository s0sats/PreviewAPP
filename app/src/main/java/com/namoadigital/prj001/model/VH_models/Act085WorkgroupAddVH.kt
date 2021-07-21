package com.namoadigital.prj001.model.VH_models

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act085WorkgroupAddListFrgCellBinding
import com.namoadigital.prj001.model.TWorkgroupObj

class Act085WorkgroupAddVH(
    val binding: Act085WorkgroupAddListFrgCellBinding,
    val onItemClick: (position: Int, workgroupObj: TWorkgroupObj, isChecked: Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root)
{
    fun onBindData(
        item: TWorkgroupObj
    ){
        val context = binding.root.context
        with(binding){
            binding.root.setOnClickListener {
                act085WorkgroupAddListFrgCellChk.performClick()
                onItemClick(adapterPosition, item, act085WorkgroupAddListFrgCellChk.isChecked)

            }
            //
            act085WorkgroupAddListFrgCellChk.apply {
                isChecked = item.createUsrWgLink
                setOnCheckedChangeListener { _, isChecked ->
                    //onItemClick(adapterPosition, item, isChecked)
                }
            }
            //
            configWgImg(context, item)
            act085WorkgroupAddListFrgCellTvWgDesc.text = item.groupDesc
        }
    }

    private fun configWgImg(
        context: Context,
        item: TWorkgroupObj
    ) {
        val placeholderAndErroDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_room_group)
        with(binding) {
            act085WorkgroupAddListFrgCellIvWgImg.setImageDrawable(placeholderAndErroDrawable)
            item.groupImage?.let {
                Glide
                    .with(context)
                    .load(it)
                    .placeholder(placeholderAndErroDrawable)
                    .into(act085WorkgroupAddListFrgCellIvWgImg)
            }
        }
    }

}