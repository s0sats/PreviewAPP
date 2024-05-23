package com.namoadigital.prj001.adapter.trip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.trip.mapping.toUserEdit
import com.namoadigital.prj001.adapter.trip.model.Extract
import com.namoadigital.prj001.adapter.trip.model.ExtractType
import com.namoadigital.prj001.adapter.trip.viewholder.TripDestinationActionViewHolder
import com.namoadigital.prj001.adapter.trip.viewholder.TripDestinationViewHolder
import com.namoadigital.prj001.adapter.trip.viewholder.TripEventViewHolder
import com.namoadigital.prj001.adapter.trip.viewholder.TripOriginViewHolder
import com.namoadigital.prj001.adapter.trip.viewholder.TripUserViewHolder
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestinationAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment
import java.util.Locale

class ExtractAdapter constructor(
    private val context: Context,
    private val source: List<Extract<*>>,
    private val hmAuxTranslate: HMAux,
    private val updateList: (Int) -> Unit,
    private val onSelectUser: (TripUserEdit, Int) -> Unit,
    private val onSelectEvent: (FSTripEvent, Int) -> Unit,
    private val onSelectOrigin: (FSTrip, Int) -> Unit,
    private val onSelectDestination: (FsTripDestination, Int) -> Unit,
    private val onSelectAction: (FsTripDestinationAction, Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredList: MutableList<Extract<*>> = mutableListOf()

    init {
        filteredList.addAll(source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            USER_VIEWTYPE -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.trip_extract_user_item, parent, false)
                TripUserViewHolder(context, view, hmAuxTranslate) { item, position ->
                    onSelectUser(item.toUserEdit(), position)
                }
            }

            EVENT_VIEWTYPE -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.trip_extract_event_item, parent, false)
                TripEventViewHolder(context, view, hmAuxTranslate) { item, position ->
                    onSelectEvent(item, position)
                }
            }

            ORIGIN_VIEWTYPE -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.trip_extract_origin_destnation_item, parent, false)
                TripOriginViewHolder(context, view, hmAuxTranslate) { item, position ->
                    onSelectOrigin(item, position)
                }
            }

            DESTINATION_VIEWTYPE -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.trip_extract_origin_destnation_item, parent, false)
                TripDestinationViewHolder(context, view, hmAuxTranslate){ item, position ->
                    onSelectDestination(item, position)
                }
            }

            ACTION_VIEWTYPE -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.trip_extract_user_item, parent, false)
                TripDestinationActionViewHolder(context, view, hmAuxTranslate){  item, position ->
                    onSelectAction(item, position)
                }
            }
            else -> {null!!}
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = filteredList[position]

        when (holder) {

            is TripUserViewHolder -> holder.bind(item.model as FSTripUser, position)
            is TripEventViewHolder -> holder.bind(item.model as FSTripEvent, position)
            is TripOriginViewHolder -> holder.bind(item.model as FSTrip, position)
            is TripDestinationViewHolder -> holder.bind(item.model as FsTripDestination, position)
            is TripDestinationActionViewHolder -> holder.bind(item.model as FsTripDestinationAction, position)

        }
    }

    override fun getItemCount() = filteredList.size

    override fun getItemViewType(position: Int): Int {

        return when (filteredList[position].type) {
            ExtractType.EVENT -> EVENT_VIEWTYPE
            ExtractType.USER -> USER_VIEWTYPE
            ExtractType.ORIGIN -> ORIGIN_VIEWTYPE
            ExtractType.DESTINATION -> DESTINATION_VIEWTYPE
            ExtractType.ACTION -> ACTION_VIEWTYPE
        }
    }

    fun filter(query: String){
        filteredList.clear()
        if(query.isEmpty()){
            filteredList.addAll(source)
        }else{
            val lowercaseQuery = query.lowercase(Locale.getDefault())
            source.forEach { extract ->
                when(extract.type){

                    ExtractType.ORIGIN -> {
                        filteredList.add(extract)
                    }

                    ExtractType.DESTINATION -> {
                        filteredList.add(extract)
                    }

                    else -> {
                        if(extract.filter.lowercase().contains(lowercaseQuery)){
                            filteredList.add(extract)
                        }
                    }

                }
            }
        }
        notifyDataSetChanged()
        updateList(filteredList.size)
    }

    companion object {
        private const val EVENT_VIEWTYPE = 0
        private const val USER_VIEWTYPE = 1
        private const val ORIGIN_VIEWTYPE = 2
        private const val DESTINATION_VIEWTYPE = 3
        private const val ACTION_VIEWTYPE = 4
    }

}