package com.namoadigital.prj001.adapter.trip.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.TripExtractOriginDestnationItemBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.model.trip.FsTripDestination.Companion.OVER_NIGHT_DESTINATION_TYPE
import com.namoadigital.prj001.model.trip.FsTripDestination.Companion.TICKET_DESTINATION_TYPE
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment.Companion.EXTRACT_CARD_NOT_FINALIZED_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment.Companion.EXTRACT_CARD_NOT_INFORMED_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment.Companion.EXTRACT_CARD_ODOMETER_LBL
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment.Companion.EXTRACT_CARD_OVER_NIGHT_LBL

class TripDestinationViewHolder constructor(
    private val context: Context,
    private val view: View,
    private val hmAuxTranslate: HMAux,
    private val onClick: (FsTripDestination, Int) -> Unit
) : RecyclerView.ViewHolder(view) {


    //Usando mesma bind da origem, já que os cards são parecidos
    private lateinit var binding: TripExtractOriginDestnationItemBinding

    init {
        init()
    }

    private fun init() {
        binding = TripExtractOriginDestnationItemBinding.bind(view)

        binding.tvPlateFleet.visibility = View.GONE
    }

    fun bind(item: FsTripDestination, position: Int) {
        with(binding) {

            if(item.destinationType == OVER_NIGHT_DESTINATION_TYPE){
                this.tvOdometer.visibility = View.GONE
            }else{
                this.tvOdometer.visibility = View.VISIBLE
                if(item.arrivedFleetOdometer == null){
                    tvOdometer.text = "${hmAuxTranslate[EXTRACT_CARD_ODOMETER_LBL]}: ${hmAuxTranslate[EXTRACT_CARD_NOT_INFORMED_LBL]}"
                }else{
                    tvOdometer.text = "${hmAuxTranslate[EXTRACT_CARD_ODOMETER_LBL]}: ${item.arrivedFleetOdometer} ${hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_KM_LBL]}"
                }}

            binding.tvUserResp.applyVisibilityIfTextExists(item.getAddress())

            val dateStart = context.formatDate(FormatDateType.OnlyDate(item.arrivedDate!!))
            val dateEnd = if(item.departedDate.isNullOrEmpty()) {
                hmAuxTranslate[EXTRACT_CARD_NOT_FINALIZED_LBL]
            }else {
                val date = context.formatDate(FormatDateType.OnlyDate(item.departedDate))
                if(date == dateStart) context.formatDate(FormatDateType.OnlyHour(item.departedDate))
                else item.departedDate.formatDateText()
            }

            this.tvDate.text = "${item.arrivedDate?.formatDateText()} - $dateEnd"
            this.tvTitle.text = when(item.destinationType){
                OVER_NIGHT_DESTINATION_TYPE -> hmAuxTranslate[EXTRACT_CARD_OVER_NIGHT_LBL]
                TICKET_DESTINATION_TYPE -> item.ticketPk
                else -> item.destinationSiteDesc
            }

            val (local, remote, url) = item.arrivedDestinationPhoto
            if (local.isNullOrEmpty() && remote.isEmpty() && url.isEmpty()) {
                this.iconCamera.visibility = View.GONE
            } else {
                this.iconCamera.visibility = View.VISIBLE
            }

            this.cardView.setOnClickListener {
                onClick(item, position)
            }
        }
    }


    private fun String?.formatDateText(): String? {
        return if (this.isNullOrEmpty()) {
            null
        } else {
            context.formatDate(FormatDateType.DateAndHour(this))
        }
    }
}