package com.namoadigital.prj001.ui.base

abstract class FactoryRepository<T> {

    abstract fun build(): T

}