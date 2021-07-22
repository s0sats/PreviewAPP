package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.databinding.Act085WorkgroupAddListFrgCellBinding
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.model.VH_models.Act085WorkgroupAddVH

class Act085WorkgroupAddAdapter(
    var source: List<TWorkgroupObj>,
    val onItemClick : (position: Int, workgroupObj: TWorkgroupObj, isCheck: Boolean) -> Unit
) : RecyclerView.Adapter<Act085WorkgroupAddVH>(), Filterable{
    private var filteredSource : List<TWorkgroupObj> = emptyList()
    private val mFilter = WorkgroupAddFilter()

    init {
       filteredSource = source
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act085WorkgroupAddVH {
        return Act085WorkgroupAddVH(
            Act085WorkgroupAddListFrgCellBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: Act085WorkgroupAddVH, position: Int) {
        holder.onBindData(
            filteredSource[position]
        )
    }

    override fun getItemCount(): Int {
       return filteredSource.size
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class WorkgroupAddFilter : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<TWorkgroupObj>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if(charFilter.isNullOrEmpty()){
                temp = source as MutableList<TWorkgroupObj>
            }else{
                temp.addAll(
                    source.filter {
                            val allFields = ToolBox.AccentMapper(it.groupDesc.toLowerCase())
                            allFields.contains(charFilter)
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
                filteredSource = results.values as MutableList<TWorkgroupObj>
                notifyDataSetChanged()
            }
        }
    }
}