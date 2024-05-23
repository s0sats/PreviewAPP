package com.namoadigital.prj001.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoadigital.prj001.util.ToolBox_Inf

inline fun <reified T> String.fromJsonToList(): List<T>? {
    val type = object : TypeToken<List<T>>() {}.type
    return try {
        Gson().fromJson(this, type)
    } catch (e: Exception) {
        ToolBox_Inf.registerException(this::class.java.name, e)
        null
    }
}

inline fun <reified T> toApiJson(): String {
    GsonBuilder().serializeNulls().create().also {
        return it.toJson(T::class.java, T::class.java)
    }
}