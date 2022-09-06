package com.namoadigital.prj001.extensions

import android.widget.EditText
import com.namoa_digital.namoa_library.ctls.MKEditTextNM

class MaskOnlyNumber(
    private val editText: EditText,
    private val OnEventAfterTextChanged: ((String) -> Unit?)? = null
) : MKEditTextNM.IMKEditTextChangeText {

    override fun reportTextChange(char: String?) {

    }

    override fun reportTextChange(char: String?, p1: Boolean) {
        char?.let {
            if(it.contains("-")){
                editText.text.delete(it.length - 1, it.length)
                return
            }

            OnEventAfterTextChanged?.let { invoke -> invoke(it) }
        }
    }
}