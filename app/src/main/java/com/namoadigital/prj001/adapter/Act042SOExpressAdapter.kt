package com.namoadigital.prj001.adapter

import android.content.Context
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
import com.namoadigital.prj001.databinding.SoItemListBinding
import com.namoadigital.prj001.design.list.OnRememberListState
import com.namoadigital.prj001.extensions.applyVisibilityIfSourceExists
import com.namoadigital.prj001.model.SO_Pack_Express_Local
import com.namoadigital.prj001.util.ToolBox_Inf

class Act042SOExpressAdapter(
    val soExpressList: ArrayList<SO_Pack_Express_Local>,
    val hmAux_Trans: HMAux,
    val onRememberListState: OnRememberListState<SO_Pack_Express_Local>,
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
            SoItemListBinding.inflate(
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
    inner class MySOExpressVh(val binding: SoItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(soPackExpressLocal: SO_Pack_Express_Local) {
            binding.apply {

                //
                //
                act047CellTvPrefixCode.apply {
                    text = "${soPackExpressLocal.so_prefix}.${soPackExpressLocal.so_code}"
                    visibility =
                        if (soPackExpressLocal.so_prefix == null) View.GONE else View.VISIBLE
                }
                act047CellTvStatusVal.apply {
                    text = hmAux_Trans[soPackExpressLocal.so_status]
                    setTextColor(
                        ToolBox_Inf.getStatusColorV2(
                            act047CellTvStatusVal.context,
                            soPackExpressLocal.so_status
                        )
                    )

                    visibility =
                        if (soPackExpressLocal.so_status == null) View.GONE else View.VISIBLE
                }


                soLeftIcon.applyVisibilityIfSourceExists(com.namoa_digital.namoa_library.R.drawable.ic_baseline_qr_code_2_24)
                setViewContentAndVisibility(soPipelineVal, soPackExpressLocal.pipeline_desc)
                setViewContentAndVisibility(soSerialIdVal, soPackExpressLocal.serial_id)
                midCardLayout.visibility = soSerialIdVal.visibility
                setViewContentAndVisibility(
                    soPriorityVal,
                    hmAux_Trans[soPackExpressLocal.priority_desc]
                )
                setViewContentAndVisibility(soSiteVal, soPackExpressLocal.exec_site_desc)
                setViewContentAndVisibility(soDeadlineVal, soPackExpressLocal.so_desc)


                listOf(
                    soBrandVal,
                    soModelVal,
                    soColorVal,
                ).forEach { item -> item.visibility = View.GONE }

                setViewContentAndVisibility(
                    soCreateDateVal,
                    hmAux_Trans["create_date_lbl"] + " " + ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(soPackExpressLocal.log_date),
                        ToolBox_Inf.nlsDateFormat(root.context) + " HH:mm"
                    )
                )
                setViewContentAndVisibility(
                    soExpressPackServiceList,
                    getPackServiceFormatted(root.context, soPackExpressLocal)
                )
                soExpressLayout.visibility = soExpressPackServiceList.visibility

                if (SYS_STATUS_DENIED == soPackExpressLocal.ret_code) {
                    soRightIcon.applyVisibilityIfSourceExists(R.drawable.ic_baseline_close_24)
                } else if (SYS_STATUS_SENT == soPackExpressLocal.status) {
                    soRightIcon.applyVisibilityIfSourceExists(R.drawable.ic_baseline_cloud_done_24_blue)
                } else {
                    soRightIcon.applyVisibilityIfSourceExists(R.drawable.ic_cloud_upload_24_red)
                }
            }
        }

        private fun getPackServiceFormatted(context: Context, soPackExpressLocal: SO_Pack_Express_Local): String? {
            var serviceList = ""
            if(soPackExpressLocal.packsLocals.size > 0) {
                serviceList = " ${context.getString(R.string.unicode_bullet)} ${soPackExpressLocal.packsLocals[0].qty}x ${soPackExpressLocal.packsLocals[0].pack_service_desc_full}"
                if (soPackExpressLocal.packsLocals.size == 1){
                    return serviceList
                }
                //
                for (i in 1 until soPackExpressLocal.packsLocals.size) {
                    serviceList += "\n ${context.getString(R.string.unicode_bullet)} ${soPackExpressLocal.packsLocals[i].qty}x ${soPackExpressLocal.packsLocals[i].pack_service_desc_full}"
                }
            }else{
                serviceList += " ${context.getString(R.string.unicode_bullet)} 1x ${soPackExpressLocal.so_desc}"
            }
            return serviceList
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
    inner class Act042SOExpressFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<SO_Pack_Express_Local>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if (charFilter.isNullOrEmpty()) {
                temp = soExpressList as MutableList<SO_Pack_Express_Local>
            } else {
                temp.addAll(
                    soExpressList.filter {
                        when (it) {
                            is SO_Pack_Express_Local ->{
                                /**
                                 * BARRIONUEVO 06-12-2021
                                 * Tradução foi marretada pq não previ o status como filtrado.
                                 */
                                var soStatusFiltered = ""
                                if(hmAux_Trans.get(it.so_status) != null) {
                                    soStatusFiltered = hmAux_Trans.get(it.so_status)!!.toLowerCase()
                                }
                                val allFields = ToolBox.AccentMapper(it.getAllFieldForFilter().toLowerCase() + soStatusFiltered)
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
                onRememberListState.dataChanged(results.values as ArrayList<SO_Pack_Express_Local>)
            }
        }
    }
}