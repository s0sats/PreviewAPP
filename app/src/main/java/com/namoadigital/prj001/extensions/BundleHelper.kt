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

fun Bundle?.getIntArguments(key: String) = this?.getInt(key, -1) ?: -1
fun Bundle?.getLongArguments(key: String) = this?.getLong(key, -1L) ?: -1L
fun Bundle?.getStringArguments(key: String) = this?.getString(key, "") ?: ""
fun Bundle?.getBooleanArguments(key: String) = this?.getBoolean(key, false) ?: false

