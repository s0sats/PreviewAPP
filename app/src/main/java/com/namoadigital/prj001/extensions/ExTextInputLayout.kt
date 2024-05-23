package com.namoadigital.prj001.extensions

import android.content.Context
import android.content.res.Resources
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.namoadigital.prj001.R

fun TextInputLayout.setBoxStrokeColorState(
    context: Context,
    colorId: Int,
    theme: Resources.Theme? = null
) {
    ResourcesCompat.getColorStateList(context.resources, colorId, theme)
        ?.let(this::setBoxStrokeColorStateList)
}

fun TextInputLayout.setHintTextColor(
    context: Context,
    colorId: Int,
    theme: Resources.Theme? = null
) {
    ResourcesCompat.getColorStateList(context.resources, colorId, theme)
        ?.let(this::setHintTextColor)
}

fun TextInputLayout.configureToRequiredInput(
    context: Context,
    hintText: String,
    componentText: TextInputEditText
) {

    val hintValue = "$hintText *"
    hint = hintValue

    if(componentText.text.isNullOrEmpty()){
        setBoxStrokeColorState(context, R.color.edit_text_color_required)
        setHintTextColor(context, R.color.edit_text_color_required)
    }else{
        setBoxStrokeColorState(context, R.drawable.edittext_theme_outlined)
        setHintTextColor(context, R.drawable.edittext_theme)
    }

    componentText.addTextChangedListener(
        onTextChanged = { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                setBoxStrokeColorState(context, R.drawable.edittext_theme)
                setHintTextColor(context, R.drawable.edittext_theme)
            }
        }
    )



}