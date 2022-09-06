package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.databinding.Act091ListItemBinding
import com.namoadigital.prj001.model.TSO_Service_Search_Obj
import com.namoadigital.prj001.util.ToolBox_Inf.formatDoublePriceToScreen
import java.util.*

class Act091_Item_Adapter constructor(
    private val dataset: List<TSO_Service_Search_Obj>,
    private val showPrice: Boolean,
    private val hmAux: HMAux,
    private val notifyFilterApplied: (Int) -> Unit,
    private val openBottomSheet: (TSO_Service_Search_Obj) -> Unit,
    ) : RecyclerView.Adapter<Act091_Item_Adapter.ItemViewHolder>(), Filterable{

    var filterDataSet = dataset.toMutableList()
    val mFilter = ServiceFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(Act091ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), showPrice)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            openBottomSheet(filterDataSet[position])
        }

        return holder.onBinding(filterDataSet[position])
    }

    override fun getItemCount() = filterDataSet.size


    inner class ItemViewHolder constructor(
        private val binding: Act091ListItemBinding,
        private val showPrice: Boolean,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBinding(item: TSO_Service_Search_Obj?) {
            with(binding) {
                item?.let {
                    act091ListTitle.text = it.pack_service_desc_full
                    act091ListProgress.max = it.rating
                    act091ListProgress.progress = it.rating_ref.toInt()
                    act091ListPrice.apply {
                        if (showPrice) {
                            text = if(it.manual_price == 1){
                                hmAux_Trans["has_manual_price_lbl"]
                            }else{
                                formatDoublePriceToScreen(it.price)
                            }
                        } else {
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class ServiceFilter : Filter() {
        override fun performFiltering(char: CharSequence?): FilterResults {
            var temp = mutableListOf<TSO_Service_Search_Obj>()
            val charString = ToolBox.AccentMapper(char.toString().lowercase(Locale.getDefault()))

            if(charString.isNullOrEmpty()) {
                temp = dataset.toMutableList()
            }else {

                temp.addAll(
                    dataset.filter {
                        val allFilter = ToolBox.AccentMapper(it.allFieldForFilter.lowercase())
                        allFilter.contains(charString)
                    }
                )
            }

            return FilterResults().apply {
                count = temp.size
                values = temp
            }
        }

        override fun publishResults(char: CharSequence?, filter: FilterResults?) {
            filter?.let {
                filterDataSet = it.values as MutableList<TSO_Service_Search_Obj>
                notifyDataSetChanged()
                notifyFilterApplied(filterDataSet.size)
            }
        }

    }
}