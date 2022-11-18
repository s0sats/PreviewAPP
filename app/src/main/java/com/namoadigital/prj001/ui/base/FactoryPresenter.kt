package com.namoadigital.prj001.ui.base

abstract class FactoryPresenter<PRESENTER> {

    abstract fun build(): PRESENTER

}