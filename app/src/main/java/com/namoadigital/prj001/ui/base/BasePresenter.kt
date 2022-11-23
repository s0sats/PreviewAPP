package com.namoadigital.prj001.ui.base

import com.namoa_digital.namoa_library.util.HMAux

interface BasePresenter<T> {

    fun setView(view: T)

    fun loadTranslation(): HMAux

    fun getTranslation() = loadTranslation()
}