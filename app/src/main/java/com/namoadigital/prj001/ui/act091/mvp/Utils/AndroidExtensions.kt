package com.namoadigital.prj001.ui.act091.mvp.Utils

import android.view.View

fun View.onVisible() = View.VISIBLE.also { visibility = it }

fun View.onHide() = View.GONE.also { this.visibility = it }