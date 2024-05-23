package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.SimpleTextBinding
import com.namoadigital.prj001.extensions.applyVisibilityIfTextExists
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import java.util.Locale

class UsersListAdapter constructor(
    private var source: MutableList<TripUserEdit>,
    private val selectUser: (item: TripUserEdit) -> Unit,
    private val updateSizeList: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredList: MutableList<TripUserEdit> = ArrayList()

    init {
        filteredList.addAll(source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TechnicalItemHolder(
            SimpleTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TechnicalItemHolder).onBinding(filteredList[position])
    }

    inner class TechnicalItemHolder(
        private val binding: SimpleTextBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBinding(item: TripUserEdit) {
            with(binding) {
                textView.apply {
                    applyVisibilityIfTextExists(item.userName)
                    setOnClickListener {
                        selectUser(item)
                    }
                }
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
                if (user.userName.lowercase(Locale.getDefault()).contains(lowercaseQuery)) {
                    filteredList.add(user)
                }
            }
        }
        notifyDataSetChanged()
        updateSizeList(filteredList.size)
    }
}
