package com.mangpo.bookclub.view

import android.app.Application
import com.mangpo.bookclub.BuildConfig
import com.mangpo.bookclub.viewmodel.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) org.koin.core.logger.Level.ERROR else org.koin.core.logger.Level.NONE)
            androidContext(applicationContext)
            modules(ViewModelModule)
        }
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}