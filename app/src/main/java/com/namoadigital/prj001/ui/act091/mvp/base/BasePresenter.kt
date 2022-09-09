package com.namoadigital.prj001.ui.act091.mvp.base

import com.namoa_digital.namoa_library.util.HMAux

interface BasePresenter<T, EVENT> {

    @Suppress("MemberVisibilityCanBePrivate")
    private val hmAux_Trans: HMAux
        get() = loadTranslation()

    fun setView(view: T)

    fun onEvent(event: EVENT)

    @Suppress("MemberVisibilityCanBePrivate")
    fun loadTranslation(): HMAux

    fun getTranslation() = hmAux_Trans
}