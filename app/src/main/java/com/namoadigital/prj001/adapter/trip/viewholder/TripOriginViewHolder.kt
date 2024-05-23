package com.namoadigital.prj001.adapter.trip.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.TripExtractOriginDestnationItemBinding
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.TripStatus
import com.namoadigital.prj001.model.trip.toTripStatus
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.origin.enums.OriginType
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment.Companion.EXTRACT_CARD_NOT_INFORMED_LBL

class TripOriginViewHolder constructor(
    private val context: Context,
    private val view: View,
    private val hmAuxTranslate: HMAux,
    private val onClick: (FSTrip, Int) -> Unit,
): RecyclerView.ViewHolder(view) {

    private lateinit var binding: TripExtractOriginDestnationItemBinding


    init {
        init()
    }


    fun init(){
        binding = TripExtractOriginDestnationItemBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: FSTrip, position: Int){
        with(binding){
            val originType = OriginType.valueOf(item.originType ?: "")
            this.tvTitle.text = if(originType == OriginType.GPS) hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_ORIGIN_GPS_LBL]
            else item.originSiteDesc

            this.tvDate.text = item.originDate?.formatDateText() ?: ""
            tvUserResp.visibility = View.VISIBLE
            tvPlateFleet.visibility = View.VISIBLE
            tvUserResp.text = "${hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_USER_RESPONSIBLE_LBL]}: ${item.tripUserName}"
            tvPlateFleet.text = "${hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_PLATE_FLEET_LBL]}: ${item.fleetLicencePlate ?: hmAuxTranslate[EXTRACT_CARD_NOT_INFORMED_LBL]}"
            if(item.fleetStartOdometer == null){
                tvOdometer.text = "${hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_ODOMETER_LBL]}: ${hmAuxTranslate[EXTRACT_CARD_NOT_INFORMED_LBL]}"
            }else{
                tvOdometer.text = "${hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_ODOMETER_LBL]}: ${item.fleetStartOdometer} ${hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_KM_LBL]}"
            }

            val (local, remote, url) = item.photoFleetStart
            if(local.isNullOrEmpty() && remote.isEmpty() && url.isEmpty()) {
                this.iconCamera.visibility = View.GONE
            }else{
                this.iconCamera.visibility= View.VISIBLE
            }

            if(item.tripStatus.toTripStatus() != TripStatus.PENDING) {
                cardView.setOnClickListener {
                    onClick(item, position)
                }
            }
        }
    }


    private fun String?.formatDateText(): String? {
        return if(this.isNullOrEmpty()){
            null
        }else{
            context.formatDate(FormatDateType.DateAndHour(this))
        }
    }

}