package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act011InspectionQuestionFormCellBinding
import com.namoadigital.prj001.databinding.CellAddPackServicesItemBinding
import com.namoadigital.prj001.model.MainTagMenu
import com.namoadigital.prj001.model.SoPackExpressPacksLocal
import com.namoadigital.prj001.model.SoPackExpressServicesLocal

class Act040SOExpressPackServicesAdapter(
    val soExpressList: MutableList<SoPackExpressPacksLocal>,
    val showServicePrice: Boolean,
    val onExpressServiceSelected: (packServices: SoPackExpressPacksLocal) -> Any?
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
        holder.onBind(soExpressList[position])
    }

    override fun getItemCount(): Int {
        return soExpressList.size
    }

    inner class MySOExpressPackVh(val binding: CellAddPackServicesItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(packServices: SoPackExpressPacksLocal){
            binding.tvPackServices.text = getFormattedPackDesc(packServices)
            binding.ivPackServicesEdit.setOnClickListener {
                onExpressServiceSelected(packServices)
            }
        }

        private fun getFormattedPackDesc(packServices: SoPackExpressPacksLocal): String {
            return packServices.pack_service_desc_full + """(${packServices.qty} x ${getTotalPrice(packServices.serviceList)})"""
        }

        private fun getTotalPrice(serviceList: MutableList<SoPackExpressServicesLocal>): String {
            if(showServicePrice) {
                var price: Double = 0.0
                for (soPackExpressServicesLocal in serviceList) {
                    price += soPackExpressServicesLocal.price
                }
                return """${price} """
            }
            return ""
        }
    }

    interface OnFrgMainHomeIteract {
        fun onSelectMenuTagItem(item: MainTagMenu)
    }
}