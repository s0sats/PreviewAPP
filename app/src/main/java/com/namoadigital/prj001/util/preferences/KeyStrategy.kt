package com.namoadigital.prj001.util.preferences

interface KeyStrategy <T> {
    fun getKey(item: T): String
}