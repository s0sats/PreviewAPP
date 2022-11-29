package com.namoadigital.prj001.ui.base

abstract class NamoaFactory<out T> {

    abstract fun build(): T

}