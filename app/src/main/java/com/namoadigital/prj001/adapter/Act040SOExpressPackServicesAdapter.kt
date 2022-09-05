package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.CellAddPackServicesItemBinding
import com.namoadigital.prj001.model.MainTagMenu
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.SoPackExpressServicesLocal

class Act040SOExpressPackServicesAdapter(
    val soExpressList: MutableList<SoPackExpressPacksLocal>,
    val showServicePrice: Boolean,
    val hmauxTrans: HMAux,
    var highlightedPosition:Int =-1,
    val onExpressServiceSelected: (packServices: SoPackExpressPacksLocal, position: Int) -> Any?
) : RecyclerView.Adapter<Act040SOExpressPackServicesAdapter.MySOExpressPackVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySOExpressPackVh {
        return MySOExpressPackVh(
            CellAddPackServicesItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MySOExpressPackVh, position: Int) {
        holder.onBind(soExpressList[position], position)
    }

    override fun getItemCount(): Int {
        return soExpressList.size
    }

    fun highlightItemChange(position: Int, item: SoPackExpressPacksLocal) {
        soExpressList.set(position, item)
        highlightedPosition = position
        notifyItemChanged(position)
    }

    inner class MySOExpressPackVh(val binding: CellAddPackServicesItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(packServices: SoPackExpressPacksLocal, position: Int){
            binding.tvPackServices.text = getFormattedPackDesc(packServices)
            binding.ivPackServicesEdit.setOnClickListener {
                onExpressServiceSelected(packServices, position)
            }
            if(position == highlightedPosition){
//                binding.clPackServicesCell.background = binding.root.context.getDrawable(R.color.namoa_color_light_blue6)
            }
            binding.tvPackServicesComment.visibility = View.VISIBLE
            if(packServices.comments.isNullOrEmpty()){
                if(hasAllCommentsNullOrEmpty(packServices.serviceList)){
                    binding.tvPackServicesComment.visibility = View.GONE
                }else{
                    binding.tvPackServicesComment.text = if(hasMultipleComments(packServices)){
                        packServices.comments
                    }else{
                        hmauxTrans["so_express_service_various_comments"]
                    }
                }
            }else{
                binding.tvPackServicesComment.text = packServices.comments
            }
        }

        private fun hasAllCommentsNullOrEmpty(serviceList: MutableList<SoPackExpressServicesLocal>?): Boolean {
            serviceList?.let {
                for (services in serviceList) {
                    services.comments?.let {
                        if(it.isNotEmpty()){
                            return false
                        }
                    }
                }
                return true
            }
            return false
        }

        private fun hasMultipleComments(packServices: SoPackExpressPacksLocal): Boolean {
            var count = 0
            packServices.serviceList.forEach { pack ->
                if(count > 1){
                    return true
                }

                if(!pack.comments.isNullOrEmpty()){
                    count++
                }
            }
            return false
        }

        private fun getFormattedPackDesc(packServices: SoPackExpressPacksLocal): String {
            return packServices.pack_service_desc_full + """(${packServices.qty} x ${getTotalPrice(packServices)})"""
        }

        private fun getTotalPrice(packServices: SoPackExpressPacksLocal): String {
            if(showServicePrice) {
                var price = packServices.price?: 0.0
                /*for (soPackExpressServicesLocal in packServices.serviceList) {
//                    price += soPackExpressServicesLocal.price?: 0.0
                }*/
                return  String.format("%.2f", price)
            }
            return ""
        }
    }

    interface OnFrgMainHomeIteract {
        fun onSelectMenuTagItem(item: MainTagMenu)
    }
}