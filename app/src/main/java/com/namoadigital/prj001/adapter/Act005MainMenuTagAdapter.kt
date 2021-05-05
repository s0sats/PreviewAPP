package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act005TagCellBinding
import com.namoadigital.prj001.model.MainTagMenu
import com.namoadigital.prj001.util.ConstantBaseApp.*
import com.namoadigital.prj001.view.frag.frg_main_home.FrgMainHome

class Act005MainMenuTagAdapter(
        private val _mMainTagMenu: MutableList<MainTagMenu>,
        private val hmAux_Trans: HMAux,
        val mListener: FrgMainHome.OnFrgMainHomeIteract
): RecyclerView.Adapter<Act005MainMenuTagAdapter.MyTagVh>() {

    val mMainTagMenu get() = _mMainTagMenu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTagVh {
        return MyTagVh(Act005TagCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyTagVh, position: Int) {
        holder.itemView.setOnClickListener{
            mListener.onSelectMenuTagItem(mMainTagMenu[position])
        }
        return holder.onBinding(mMainTagMenu[position], hmAux_Trans)
    }

    override fun getItemCount(): Int {
        return mMainTagMenu.size
    }

    class MyTagVh(private val binding: Act005TagCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(mMainTagMenu: MainTagMenu, hmAux_Trans: HMAux) {
            binding.tvTagName.text = mMainTagMenu.tagName
            //
            binding.tvTagQty.text = " ${hmAux_Trans.get("tag_item_qty")}: + ' ' + ${mMainTagMenu.tagQty} "
            if(mMainTagMenu.tagQtyInExecution > 0 ){
                binding.tvTagQty.text = binding.tvTagQty.text.toString() + "(" + mMainTagMenu.tagQtyInExecution + hmAux_Trans.get("tag_item_qty_in_execution") +")"
            }
            binding.ivTagStatus.visibility = View.VISIBLE
            //
            @DrawableRes var iconStatus: Int? = null
            @ColorRes var iconColor: Int? = null
            //
            if(MAIN_TAG_MENU_SYNC_DATA.equals(mMainTagMenu.status)){
                iconStatus = R.drawable.ic_sync_main_menu_data
                binding.ivTagStatus.setBackgroundResource(iconStatus)
                binding.ivTagStatus.visibility = View.VISIBLE
            }
            //
            if(MAIN_TAG_MENU_SEND_DATA.equals(mMainTagMenu.status)){
                iconStatus = R.drawable.ic_cloud_upload
                iconColor = R.color.namoa_cancel_red
                binding.ivTagStatus.setBackgroundResource(iconStatus)
                binding.ivTagStatus.setBackgroundResource(iconStatus)
            }
            //
            if(MAIN_TAG_MENU_RECEIVE_DATA.equals(mMainTagMenu.status)){
                iconStatus = R.drawable.ic_baseline_cloud_download_24
                iconColor = R.color.custom_yellow_sync
            }
            //
            if(MAIN_TAG_MENU_DATA_OK.equals(mMainTagMenu.status)){
                binding.ivTagStatus.visibility = View.GONE
            }else{
                iconStatus?.let {
                    binding.ivTagStatus.setBackgroundResource(it)
                }
                //
                iconColor?.let{
                    binding.ivTagStatus.setColorFilter(iconColor)
                }
                //
                binding.ivTagStatus.visibility = View.VISIBLE
            }

        }
    }

}