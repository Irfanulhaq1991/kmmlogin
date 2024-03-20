package org.irfan.project

import android.app.Application
import initKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}