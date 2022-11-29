package com.namoadigital.prj001.core.data.local.preferences

interface ModelPreferences<T> {

    fun write(model: T)

    fun read(): T


}