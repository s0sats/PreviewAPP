package com.namoadigital.prj001.view.act.product_selection

import android.text.SpannableString
import com.namoa_digital.namoa_library.util.HMAux

data class ActProductSelectionListItem(
    val source: HMAux,
    val productDescHighlight: SpannableString?
)