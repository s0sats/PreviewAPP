package com.namoadigital.prj001.design.list

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IOnRememberListState<T> constructor(
    private val recyclerView: RecyclerView,
    private val textView: TextView
) : OnRememberListState<T> {


    companion object {
        const val TAG = "OnRemembe"
    }

    override fun dataChanged(list: ArrayList<T>?) {
        Log.d(TAG, "Tamanaho da lista: ${list?.size}")
        Log.d(TAG, "Está vazia?: ${list?.isEmpty()}")

        recyclerView.visibility = if (list?.isEmpty() == true) View.GONE else View.VISIBLE
        Log.d(TAG, "recyclerView: ${recyclerView.visibility}")

        textView.visibility = if (list?.isEmpty() == true) View.VISIBLE else View.GONE
        Log.d(TAG, "textView: ${textView.visibility}")
    }
}