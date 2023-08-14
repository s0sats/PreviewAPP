package com.namoadigital.prj001.core.data.domain.preference

interface ModelPreferences<T> {

    fun write(model: T)

    fun read(): T


}