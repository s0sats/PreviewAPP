package com.namoadigital.prj001.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.SelectDestinationItemBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.extensions.highlightItem
import com.namoadigital.prj001.extensions.setLateColor
import com.namoadigital.prj001.extensions.setNextColor
import com.namoadigital.prj001.model.trip.FsTripDestination.Companion.TICKET_DESTINATION_TYPE
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable
import com.namoadigital.prj001.ui.act094.domain.model.SelectionDestinationAvailable.Companion.convertZeroToLine
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.Locale

class SelectDestinationAdapter(
    private val source: List<SelectionDestinationAvailable>,
    private val onSelectItem: (SelectionDestinationAvailable) -> Unit,
    private val onDetailItem: (SelectionDestinationAvailable) -> Unit,
    private val notifyFilterApplied: (qtyItemsFiltered: Int) -> Unit,
    private val goToAct083: (SelectionDestinationAvailable) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    lateinit var context: Context
    var filterList = source.toMutableList()
    val mFilter = SelectDestinationFilter()
    var hmAux_Trans = HMAux()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        loadTranslation()
        return SelectionItemHolder(
            SelectDestinationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectionItemHolder).onBinding(filterList[position])
    }


    inner class SelectionItemHolder(
        private val binding: SelectDestinationItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(item: SelectionDestinationAvailable) {

            with(binding) {

                val isTicket = item.destinationType == TICKET_DESTINATION_TYPE

                if (isTicket) {
                    cardTicket(item)
                } else {
                    cardDefault(item)
                }

                if (datePlanned.visibility == View.GONE && flagActionMe.visibility == View.GONE) {
                    constraintLayout.visibility = View.GONE
                } else {
                    constraintLayout.visibility = View.VISIBLE
                }


                detailAdress.apply {
                    visibility = tvAddressLocation.visibility
                    setOnClickListener {
                        onDetailItem(item)
                    }
                }

                selectItemButton.apply {
                    text = hmAux_Trans[BTN_SELECT_DESTINATION]
                    setOnClickListener { view ->
                        onSelectItem(item)
                    }
                }

            }

        }
    }

    private fun SelectDestinationItemBinding.cardDefault(item: SelectionDestinationAvailable) {
        if (item.siteMainUser == ToolBox_Con.getPreference_User_Code(flagActionMe.context)
                .toInt()
        ) {
            flagActionMe.visibility = View.VISIBLE
        } else {
            flagActionMe.visibility = View.GONE
        }

        if (!item.minDate.isNullOrEmpty()) {
            val date = ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(item.minDate),
                ToolBox_Inf.nlsDateFormat(context)
            )
            datePlanned.apply {
                val label = if(ticketsCnt(item) > 0 ) hmAux_Trans[PLANNED_FOR_LBL] else hmAux_Trans[NO_TICKETS_PLANNED_LBL]
                text = "$label $date"
                datePlanned.visibility = View.VISIBLE
            }
        } else {
            datePlanned.visibility = View.VISIBLE
            datePlanned.apply {
                text = "${hmAux_Trans[NO_TICKETS_PLANNED_LBL]}"
                datePlanned.visibility = View.VISIBLE
            }
        }

        regionName.applyVisibilityIfTextExists(item.regionDesc)
        siteActionName.applyVisibilityIfTextExists(item.siteDesc)
        tvAddressLocation.applyVisibilityIfTextExists(item.getFullAddress())
        //
        vCategory.setBackgroundColor(Color.parseColor(getDestinationColor(item)))
        //
        urgentValue.apply {
            text = item.priorityCnt.convertZeroToLine()
            visibility = View.VISIBLE
            highlightItem(item.priorityCnt, R.color.m3_namoa_onSurfaceVariant)
        }
        forTodayValue.apply {
            text = item.todayCnt.convertZeroToLine()
            visibility = View.VISIBLE
            highlightItem(item.todayCnt, R.color.namoa_destination_tag_today)
        }
        lateValue.apply {
            text = item.lateCnt.convertZeroToLine()
            visibility = View.VISIBLE
            setLateColor(item.todayCnt, item.lateCnt)
        }
        nextValue.apply {
            text = item.nextCnt.convertZeroToLine()
            visibility = View.VISIBLE
            setNextColor(item.todayCnt, item.lateCnt, item.nextCnt)
        }
        serialsValue.apply {
            text = item.serialCnt.convertZeroToLine()
            visibility = View.VISIBLE
            highlightItem(item.serialCnt, R.color.m3_namoa_onSurfaceVariant)
        }
        urgentLabel.apply {
            text = hmAux_Trans[URGENT_LBL]
            highlightItem(item.priorityCnt, R.color.m3_namoa_onSurfaceVariant)
        }
        forTodayLabel.apply {
            text = hmAux_Trans[TODAY_LBL]
            highlightItem(item.todayCnt, R.color.namoa_destination_tag_today)
        }
        lateLabel.apply {
            text = hmAux_Trans[LATE_LBL]
            setLateColor(item.todayCnt, item.lateCnt)
        }
        nextLabel.apply {
            text = hmAux_Trans[NEXT_LBL]
            setNextColor(item.todayCnt, item.lateCnt, item.nextCnt)
        }
        serialsLabel.apply {
            text = hmAux_Trans[SERIALS_LBL]
            visibility = View.VISIBLE
            highlightItem(item.serialCnt, R.color.m3_namoa_onSurfaceVariant)
        }
        serialLayout.visibility = View.GONE
        seeTicketsButton.apply {
            text = hmAux_Trans[SEE_TICKETS_LBL]
            visibility = View.VISIBLE
            setOnClickListener {
                goToAct083(item)
            }
        }
    }

    private fun ticketsCnt(item: SelectionDestinationAvailable): Int {
        return (item.priorityCnt?:0) + (item.todayCnt?:0) + (item.lateCnt?:0) + (item.nextCnt?:0)
    }


    private fun SelectDestinationItemBinding.cardTicket(item: SelectionDestinationAvailable) {
        urgentValue.visibility = View.GONE
        forTodayValue.visibility = View.GONE
        lateValue.visibility = View.GONE
        nextValue.visibility = View.GONE
        serialsValue.visibility = View.INVISIBLE
        serialsLabel.visibility = View.INVISIBLE
        serialsLabel.text = hmAux_Trans[SERIALS_LBL]
        vCategory.setBackgroundColor(Color.parseColor(getDestinationColor(item)))
        regionName.applyVisibilityIfTextExists(item.regionDesc)
        siteActionName.text = hmAux_Trans[EXTERNAL_ADDRESS]
        tvAddressLocation.applyVisibilityIfTextExists(item.getFullAddress())

        if (!item.minDate.isNullOrEmpty()) {
            datePlanned.apply {
                text = ToolBox_Inf.millisecondsToString(
                    ToolBox_Inf.dateToMilliseconds(item.minDate),
                    ToolBox_Inf.nlsDateFormat(context)
                )

                visibility = View.VISIBLE
            }
        } else {
            datePlanned.visibility = View.GONE
        }

        urgentLabel.apply {
            text = hmAux_Trans[URGENT_LBL]
            highlightItem(item.priorityCnt, R.color.m3_namoa_onSurfaceVariant)
        }
        forTodayLabel.apply {
            text = hmAux_Trans[TODAY_LBL]
            highlightItem(item.todayCnt, R.color.namoa_destination_tag_today)
        }
        lateLabel.apply {
            text = hmAux_Trans[LATE_LBL]
            setLateColor(item.todayCnt, item.lateCnt)
        }
        nextLabel.apply {
            text = hmAux_Trans[NEXT_LBL]
            setNextColor(item.todayCnt, item.lateCnt, item.nextCnt)
        }

        seeTicketsButton.visibility = View.GONE
        serialLayout.visibility = View.VISIBLE
        serialId.applyVisibilityIfTextExists(item.serialId)
        serialSiteValue.applyVisibilityIfTextExists(item.siteDesc)

    }

    private fun getDestinationColor(item: SelectionDestinationAvailable): String {
        var color = "#EFF1F3"
        if((item.todayCnt ?: 0) > 0){
            color = "#2962FF"
        }
        if((item.todayCnt ?: 0) == 0
            && (item.lateCnt ?: 0) > 0
            ){
            color = "#39257C"
        }
        if((item.todayCnt ?: 0) == 0
            && (item.lateCnt ?: 0) == 0
            && (item.nextCnt ?: 0) > 0
            ){
            color = "#D1CAE5"
        }
        return color
    }


    private fun loadTranslation() {
        listOf(
            EXTERNAL_ADDRESS,
            URGENT_LBL,
            TODAY_LBL,
            LATE_LBL,
            NEXT_LBL,
            SERIALS_LBL,
            BTN_SELECT_DESTINATION,
            SEE_TICKETS_LBL,
            PLANNED_FOR_LBL,
            NO_TICKETS_PLANNED_LBL,
        ).let { list ->
            resourceCode = ToolBox_Inf.getResourceCode(
                context,
                moduleCode,
                resourceName
            )

            hmAux_Trans = ToolBox_Inf.setLanguage(
                context,
                moduleCode,
                resourceCode,
                ToolBox_Con.getPreference_Translate_Code(context),
                list
            )
        }
    }

    inner class SelectDestinationFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var temp = mutableListOf<SelectionDestinationAvailable>()

            val charFilter = ToolBox.AccentMapper(
                constraint.toString().lowercase(Locale.getDefault())
            )

            if (charFilter.isNullOrEmpty()) {
                temp = source.toMutableList()
            } else {
                temp.addAll(
                    source.filter {
                        val allFields = ToolBox.AccentMapper(
                            it.getAllFieldForFilter(
                                context,
                                hmAux_Trans[EXTERNAL_ADDRESS] ?: "EXTERNAL_ADDRESS"
                            ).lowercase(Locale.getDefault())
                        )
                        allFields.contains(charFilter)
                    }
                )
            }

            val filterResult = FilterResults()
            filterResult.count = temp.size
            filterResult.values = temp
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.let {
                filterList = (it.values as List<SelectionDestinationAvailable>).toMutableList()
                notifyDataSetChanged()
                notifyFilterApplied(filterList.size)
            }
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }


    companion object {

        var resourceCode = "0"
        val resourceName = "act094_adapter"
        val moduleCode = Constant.APP_MODULE


        const val EXTERNAL_ADDRESS = "external_address"
        const val URGENT_LBL = "urgent_lbl"
        const val TODAY_LBL = "today_lbl"
        const val LATE_LBL = "late_lbl"
        const val NEXT_LBL = "next_lbl"
        const val SERIALS_LBL = "serials_lbl"
        const val PLANNED_FOR_LBL = "planned_for_lbl"
        const val NO_TICKETS_PLANNED_LBL = "no_tickets_planned_lbl"
        const val BTN_SELECT_DESTINATION = "btn_select_destination"
        const val SEE_TICKETS_LBL = "see_tickets_lbl"
    }
}