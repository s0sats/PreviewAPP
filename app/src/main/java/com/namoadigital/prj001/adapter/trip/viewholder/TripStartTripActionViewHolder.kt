package com.namoadigital.prj001.adapter.trip.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.TripExtractStartTripItemBinding
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment

class TripStartTripActionViewHolder constructor(
    private val context: Context,
    private val view: View,
    private val hmAuxTranslate: HMAux,
    private val onClick: (FSTrip, Int) -> Unit,
) : RecyclerView.ViewHolder(view) {

    private lateinit var binding: TripExtractStartTripItemBinding


    init {
        init()
    }


    fun init() {
        binding = TripExtractStartTripItemBinding.bind(view)
        initializeViews()
    }

    private fun initializeViews() {
        with(binding) {
            this.tvTitle.text = hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_START_TRIP_LBL]
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: FSTrip, position: Int) {
        with(binding) {
            val dateStart = context.formatDate(FormatDateType.OnlyDate(item.startDate!!))

            this.tvDate.text = item.startDate.formatDateText() ?: ""
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