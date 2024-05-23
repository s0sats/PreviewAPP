package com.namoadigital.prj001.adapter.trip.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.TripExtractUserItemBinding
import com.namoadigital.prj001.extensions.date.FormatDateType
import com.namoadigital.prj001.extensions.date.formatDate
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.ui.act005.trip.fragment.extract.TripExtractFragment

class TripUserViewHolder constructor(
    private val context: Context,
    private val view: View,
    private val hmAuxTranslate: HMAux,
    private val onClick: (FSTripUser, Int) -> Unit,
): RecyclerView.ViewHolder(view) {

    private lateinit var binding: TripExtractUserItemBinding


    init {
        init()
    }


    fun init(){
        binding = TripExtractUserItemBinding.bind(view)
        initializeViews()
    }

    private fun initializeViews(){
        with(binding){
            this.tvTitle.text = hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_USER_CURRENT_TRIP_LBL]
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: FSTripUser, position: Int){
        with(binding){
            val dateStart = context.formatDate(FormatDateType.OnlyDate(item.dateStart))
            val dateEnd = if(item.dateEnd.isNullOrBlank()) hmAuxTranslate[TripExtractFragment.EXTRACT_CARD_NOT_FINALIZED_LBL]
            else {
                val date = context.formatDate(FormatDateType.OnlyDate(item.dateEnd))
                if(date == dateStart) context.formatDate(FormatDateType.OnlyHour(item.dateEnd))
                else item.dateEnd.formatDateText()
            }

            this.tvDate.text = "${item.dateStart.formatDateText() ?: ""} - $dateEnd"
            this.tvUserName.text = item.userName
            this.cardView.setOnClickListener {
                onClick(item, position)
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