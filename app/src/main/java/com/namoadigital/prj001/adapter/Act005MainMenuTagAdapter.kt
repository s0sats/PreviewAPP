package com.namoadigital.prj001.adapter

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
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
        val mListener: FrgMainHome.OnFrgMainHomeIteract?
): RecyclerView.Adapter<Act005MainMenuTagAdapter.MyTagVh>() {

    public val mMainTagMenu get() = _mMainTagMenu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTagVh {
        return MyTagVh(Act005TagCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyTagVh, position: Int) {
        holder.itemView.setOnClickListener{
            mListener?.onSelectMenuTagItem(mMainTagMenu[position])
        }
        return holder.onBinding(mMainTagMenu[position], hmAux_Trans)
    }

    override fun getItemCount(): Int {
        return mMainTagMenu.size
    }

    class MyTagVh(private val binding: Act005TagCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(mMainTagMenu: MainTagMenu, hmAux_Trans: HMAux) {
            val context = binding.root.context
            binding.tvTagName.text = mMainTagMenu.tagName
            //
            binding.tvTagQty.text = """${hmAux_Trans.get("tag_item_qty")} :  ${mMainTagMenu.tagQty} """
            //
            if(mMainTagMenu.tagHasFormInExecution > 0 ){
                val tagInfo: String = """${binding.tvTagQty.text} (${hmAux_Trans.get("tag_item_form_in_execution")})"""
                val spannableString = SpannableString(tagInfo)
                //
                spannableString.setSpan(
                        ForegroundColorSpan(context.getResources().getColor(R.color.namoa_amount_pipeline_background_btn)),
                        tagInfo.indexOf("("+ hmAux_Trans.get("tag_item_form_in_execution") +")"),
                        tagInfo.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                binding.tvTagQty.text = spannableString
            }

            binding.ivTagStatus.visibility = View.VISIBLE
            // drawableEnd
            var iconStatus: Drawable? = null
            @ColorRes var iconColor: Int? = null
            //
            if(MAIN_TAG_MENU_SYNC_DATA.equals(mMainTagMenu.status)){
                iconStatus = context.getDrawable(R.drawable.ic_sync_main_menu_data)
//                iconColor = ContextCompat.getColor(binding.root.context, R.color.namoa_cancel_red)
            }
            //
            if(MAIN_TAG_MENU_SEND_DATA.equals(mMainTagMenu.status)){
                iconStatus = context.getDrawable(R.drawable.ic_cloud_upload)
                iconColor = ContextCompat.getColor(context, R.color.namoa_cancel_red)
            }
            //
            if(MAIN_TAG_MENU_RECEIVE_DATA.equals(mMainTagMenu.status)){
                iconStatus = context.getDrawable(R.drawable.ic_baseline_cloud_download_24)
                iconColor = ContextCompat.getColor(context, R.color.custom_yellow_sync)
            }
            //
            if(MAIN_TAG_MENU_DATA_OK.equals(mMainTagMenu.status)){
                binding.ivTagStatus.visibility = View.GONE
            }else{
                if(iconStatus != null) {
                    binding.ivTagStatus.setImageDrawable(iconStatus)
                }
                //
                if(iconColor != null){
                    binding.ivTagStatus.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
                }
                //
                binding.ivTagStatus.visibility = View.VISIBLE
            }

        }
    }

}