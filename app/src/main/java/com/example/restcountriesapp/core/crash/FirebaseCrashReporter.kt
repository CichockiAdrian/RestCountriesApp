package com.example.restcountriesapp.core.crash

import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashReporter : CrashReporter {

    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun recordException(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}