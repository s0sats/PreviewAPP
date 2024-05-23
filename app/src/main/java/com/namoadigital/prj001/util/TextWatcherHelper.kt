package com.namoadigital.prj001.util

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TextWatcherHelper(private val listener: TextChangedListener) : TextWatcher {

    private val DELAY: Long = 200
    private var job: Job? = null
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY)
            editable?.let { listener.onTextChanged(it.toString()) } ?: run {
                listener.onTextChanged("")
            }
        }
    }

    interface TextChangedListener {
        fun onTextChanged(text: String)
    }
}