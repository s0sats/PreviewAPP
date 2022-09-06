package com.namoadigital.prj001.extensions

import android.widget.EditText
import com.namoa_digital.namoa_library.ctls.MKEditTextNMN

class MaskOnlyNumber(
    private val editText: EditText,
    private val OnEventAfterTextChanged: ((String) -> Unit?)? = null
) : MKEditTextNMN.IMKEditTextChangeText {


/*    override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {

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

    }*/

    override fun reportTextChange(char: String?) {
        char?.let {
            if(it.contains("-")){
                editText.text.delete(it.length - 1, it.length)
            }
            OnEventAfterTextChanged?.let { invoke -> invoke(it) }
        }
    }
}


private val blackList = listOf("-", ",")