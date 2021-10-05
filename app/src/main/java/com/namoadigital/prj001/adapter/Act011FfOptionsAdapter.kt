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

class Act011FfOptionsAdapter(
    val tabs: List<Act011FormTab>,
    var tabSelected:Int,
    val mListener: Act011FfOption.ICustom_Form_FF_Options_ll
) : RecyclerView.Adapter<Act011FfOptionsAdapter.MyFormTabVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFormTabVH {
        return MyFormTabVH(Act011FfOptionsCellBinding.inflate(LayoutInflater.from(parent.context), parent, false), mListener)
    }

    override fun onBindViewHolder(holder: MyFormTabVH, position: Int) {
        val tab = tabs[position]
        holder.onBinding(tab)

    }

    override fun getItemCount(): Int {
        return tabs.size
    }


    inner class MyFormTabVH(val binding: Act011FfOptionsCellBinding, val mListener: Act011FfOption.ICustom_Form_FF_Options_ll) : RecyclerView.ViewHolder(binding.root){
        fun onBinding(item: Act011FormTab){
            with(binding) {
                clTabItem.apply {
                    setOnClickListener {
                        mListener.onTabSelected(item.page)
                    }
                    when(item.status)
                    {
                        Act011FormTabStatus.OK ->{
                            setBackgroundColor(context.getResources().getColor(R.color.namoa_color_light_green4))
                        }
                        //
                        Act011FormTabStatus.ERROR ->{
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
                    if(item.page == tabSelected){
                        setBackgroundColor(
                            context.getResources().getColor(R.color.namoa_myactions_blue_bg)
                        )
                    }
                }

                tvFormTabName.text = item.name
                tvFormTabOrder.text = "${item.page.toString()}."
                tvFormTabFieldsCount.text = item.fieldCount.toString()
                tvFormTabTracking.visibility = View.INVISIBLE
                item.tracking?.let {
                    tvFormTabTracking.visibility = View.VISIBLE
                    tvFormTabTracking.text = it
                }

                tvForecastCountVal.visibility = View.INVISIBLE
                item.forecastCount?.let {
                    tvForecastCountVal.visibility = View.VISIBLE
                    tvForecastCountVal.text = it.toString()
                }

                tvCriticalForecastCountVal.visibility = View.INVISIBLE
                item.criticalForecastCount?.let {
                    tvCriticalForecastCountVal.visibility = View.VISIBLE
                    tvCriticalForecastCountVal.text = it.toString()
                }

                tvProblemReportedCountVal.visibility = View.INVISIBLE
                item.problemReportedCount?.let {
                    tvProblemReportedCountVal.visibility = View.VISIBLE
                    tvProblemReportedCountVal.text = it.toString()
                }
            }
        }
    }

}