package com.namoadigital.prj001.ui.base

import com.google.android.material.snackbar.Snackbar
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R

abstract class BaseActivityMvp<PRESENTER, BINDING>(
) : Base_Activity() {

    protected abstract val binding: BINDING
    protected abstract val presenter: PRESENTER

    fun showSnackbar(message: String = "Em manutenção") {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .setTextColor(resources.getColor(R.color.m3_namoa_surface))
            .show()
    }

}