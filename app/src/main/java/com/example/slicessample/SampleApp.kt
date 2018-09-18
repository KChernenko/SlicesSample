package com.example.slicessample

import android.app.Application
import timber.log.Timber

class SampleApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}