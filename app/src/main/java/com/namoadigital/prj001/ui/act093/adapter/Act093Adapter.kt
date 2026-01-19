package com.namoadigital.prj001.ui.act093.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act020HeaderListBinding
import com.namoadigital.prj001.databinding.Act093AdapterItemBinding
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem.Companion.ITEM_CHECK_STATUS_MANUAL_ALERT
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel

class Act093Adapter constructor(
    private val source: List<DeviceTpModel>,
    private val hmAux: HMAux,
    private val onItemSelected: (position: Int, item: DeviceTpModel) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val defaultList = source.separateListByHeader().toMutableList()

    private fun List<DeviceTpModel>.separateListByHeader(): List<DeviceViewItem> {
        val listChangeReached = mutableListOf<DeviceViewItem>()
        val listWithProblem = mutableListOf<DeviceViewItem>()
        val mergeList = mutableListOf<MutableList<DeviceViewItem>>()

        forEach { item ->
            if (item.item_check_status == ITEM_CHECK_STATUS_MANUAL_ALERT) {
                if (listWithProblem.contains(DeviceViewItem.SectionItem(R.color.namoa_os_form_problem_red))) {
                    listWithProblem.add(
                        DeviceViewItem.ContentItem(
                            item,
                            R.color.namoa_os_form_problem_red
                        )
                    )
                } else {
                    listWithProblem.add(DeviceViewItem.SectionItem(R.color.namoa_os_form_problem_red))
                    listWithProblem.add(
                        DeviceViewItem.ContentItem(
                            item,
                            R.color.namoa_os_form_problem_red
                        )
                    )
                }
            } else if (
                item.critical_item == 1
                && item.color_item == GeOsDeviceItemStatusColor.YELLOW
                ) {
                if (listChangeReached.contains(DeviceViewItem.SectionItem(R.color.namoa_os_form_critical_forecast_yellow))) {
                    listChangeReached.add(
                        DeviceViewItem.ContentItem(
                            item,
                            R.color.namoa_os_form_critical_forecast_yellow
                        )
                    )
                } else {
                    listChangeReached.add(DeviceViewItem.SectionItem(R.color.namoa_os_form_critical_forecast_yellow))
                    listChangeReached.add(
                        DeviceViewItem.ContentItem(
                            item,
                            R.color.namoa_os_form_critical_forecast_yellow
                        )
                    )
                }
            }
        }

        mergeList.add(listWithProblem)
        mergeList.add(listChangeReached)

        val newList = mutableListOf<DeviceViewItem>()
        mergeList.forEach { listDevice ->
            listDevice.forEach { device ->
                newList.add(device)
            }
        }
        return if (newList.size == 0) emptyList<DeviceViewItem>().toMutableList() else newList

    }


    override fun getItemViewType(position: Int): Int {
        if (defaultList[position] is DeviceViewItem.SectionItem) {
            return DeviceViewItem.VIEW_TYPE_SECTION
        }
        return DeviceViewItem.VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == DeviceViewItem.VIEW_TYPE_SECTION) {
            return DoneItemHolder(
                Act020HeaderListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            Act093AdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = defaultList[position]
        if (holder is DoneItemHolder && item is DeviceViewItem.SectionItem) {
            var header: String? = null
            if (item.color == R.color.namoa_os_form_problem_red) {
                header = hmAux["item_with_problem_lbl"]
            } else {
                header = hmAux["item_with_change_expired_lbl"]
            }
            holder.onBinding(header)
        }
        if (holder is ViewHolder && item is DeviceViewItem.ContentItem) {
            holder.onBinding(item.item, item.color)
            holder.itemView.setOnClickListener {
                onItemSelected(position, item.item)
            }
        }
    }


    inner class DoneItemHolder constructor(
        private val binding: Act020HeaderListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(header: String?) {
            with(binding) {
                headerText.text = header
                lineSeparator.visibility = View.GONE
            }
        }
    }

    inner class ViewHolder constructor(
        private val binding: Act093AdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBinding(item: DeviceTpModel, color: Int) {
            with(binding) {

                ballColor.apply {
                    setColorFilter(resources.getColor(color))
                }
                ivItemDetail.visibility = View.VISIBLE
                itemOverlined.text = item.device_tp_desc
                itemTitle.text = item.item_check_desc
                itemSupport.apply {
                    visibility =
                        if (!item.materialListFormatted.isNullOrEmpty()) View.VISIBLE else View.GONE
                    text =
                        if (!item.materialListFormatted.isNullOrEmpty()) item.materialListFormatted else ""
                }

            }
        }

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return defaultList.size
    }

}