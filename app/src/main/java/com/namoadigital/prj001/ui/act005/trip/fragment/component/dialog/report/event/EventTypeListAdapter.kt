package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.SimpleTextBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.model.trip.FSEventType
import java.util.Locale

class EventTypeListAdapter constructor(
    private var source: MutableList<FSEventType>,
    private val hasFormInProcess: Boolean = false,
    private val selectEventType: (item: FSEventType) -> Unit,
    private val updateSizeList: (Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredList: MutableList<FSEventType> = ArrayList()

    init {
        filteredList.addAll(source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventTypeItemHolder(
            SimpleTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EventTypeItemHolder).onBinding(filteredList[position], position == 0)
    }

    inner class EventTypeItemHolder(
        private val binding: SimpleTextBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBinding(item: FSEventType, firstItem: Boolean) {
            with(binding) {

                cardLayout.apply {
                    val isItemEnabled = !(hasFormInProcess && item.isWaitAllowed)

                    isEnabled = isItemEnabled
                    isClickable = isItemEnabled

                    setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            if (isItemEnabled) android.R.color.white
                            else R.color.namoa_color_gray_7
                        )
                    )

                    alpha = if (isItemEnabled) 1f else 0.55f

                    setOnClickListener {
                        selectEventType(item)
                    }
                }


                textView.applyVisibilityIfTextExists(item.eventTypeDesc)
            }
        }
    }

    fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(source)
        } else {
            val lowercaseQuery = query.lowercase(Locale.getDefault())
            source.forEach { user ->
                if (user.eventTypeDesc.lowercase(Locale.getDefault()).contains(lowercaseQuery)) {
                    filteredList.add(user)
                }
            }
        }
        notifyDataSetChanged()
        updateSizeList(filteredList.size)
    }
}
