package com.namoadigital.prj001.extensions

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Throwable.firebaseRegisterException(){
    if (!this.toString().contains("EXCEPTION_DATABASE_CUSTOMER_-1")) {
        FirebaseCrashlytics.getInstance().recordException(this)
    }
}