package com.namoadigital.prj001.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act005ModuleCellBinding
import com.namoadigital.prj001.model.MainModuleMenu
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.view.frag.frg_main_home_alt.FrgMainHomeAlt

class Act005MainMenuModuleAdapter(
        var mMainModuleMenu: MutableList<MainModuleMenu>,
        private val hmAux_Trans: HMAux,
        val mListener: FrgMainHomeAlt.OnFrgMainHomeAltInteract?
): RecyclerView.Adapter<Act005MainMenuModuleAdapter.MyModuleVh>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyModuleVh {
        return MyModuleVh(Act005ModuleCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mMainModuleMenu.size
    }

    override fun onBindViewHolder(holder: MyModuleVh, position: Int) {
        val item = mMainModuleMenu[position]
        holder.itemView.setOnClickListener{
            when(item.moduleId){
                MainModuleMenu.ID_MODULE_OS -> mListener?.onSelectOS()
                MainModuleMenu.ID_MODULE_OS_EXPRESS -> mListener?.onSelectOSExpress()
                MainModuleMenu.ID_MODULE_OS_NEXT -> mListener?.onSelectOSNext()
                MainModuleMenu.ID_MODULE_OS_VIN_SEARCH -> mListener?.onSelectOSVinSearch()
                MainModuleMenu.ID_MODULE_ASSETS -> mListener?.onSelectAsset()
                MainModuleMenu.ID_MODULE_TAGS -> mListener?.onSelectTags()
                MainModuleMenu.ID_MODULE_TAGS_BY_SERIAL_SEARCH -> mListener?.onSelectTagsBySerialSearch()
            }
        }
        return holder.onBinding(item)
    }

    class MyModuleVh(private val binding: Act005ModuleCellBinding)  : RecyclerView.ViewHolder(binding.root){
        fun onBinding(item: MainModuleMenu) {
            val context = binding.root.context
            //
            binding.tvModuleTitle.text = item.moduleTitle
            binding.tvModuleDetail.text = item.moduleDetail
            binding.ivModuleItem.setImageDrawable(context.getDrawable(item.moduleIcon))
            //
            binding.ivModuleStatus.visibility = View.GONE
            //
            handleModuleStatus(context, item)
            //
        }

        private fun handleModuleStatus(context: Context, item: MainModuleMenu) {
            var iconStatus: Drawable? = null
            @ColorRes var iconColor: Int? = null
            //
            if (ConstantBaseApp.MAIN_TAG_MENU_SYNC_DATA.equals(item.status)) {
                iconStatus = context.getDrawable(R.drawable.ic_sync_main_menu_data)
            }
            //
            if (ConstantBaseApp.MAIN_TAG_MENU_SEND_DATA.equals(item.status)) {
                iconStatus = context.getDrawable(R.drawable.ic_cloud_upload)
                iconColor = ContextCompat.getColor(context, R.color.namoa_cancel_red)
            }
            //
            if (ConstantBaseApp.MAIN_TAG_MENU_RECEIVE_DATA.equals(item.status)) {
                iconStatus = context.getDrawable(R.drawable.ic_baseline_cloud_download_24)
                iconColor = ContextCompat.getColor(context, R.color.custom_yellow_sync)
            }
            //
            if (ConstantBaseApp.MAIN_TAG_MENU_DATA_OK.equals(item.status)) {
                binding.ivModuleStatus.visibility = View.GONE
            } else {
                if (iconStatus != null) {
                    binding.ivModuleStatus.setImageDrawable(iconStatus)
                }
                //
                if (iconColor != null) {
                    binding.ivModuleStatus.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
                }
                //
                binding.ivModuleStatus.visibility = View.VISIBLE
            }
        }
    }
}