package com.namoadigital.prj001.ui.act092.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act020HeaderListBinding
import com.namoadigital.prj001.databinding.MyActionsItemBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfSourceExists
import com.namoadigital.prj001.model.MyActions
import com.namoadigital.prj001.model.MyActionsBase
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

class Act092_Adapter constructor(
    private val source: List<MyActionsBase>,
    private val hmAux: HMAux,
    private val myActionClickListener: (myAction: MyActions, position: Int) -> Unit,
    private val notifyFilterApplied: (qtyItensFiltered: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {


    val defaultList = (source as List<MyActions>).separateListByHeader()
    var filterList = defaultList.toMutableList()
    val mFilter = ServiceFilter()

    var userMainFilter = false

    private fun List<MyActions>.separateListByHeader(): MutableList<SerialViewItem> {
        val newList = mutableListOf<SerialViewItem>()
        sortedBy {
            it.doneDate?.isNotEmpty() == true
        }.map {
            if (!it.doneDate.isNullOrEmpty()) {
                if (newList.contains(SerialViewItem.SectionItem)) {
                    newList.add(SerialViewItem.ContentItem(it))
                } else {
                    newList.add(SerialViewItem.SectionItem)
                    newList.add(SerialViewItem.ContentItem(it))
                }
            } else {
                newList.add(SerialViewItem.ContentItem(it))
            }
        }

        return if (newList.size == 0) emptyList<SerialViewItem>().toMutableList() else newList

    }

    override fun getItemViewType(position: Int): Int {
        if (filterList[position] is SerialViewItem.SectionItem) {
            return SerialViewItem.VIEW_TYPE_SECTION
        }
        return SerialViewItem.VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == SerialViewItem.VIEW_TYPE_SECTION) {
            return DoneItemHolder(
                Act020HeaderListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            MyActionsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = filterList[position]
        if (holder is DoneItemHolder && item is SerialViewItem.SectionItem) {
            holder.onBinding(hmAux["done_action_list_limiter_lbl"])
        }
        if (holder is ViewHolder && item is SerialViewItem.ContentItem) {
            holder.onBinding(item.item as MyActions)
        }
    }

    override fun getItemCount() = filterList.size


    inner class DoneItemHolder constructor(
        private val binding: Act020HeaderListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(header: String?) {
            with(binding) {
                headerText.text = header
            }
        }
    }

    inner class ViewHolder constructor(
        private val binding: MyActionsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(item: MyActions) {
            with(binding) {

                act083SerialInfo.visibility = View.GONE
                serialDetail.visibility = View.GONE
                myActionSelectSerial.apply {
                    visibility = View.VISIBLE
                    if (item.actionType == MyActions.MY_ACTION_TYPE_SCHEDULE && !item.hasUserFocus) {
                        visibility = View.GONE
                    } else if (!item.pdfUrl.isNullOrEmpty()
                        || MyActions.MY_ACTION_TYPE_TICKET_CACHE == item.actionType
                    ) {
                        text = hmAux["cell_download_action_lbl"]
                        setTextColor(resources.getColor(R.color.m3_namoa_surface))
                    } else if (item.highlightItem) {
                        text = hmAux["cell_continue_action_lbl"]
                        setTextColor(Color.parseColor("#462A00"))
                    } else {
                        text = hmAux["cell_open_action_lbl"]
                        setTextColor(resources.getColor(R.color.m3_namoa_surface))

                    }
                }
                //
                myActionSelectSerial.setOnClickListener {
                    myActionClickListener(item, adapterPosition)
                }
                //
                myActionsItemTvCode.text = item.processId
                myActionsItemTvClassStatus.visibility = View.GONE
                if((item.actionType == MyActions.MY_ACTION_TYPE_TICKET
                    ||item.actionType == MyActions.MY_ACTION_TYPE_TICKET_CACHE)
                    && !item.classId.isNullOrEmpty()) {
                    myActionsItemTvClassStatus.text = item.classId
                    myActionsItemTvClassStatus.setTextColor(Color.parseColor(item.classColor))
                    myActionsItemTvClassStatus.visibility = View.VISIBLE
                }
                //
                configPlannedDate(item)
                configDoneDate(item)
                //
                myActionsItemIvIconLeft.applyVisibilityIfSourceExists(item.processLeftIcon)
                myActionsItemIvIconMid.applyVisibilityIfSourceExists(item.processMidIcon)
                myActionsItemIvIconMainUser.applyVisibilityIfSourceExists(item.processRightIcon)
                //
                configTvTag(item)
                myActionsItemTvProdDesc.text = item.productDesc
                myActionsItemTvSerialId.text = item.serialId
                if (ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL != item.ticketOriginType) {
                    configTvOriginView(item)
                    myActionsItemTvOrigin.visibility = View.VISIBLE
                } else {
                    myActionsItemTvOrigin.visibility = View.GONE
                }
                myActionsItemTvFocusStepDesc.applyVisibilityIfTextExists(
                    if (item.focusStepDesc.isNullOrEmpty()) {
                        null
                    } else {
                        "${hmAux["cell_step_lbl"]}: ${item.focusStepDesc}"
                    }
                )
                //
                configTvSite(item)
                myActionsItemTvDoneDate.applyVisibilityIfTextExists(item.doneDate)
                myActionsItemTvActionProcess.applyVisibilityIfTextExists(item.processDesc)
                //
                myActionsItemTvInternalComments.applyVisibilityIfTextExists(
                    getInfoQuotesFormatted(
                        item.internalComments
                    )
                )
                //
                binding.myActionsItemTvJustifyId.apply {
                    applyVisibilityIfTextExists(item.justify_item_id)
                }
                //
                binding.myActionsItemTvJustifyDesc.apply {
                    applyVisibilityIfTextExists(item.justify_item_desc)
                }
                applyBackgroundStrokeColor(item)
            }
        }

        private fun TextView.applyVisibilityIfTextExists(text: String?) {
            this.text = text
            this.visibility = if (!this.text.isNullOrEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        private fun configDoneDate(myAction: MyActions) {
            with(binding) {
                myActionsItemTvDoneDate.apply {
                    applyVisibilityIfTextExists(myAction.doneDate)
                    if (ConstantBaseApp.SYS_STATUS_DONE == myAction.processStatus) {
                        this.setTextColor(
                            ToolBox_Inf.getStatusColorV2(
                                context,
                                myAction.processStatus
                            )
                        )
                    } else {
                        this.setTextColor(
                            context.resources.getColor(R.color.namoa_color_gray_8)
                        )
                    }
                }

            }
        }

        private fun configTvTag(myAction: MyActions) {
            with(binding) {
                myActionsItemTvTagDesc.text = myAction.tagOperationDesc?.toUpperCase() ?: null
            }
        }

        fun configTvSite(myAction: MyActions) {
            with(binding) {
                myActionsItemTvSite.apply {
                    myAction.siteCode?.let {
                        if (ToolBox_Inf.equalsToLoggedSite(context, it.toString())) {
                            visibility = View.VISIBLE
                            text = myAction.getFormattedSiteZoneDesc()
                        } else {
                            visibility = View.VISIBLE
                            text = myAction.getFormattedSiteZoneDesc() //namoa_color_danger_red
                        }
                        return@with
                    }
                    visibility = View.GONE
                    text = myAction.getFormattedSiteZoneDesc()
                }
            }
        }

        private fun configPlannedDate(myAction: MyActions) {
            with(binding) {
                myActionsItemTvPlannedDate.apply {
                    text = myAction.plannedDate
                    if(text.isNullOrEmpty()){
                        visibility = View.GONE
                    }else{
                        visibility = View.VISIBLE
                    }
                    //
                    if (myAction.doneDate.isNullOrEmpty()) {
                        when {
                            myAction.lateItem -> {
                                setTextColor(ContextCompat.getColor(context, R.color.text_red))
                            }
                            myAction.periodStarted -> {
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.namoa_status_process
                                    )
                                )
                            }
                            else -> {
                                setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.m3_namoa_onSurfaceVariant
                                    )
                                )
                            }
                        }
                    } else {
                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.m3_namoa_onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        private fun applyBackgroundStrokeColor(myAction: MyActions) {
            with(binding) {

/*                myActionsItemTvFormNoFinish.apply {
                    visibility = if(myAction.highlightItem) View.VISIBLE else View.GONE
                    text = "Contém formulário não concluído!"
                }*/

                myActionSelectSerial.apply {
                    backgroundTintList = if (myAction.highlightItem) {
                        ColorStateList.valueOf(resources.getColor(R.color.namoa_color_orange))
                    } else {
                        if (myAction.isLastSelectedItem) {
                            ColorStateList.valueOf(resources.getColor(R.color.namoa_color_yellow_2))
                        } else {
                            ColorStateList.valueOf(resources.getColor(R.color.m3_namoa_primary))
                        }
                    }

                }
            }
        }

        private fun configTvOriginView(myAction: MyActions) {
            with(binding) {
                myActionsItemTvOrigin.apply {
                    text = myAction.originDescriptor
//                Log.d("TESTE_ORIGEM", """isTicketOriginManulOrBarcode: ${isTicketOriginManulOrBarcode(myAction)}""" )
//                Log.d("TESTE_ORIGEM", """originDescriptor: ${myAction.originDescriptor}""" )
                    ellipsize = if (isTicketOriginManulOrBarcode(myAction)) {
                        TextUtils.TruncateAt.START
                    } else {
                        null
                    }
                }
            }
        }

        private fun isTicketOriginManulOrBarcode(myAction: MyActions) =
            ((MyActions.MY_ACTION_TYPE_TICKET == myAction.actionType
                    || MyActions.MY_ACTION_TYPE_TICKET_CACHE == myAction.actionType)
                    && (ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_BARCODE == myAction.ticketOriginType
                    || ConstantBaseApp.TK_TICKET_ORIGIN_TYPE_MANUAL == myAction.ticketOriginType))

        /**
         * Formata info com bullet quando há informação.
         */
        private fun getInfoQuotesFormatted(value: String?): String? {
            if (!value.isNullOrEmpty()) {
                return "\"$value\""
            }
            return null
        }

    }

    inner class ServiceFilter() : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<MyActionsBase>()
            var charFilter = ToolBox.AccentMapper(constraint.toString().toLowerCase())
            if (charFilter.isNullOrEmpty()) {
                if (userMainFilter) {
                    temp = source.filter {
                        if (it is MyActions) {
                            it.isMainUserTicket
                        } else {
                            true
                        }
                    } as MutableList<MyActionsBase>
                } else {
                    temp = source as MutableList<MyActionsBase>
                }
            } else {
                temp.addAll(
                    source.filter {
                        when (it) {
                            is MyActions -> {
                                val allFields = ToolBox.AccentMapper(
                                    it.getAllFieldForFilter().toLowerCase()
                                )
                                if (userMainFilter) {
                                    allFields.contains(charFilter) && it.isMainUserTicket
                                } else {
                                    allFields.contains(charFilter)
                                }
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
                filterList = (it.values as List<MyActions>).separateListByHeader()
                notifyDataSetChanged()
                notifyFilterApplied(filterList.size)
            }
        }


    }

    override fun getFilter(): Filter {
        return mFilter
    }
}