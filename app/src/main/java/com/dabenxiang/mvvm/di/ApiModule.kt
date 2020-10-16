package com.dabenxiang.mvvm.di

import com.dabenxiang.mvvm.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val apiModule = module {
    single { provideHttpLoggingInterceptor() }
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = when (BuildConfig.DEBUG) {
        true -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
    }
    return httpLoggingInterceptor
}
