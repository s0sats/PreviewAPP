package com.namoadigital.prj001.ui.base

import com.namoa_digital.namoa_library.util.HMAux

interface BasePresenter<T> {

    val hmAux_Trans: HMAux
        get() = loadTranslation()

    fun setView(view: T)


    fun loadTranslation(): HMAux

    fun getTranslation() = hmAux_Trans
}