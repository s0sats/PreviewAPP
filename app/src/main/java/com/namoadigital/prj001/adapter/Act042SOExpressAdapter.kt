package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.ConstantBase.SYS_STATUS_DENIED
import com.namoa_digital.namoa_library.util.ConstantBase.SYS_STATUS_SENT
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act042SoExpressCellBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfSourceExists
import com.namoadigital.prj001.model.SO_Pack_Express_Local
import com.namoadigital.prj001.util.ToolBox_Inf

class Act042SOExpressAdapter (
    val soExpressList : ArrayList<SO_Pack_Express_Local>,
    val hmAux_Trans: HMAux
) : RecyclerView.Adapter<Act042SOExpressAdapter.MySOExpressVh>(), Filterable{
    //
    val soExpressListFiltered: MutableList<SO_Pack_Express_Local> = mutableListOf()
    //
    var mFilter = Act042SOExpressFilter()
    //
    init {
        soExpressListFiltered.addAll(soExpressList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySOExpressVh {
        return MySOExpressVh(
            Act042SoExpressCellBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }
    //
    override fun onBindViewHolder(holder: MySOExpressVh, position: Int) {
        val soPackExpressLocal = soExpressListFiltered[position]
        holder.onBind(soPackExpressLocal)
    }
    //
    override fun getItemCount() = soExpressListFiltered.size
    //
    inner class MySOExpressVh(val binding: Act042SoExpressCellBinding)  : RecyclerView.ViewHolder(binding.root){
        fun onBind(soPackExpressLocal: SO_Pack_Express_Local){
            binding.apply {
                setHeaderContentAndVisibility(soExpressTvHeader ,soPackExpressLocal.so_id, hmAux_Trans.get(soPackExpressLocal.so_status))
                setViewContentAndVisibility(soExpressPackageDesc ,soPackExpressLocal.so_desc)
                setViewContentAndVisibility(soExpressProductDesc ,soPackExpressLocal.product_desc)
                setViewContentAndVisibility(soExpressSerialId ,soPackExpressLocal.serial_id)
                setViewContentAndVisibility(soExpressBillingAddInf1 ,soPackExpressLocal.billing_add_inf1_value)
                setViewContentAndVisibility(soExpressBillingAddInf2 ,soPackExpressLocal.billing_add_inf2_value)
                setViewContentAndVisibility(soExpressBillingAddInf3 ,soPackExpressLocal.billing_add_inf3_value)
                setViewContentAndVisibility(soExpressSiteDesc ,soPackExpressLocal.exec_site_desc)
                setViewContentAndVisibility(soExpressOperationDesc ,soPackExpressLocal.operation_desc)
                setViewContentAndVisibility(soExpressErrorMsg ,soPackExpressLocal.ret_msg)
                setViewContentAndVisibility(soExpressEndDatetime ,ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(soPackExpressLocal.log_date),
                    ToolBox_Inf.nlsDateFormat(root.context) + " HH:mm"
                ))
                soExpressEndDatetime.setTextColor(root.context.getResources().getColor(R.color.namoa_lime_green_2))
                if(SYS_STATUS_DENIED.equals(soPackExpressLocal.ret_code)){
                    soExpressIvStatus.applyVisibilityIfSourceExists(R.drawable.ic_baseline_close_24)
                    soExpressEndDatetime.setTextColor(root.context.getResources().getColor(R.color.namoa_color_gray_8))
                }else if(SYS_STATUS_SENT.equals(soPackExpressLocal.status)) {
                    soExpressIvStatus.applyVisibilityIfSourceExists(R.drawable.ic_baseline_cloud_done_24_blue)
                }else{
                    soExpressIvStatus.applyVisibilityIfSourceExists(R.drawable.ic_cloud_upload_24_red)
                }
            }
        }
        private fun setHeaderContentAndVisibility(soExpressTvHeader: TextView, soId: String?, soStatus: String?) {
            if(soStatus.isNullOrEmpty()
                && soId.isNullOrEmpty()){
                soExpressTvHeader.visibility = View.GONE
            }else{
                soExpressTvHeader.visibility = View.VISIBLE
                if(!soId.isNullOrEmpty()){
                    soExpressTvHeader.text = soId
                }
                if(!soStatus.isNullOrEmpty()){
                    soExpressTvHeader.text = """${soExpressTvHeader.text}  ${soStatus}"""
                }
            }
        }

    }
    //



    fun setViewContentAndVisibility(textView: TextView, content: String?){
        if(content.isNullOrEmpty()){
            textView.visibility = View.GONE
        }else{
            textView.visibility = View.VISIBLE
            textView.text = content
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }
    //
    inner class Act042SOExpressFilter() : Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<SO_Pack_Express_Local>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if(charFilter.isNullOrEmpty()){
                temp = soExpressList as MutableList<SO_Pack_Express_Local>
            }else{
                temp.addAll(
                    soExpressList.filter {
                        when(it){
                            is SO_Pack_Express_Local ->{
                                val allFields = ToolBox.AccentMapper(it.getAllFieldForFilter().toLowerCase())
                                allFields.contains(charFilter)
                            }
                            //se for o botão, sempre exibe
                            else -> true
                        }
                    }
                )
            }
            val filterResults = FilterResults()
            filterResults.count = temp.size
            filterResults.values = temp
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.let {
                soExpressListFiltered.clear()
                soExpressListFiltered.addAll(results.values as Collection<SO_Pack_Express_Local>)
                notifyDataSetChanged()
            }
        }
    }
}