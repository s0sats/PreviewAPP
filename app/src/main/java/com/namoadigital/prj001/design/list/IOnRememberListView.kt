package com.namoadigital.prj001.design.list

import android.view.View
import android.widget.ListView
import android.widget.TextView

class IOnRememberListView<T> constructor(
    private var listView: ListView,
    private var textView: TextView,
) : OnRememberListState<T> {

    override fun dataChanged(list: ArrayList<T>?) {
        listView.visibility = if (list?.isEmpty() == true) View.GONE else View.VISIBLE
        textView.visibility = if (list?.isEmpty() == true) View.VISIBLE else View.GONE
    }
}