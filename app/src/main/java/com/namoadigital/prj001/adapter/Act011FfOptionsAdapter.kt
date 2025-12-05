package com.namoadigital.prj001.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act011FfOptionsCellBinding
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.model.Act011FormTabStatus
import com.namoadigital.prj001.ui.act011.Act011FfOption
import java.util.Locale

class Act011FfOptionsAdapter(
    val tabs: List<Act011FormTab>,
    var tabSelected: Int,
    val isFormSO: Boolean,
    val requiredByTicketTranslate: String,
    val mListener: Act011FfOption.ICustom_Form_FF_Options_ll
) : RecyclerView.Adapter<Act011FfOptionsAdapter.MyFormTabVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFormTabVH {
        return MyFormTabVH(
            Act011FfOptionsCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: MyFormTabVH, position: Int) {
        val tab = tabs[position]
        holder.onBinding(tab, position)

    }

    override fun getItemCount(): Int {
        return tabs.size
    }


    inner class MyFormTabVH(
        val binding: Act011FfOptionsCellBinding,
        val mListener: Act011FfOption.ICustom_Form_FF_Options_ll
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(item: Act011FormTab, position: Int) {
            with(binding) {
                if (item.fieldCount == -1) {
                    tvFormTabFieldsCount.visibility = View.INVISIBLE
                } else {
                    tvFormTabFieldsCount.visibility = View.VISIBLE
                }
                clTabItem.apply {
                    setOnClickListener {
                        mListener.onTabSelected(item.page)
                    }
                    when (item.status) {
                        Act011FormTabStatus.OK -> {
                            setBackgroundColor(
                                context.getResources().getColor(R.color.namoa_color_light_green4)
                            )
                        }
                        //
                        Act011FormTabStatus.ERROR -> {
                            setBackgroundColor(
                                context.getResources().getColor(R.color.namoa_os_form_tab_pending)
                            )
                        }
                        //
                        else -> {
                            background = null
                            setBackgroundColor(0x00000000)
                        }
                    }
                    if (item.page == tabSelected) {
                        setBackgroundColor(
                            context.getResources().getColor(R.color.namoa_myactions_blue_bg)
                        )
                    }
                }
                //
                tvFormTabName.text = item.name
                tvFormTabOrder.text = getTabOrder(item)
                tvFormTabFieldsCount.text =
                    if (item.countInteract != null) "${item.countInteract}/${item.fieldCount}" else item.fieldCount.toString()
                //
                tvFormTabTracking.visibility = View.GONE
                item.tracking?.let {
                    tvFormTabTracking.visibility = View.VISIBLE
                    tvFormTabTracking.text = it
                }
                //
                cardForecastCount.visibility = View.GONE
                item.forecastCount?.let {
                    cardForecastCount.visibility = View.VISIBLE
                    tvForecastCountVal.text = item.forecastCount.formattedCounter()
                }
                //
                cardCriticalForecastCount.visibility = View.GONE
                item.criticalForecastCount?.let {
                    cardCriticalForecastCount.visibility = View.VISIBLE
                    tvCriticalForecastCountVal.text = item.criticalForecastCount.formattedCounter()
                }
                //
                cardProblemReportedCount.visibility = View.GONE
                item.problemReportedCount?.let {
                    cardProblemReportedCount.visibility = View.VISIBLE
                    tvProblemReportedCountVal.text = item.problemReportedCount.formattedCounter()
                }
                //
                cardNonForecastCount.visibility = View.GONE
                item.nonForecastCount?.let {
                    cardNonForecastCount.visibility = View.VISIBLE
                    tvNonForecastCountVal.text = item.nonForecastCount.formattedCounter()
                }
                //
                cardRequiredByTicketCount.visibility = View.GONE
                item.requiredByTicketCount?.let { count ->
                    if(count > 0) {
                        cardRequiredByTicketCount.visibility = View.VISIBLE
                        tvRequiredByTicketCountVal.text = String.format(
                            Locale.ROOT,
                            "%s %d",
                            requiredByTicketTranslate,
                            count
                        )
                    }
                }
            }
        }

        /**
         * Fun que ajusta o indice caso seja um form O.S
         * Nesses casos, o indice exibido é page + 1
         */
        private fun getTabOrder(item: Act011FormTab) =
            "${if (!isFormSO) item.page else item.page + 1}."
    }

}