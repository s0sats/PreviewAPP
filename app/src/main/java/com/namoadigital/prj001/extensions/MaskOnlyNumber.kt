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

            it.forEachIndexed { index, c ->
                if(c.toString() == "-") {
                    editText.text.delete(index, index + 1)
                }
            }

            OnEventAfterTextChanged?.let { invoke -> invoke(it) }
        }
    }
}