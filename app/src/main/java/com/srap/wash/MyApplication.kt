package com.srap.wash

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

lateinit var myApplication: MyApplication

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        myApplication = this
    }
}