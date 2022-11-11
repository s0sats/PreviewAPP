package com.namoadigital.prj001.adapter.act020

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.adapter.act020.model.ProductSerialList
import com.namoadigital.prj001.adapter.act020.model.SearchSerialViewItem
import com.namoadigital.prj001.adapter.act020.model.toAdapterList
import com.namoadigital.prj001.databinding.Act020HeaderListBinding
import com.namoadigital.prj001.databinding.Act020ListItemBinding
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.util.ToolBox_Con
import java.util.*

class Act020_Adapter constructor(
    private val context: Context,
    private val hmAux: HMAux,
    private val source: ArrayList<MD_Product_Serial>,
    private val onCardClick: (product: MD_Product_Serial) -> Unit,
    private val emptyList: (listSize: Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    val defaultList = source.map { m -> m.toAdapterList(ToolBox_Con.getPreference_Site_Code(context).toInt()) }.toMutableList()
    var filterSource = defaultList.separateListByHeader()
    val mFilter = ServiceFilter()


    private fun MutableList<ProductSerialList>.separateListByHeader(): MutableList<SearchSerialViewItem>{
        val newList = mutableListOf<SearchSerialViewItem>()
        this.map {
            if (!it.currentSite) {
                if (newList.contains(SearchSerialViewItem.SectionItem(false))) {
                    newList.add(
                        SearchSerialViewItem.ContentItem(
                            it,
                            source.find { f -> it.serial == f.serial_id }!!
                        )
                    )
                } else {
                    newList.add(SearchSerialViewItem.SectionItem(false))
                    newList.add(
                        SearchSerialViewItem.ContentItem(
                            it,
                            source.find { f -> it.serial == f.serial_id }!!
                        )
                    )
                }
            } else {
                newList.add(
                    SearchSerialViewItem.ContentItem(
                        it,
                        source.find { f -> it.serial == f.serial_id }!!
                    )
                )
            }
        }
        return if(newList.size == 0) emptyList<SearchSerialViewItem>().toMutableList() else newList
    }

    override fun getItemViewType(position: Int): Int {
        if(filterSource[position] is SearchSerialViewItem.SectionItem){
           return SearchSerialViewItem.VIEW_TYPE_SECTION
        }
        return SearchSerialViewItem.VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        if(viewType == SearchSerialViewItem.VIEW_TYPE_SECTION){
            return SectionItemHolder(Act020HeaderListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        return ItemViewHolder(
            Act020ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = filterSource[position]

        if(holder is SectionItemHolder && item is SearchSerialViewItem.SectionItem){
            if(!item.currentSite){
                holder.onBinding(hmAux["other_sites_lbl"])
                return
            }
        }

        if(holder is ItemViewHolder && item is SearchSerialViewItem.ContentItem){
            holder.itemView.setOnClickListener {
                onCardClick(item.product)
            }
            holder.onBinding(item.product_serial)
        }
    }

    override fun getItemCount() = filterSource.size


    inner class SectionItemHolder constructor(
        private val binding: Act020HeaderListBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun onBinding(header: String?){
            with(binding){
                headerText.text = header
            }
        }
    }

    inner class ItemViewHolder constructor(
        private val binding: Act020ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBinding(serial: ProductSerialList?) {
            with(binding) {
                serial?.let { item ->
                    cardSerialTitle.text = item.serial
                    productDescription.text = item.product_desc


                    if (item.classColor.isNotEmpty()) {
                        iconClassColor.apply {
                            setColorFilter(
                                Color.parseColor(item.classColor),
                                PorterDuff.Mode.SRC_ATOP
                            )
                            visibility = View.VISIBLE
                        }
                    }else{
                        iconClassColor.visibility = View.GONE
                    }

                    val serial_desc = item.getSerialDescription()
                    if (serial_desc.isNotEmpty()) {
                        serialDescription.apply {
                            text = serial_desc
                            visibility = View.VISIBLE
                        }
                    }else{
                        serialDescription. visibility = View.GONE
                    }

                    val tracking = item.getTracking()
                    if (tracking.isNotEmpty()) {
                        listTrackings.apply {
                            text = tracking
                            visibility = View.VISIBLE

                        }
                        spaceSite2.visibility = View.VISIBLE
                    }else{
                        listTrackings.visibility = View.GONE
                        spaceSite2.visibility = View.GONE
                    }

                    val siteZone = item.getSiteAndZone()
                    if (siteZone.isNotEmpty()) {
                        siteAndZone.apply {
                            text = siteZone
                            visibility = View.VISIBLE
                        }
                    }else{
                        siteAndZone.visibility = View.GONE
                    }
                }

            }
        }

    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class ServiceFilter : Filter(){
        override fun performFiltering(char: CharSequence?): FilterResults {
            var temp = mutableListOf<ProductSerialList>()
            val charString = ToolBox.AccentMapper(char.toString().lowercase(Locale.getDefault()).trim())

            if(charString.isNullOrEmpty()) {
                temp = defaultList
            }else {
                temp.addAll(
                    defaultList.filter {
                        val allFilter = ToolBox.AccentMapper(it.getAllFieldForFilter().lowercase(Locale.getDefault()).trim())
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
                filterSource = (it.values as MutableList<ProductSerialList>).separateListByHeader()
                notifyDataSetChanged()
                emptyList(filterSource.size)
            }
        }

    }
}