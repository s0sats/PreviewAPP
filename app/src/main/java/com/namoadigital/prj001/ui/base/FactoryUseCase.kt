package com.namoadigital.prj001.ui.base

abstract class FactoryUseCase<out USECASE> {

    abstract fun build(): USECASE

}