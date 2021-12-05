package com.mangpo.bookclub.view

import android.app.Application
import android.app.usage.UsageEvents.Event.NONE
import android.bluetooth.BluetoothAdapter.ERROR
import com.mangpo.bookclub.BuildConfig
import com.mangpo.bookclub.viewmodel.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import java.util.logging.Level

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(if (BuildConfig.DEBUG) org.koin.core.logger.Level.ERROR else org.koin.core.logger.Level.NONE)
            androidContext(applicationContext)
            modules(ViewModelModule)
        }
    }
}