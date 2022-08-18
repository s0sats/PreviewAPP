package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act011InspectionQuestionFormCellBinding
import com.namoadigital.prj001.databinding.CellAddPackServicesItemBinding
import com.namoadigital.prj001.model.MainTagMenu
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.SoPackExpressServicesLocal

class Act040SOExpressPackServicesAdapter(
    val soExpressList: MutableList<SoPackExpressPacksLocal>,
    val showServicePrice: Boolean,
    val onExpressServiceSelected: (packServices: SoPackExpressPacksLocal, position: Int) -> Any?
) : RecyclerView.Adapter<Act040SOExpressPackServicesAdapter.MySOExpressPackVh>() {
    var highlightedPosition =-1
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
                binding.clPackServicesCell.background = binding.root.context.getDrawable(R.color.namoa_color_light_blue6)
            }
        }

        private fun getFormattedPackDesc(packServices: SoPackExpressPacksLocal): String {
            return packServices.pack_service_desc_full + """(${packServices.qty} x ${getTotalPrice(packServices)})"""
        }

        private fun getTotalPrice(packServices: SoPackExpressPacksLocal): String {
            if(showServicePrice) {
                var price = packServices.price?: 0.0
                for (soPackExpressServicesLocal in packServices.serviceList) {
//                    price += soPackExpressServicesLocal.price?: 0.0
                }
                return  String.format("%.2f", price)
            }
            return ""
        }
    }

    interface OnFrgMainHomeIteract {
        fun onSelectMenuTagItem(item: MainTagMenu)
    }
}