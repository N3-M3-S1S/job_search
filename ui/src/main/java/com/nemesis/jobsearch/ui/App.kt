package com.nemesis.jobsearch.ui

import android.app.Application
import com.nemesis.jobsearch.data.DataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(DataModule, UiModule)
        }
    }

}