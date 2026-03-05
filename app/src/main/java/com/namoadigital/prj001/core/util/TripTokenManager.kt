package com.namoadigital.prj001.core.util

import android.content.Context

class TripTokenManager() {

    fun <T>  create(context: Context): TokenManager<T> {
        return TokenManager(
            context = context,
            fileName = "trip_request_token.json"
        )
    }

}