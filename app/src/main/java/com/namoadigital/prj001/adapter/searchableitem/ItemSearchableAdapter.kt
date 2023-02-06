package com.namoadigital.prj001.adapter.searchableitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.databinding.SearchableAdapterItemBinding
import com.namoadigital.prj001.design.list.OnRememberListState

class ItemSearchableAdapter constructor(
    private val source: List<MyItemSearchableAdapter>,
    private val onItemClick: (MyItemSearchableAdapter) -> Unit,
    private val onRememberListState: OnRememberListState<MyItemSearchableAdapter>
) : RecyclerView.Adapter<ItemSearchableAdapter.ItemViewHolder>(), Filterable {


    private var sourceFilter = source.toMutableList()
    val itemFilter = ItemFilter()

    override fun getFilter(): Filter {
        return itemFilter
    }


    inner class ItemFilter : Filter() {
        override fun performFiltering(char: CharSequence?): FilterResults {
            var filter = mutableListOf<MyItemSearchableAdapter>()
            val charString = ToolBox.AccentMapper(char.toString().toLowerCase())

            if (charString.isNullOrEmpty()) {
                filter = source.toMutableList()
            } else {
                filter.addAll(
                    source.filter {
                        val all = ToolBox.AccentMapper(it.text?.toLowerCase())
                        all.contains(charString)
                    }
                )
            }


            return FilterResults().apply {
                count = filter.size
                values = filter
            }
        }

        override fun publishResults(p0: CharSequence?, filter: FilterResults?) {
            filter?.let {
                sourceFilter = it.values as MutableList<MyItemSearchableAdapter>
                onRememberListState.dataChanged(sourceFilter as ArrayList<MyItemSearchableAdapter>)
                notifyDataSetChanged()
            }
        }

    }

    inner class ItemViewHolder constructor(
        private val binding: SearchableAdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MyItemSearchableAdapter) {
            with(binding) {
                searchableDescrip.text = item.text
                searchableDivider.visibility = View.VISIBLE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            SearchableAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = sourceFilter.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick(sourceFilter[position])
        }
        holder.onBind(sourceFilter[position])
    }


}