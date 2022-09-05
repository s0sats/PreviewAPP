package com.namoadigital.prj001.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class MaskOnlyNumber(
    private val editText: EditText,
    private val OnEventCurrentTextChanged: ((String) -> Unit?)? = null,
    private val OnEventAfterTextChanged: ((String) -> Unit?)? = null
) : TextWatcher {


    override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
        char?.toString()?.let {
            if(it.contains("-")){
                editText.text.delete(it.length - 1, it.length)
            }
            OnEventCurrentTextChanged?.let { invoke -> invoke(it) }
        }
    }

    override fun afterTextChanged(char: Editable?) {

        char?.toString()?.let {
            OnEventAfterTextChanged?.let { invoke -> invoke(it) }
        }

    }
}


private val blackList = listOf("-", ",")