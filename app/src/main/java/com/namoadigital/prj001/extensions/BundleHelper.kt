package com.namoadigital.prj001.extensions

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

inline fun <reified T> Bundle.putApiRequest(apiRequest: T): Bundle {
    val json = GsonBuilder().serializeNulls().create().toJson(apiRequest, T::class.java)
    this.putString(T::class.java.name, json)
    return this
}
inline fun <reified C> Bundle.putApiRequest(apiRequest: List<C>): Bundle {
    val type = object : TypeToken<List<C>>() {}.type
    val json = Gson().toJson(apiRequest, type)
    this.putString(C::class.java.name, json)
    return this
}