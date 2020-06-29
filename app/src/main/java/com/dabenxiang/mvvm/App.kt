package com.dabenxiang.mvvm

import android.app.Application
import com.dabenxiang.mvvm.di.appModule
import com.dabenxiang.mvvm.di.apiModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var self: Application
        fun applicationContext(): Application {
            return self
        }
    }

    init {
        self = this
    }

    override fun onCreate() {
        super.onCreate()

       if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }

        val module = listOf(
                appModule,
                apiModule
        )

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(module)
        }
    }
}