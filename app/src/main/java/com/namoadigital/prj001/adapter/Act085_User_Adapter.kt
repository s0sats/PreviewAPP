package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.databinding.Act085UserListFrgCellBinding
import com.namoadigital.prj001.model.TUserWorkgroupObj
import com.namoadigital.prj001.model.VH_models.Act085UserVH

class Act085_User_Adapter (
    val source: List<TUserWorkgroupObj>,
    var onItemClick : (user: TUserWorkgroupObj) -> Unit
): RecyclerView.Adapter<Act085UserVH>(), Filterable {
    private var sourceFiltered: MutableList<TUserWorkgroupObj> = mutableListOf()
    var mFilter = UserFilter()
    //
    init {
        sourceFiltered.addAll(source)
    }
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Act085UserVH {
        return Act085UserVH(
            Act085UserListFrgCellBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )
    }
    //
    override fun onBindViewHolder(holder: Act085UserVH, position: Int) {
        holder.onBindData(
            sourceFiltered[position]
        )
    }
    //
    override fun getItemCount(): Int {
        return sourceFiltered.size
    }
    //
    override fun getFilter(): Filter {
        return mFilter
    }
    //
    inner class UserFilter() : Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<TUserWorkgroupObj>()
            val charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if(charFilter.isNullOrEmpty()){
                temp = source as MutableList<TUserWorkgroupObj>
            }else{
                temp.addAll(
                    source.filter {
                        when(it){
                            is TUserWorkgroupObj ->{
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
                sourceFiltered = results.values as MutableList<TUserWorkgroupObj>
                notifyDataSetChanged()
            }
        }
    }
}