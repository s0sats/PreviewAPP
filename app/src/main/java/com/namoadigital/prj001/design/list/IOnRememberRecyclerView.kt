package com.namoadigital.prj001.design.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IOnRememberRecyclerView<T> constructor(
    private var recyclerView: RecyclerView,
    private var textView: TextView,
) : OnRememberListState<T> {

    override fun dataChanged(list: ArrayList<T>?) {
        recyclerView.visibility = if (list?.isEmpty() == true) View.GONE else View.VISIBLE
        textView.visibility = if (list?.isEmpty() == true) View.VISIBLE else View.GONE
    }
}