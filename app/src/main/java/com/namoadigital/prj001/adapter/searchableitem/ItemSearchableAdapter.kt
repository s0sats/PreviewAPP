package com.namoadigital.prj001.adapter.searchableitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.MdTagDao
import com.namoadigital.prj001.databinding.SearchableAdapterItemBinding

class ItemSearchableAdapter constructor(
    private val source: List<MyItemSearchableAdapter>,
    private val onItemClick: (HMAux) -> Unit
) : RecyclerView.Adapter<ItemSearchableAdapter.ItemViewHolder>() {


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

    override fun getItemCount() = source.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val toConvert = HMAux()
            toConvert[MdTagDao.TAG_CODE] = source[position].code
            toConvert[MdTagDao.TAG_DESC] = source[position].text
            onItemClick(toConvert)
        }
        holder.onBind(source[position])
    }


}