package com.namoadigital.prj001.adapter.trip.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.TripExtractEventItemBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.model.trip.FSTripEvent
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment.Companion.EXTRACT_CARD_EVENT_COST_LBL
import dagger.hilt.android.AndroidEntryPoint

class TripEventViewHolder constructor(
    private val context: Context,
    private val view: View,
    private val hmAuxTranslate: HMAux,
    private val onClick: (FSTripEvent, Int) -> Unit,
) : RecyclerView.ViewHolder(view) {

    private lateinit var binding: TripExtractEventItemBinding
    init {
        init()
    }
    fun init() {
        binding = TripExtractEventItemBinding.bind(view)
        initializeViews()
    }
    private fun initializeViews() {
        with(binding) {
            this.tvTitle.text = hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_EVENT_LBL]
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: FSTripEvent, position: Int) {
        with(binding) {
            val dateStart = context.formatDate(FormatDateType.OnlyDate(item.eventStart!!))
            val dateEnd = if(item.eventEnd == null) hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_NOT_FINALIZED_LBL]
            else {
                val date = context.formatDate(FormatDateType.OnlyDate(item.eventEnd!!))
                if(date == dateStart) {
                    if(!item.waitAllowed){ ""
                    } else{ context.formatDate(FormatDateType.OnlyHour(item.eventEnd!!)) }
                } else { item.eventEnd!!.formatDateText() }
            }

            if(dateEnd?.isEmpty() == true){
                this.tvDate.text = "${item.eventStart?.formatDateText() ?: ""}"
            }else{
                this.tvDate.text = "${item.eventStart?.formatDateText() ?: ""} - $dateEnd"
            }

            this.tvEventName.applyVisibilityIfTextExists(item.eventTypeDesc)
            this.tvCost.applyVisibilityIfTextExists(item.cost?.let { cost ->
                val formattedValue = String.format("%.2f", cost)
                val text = formattedValue.replace(".", ",")
                "${hmAuxTranslate[EXTRACT_CARD_EVENT_COST_LBL]}: $text"
            } ?: "")

            if (item.comment.isNullOrEmpty()) {
                this.tvComment.visibility = View.GONE
            } else {
                this.tvComment.apply {
                    text = "\"${item.comment}\""
                    visibility = View.VISIBLE
                }
            }

            val (local, remote, url) = item.getEventPhoto()

            if (local.isNullOrEmpty() && remote.isEmpty() && url.isEmpty()) {
                this.iconCamera.visibility = View.GONE
            } else {
                this.iconCamera.visibility = View.VISIBLE
            }


            if (!item.eventEnd.isNullOrEmpty()) {
                this.cardView.isEnabled = true
                this.cardView.setOnClickListener {
                    onClick(item, position)
                }
            } else {
                this.cardView.isEnabled = false
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