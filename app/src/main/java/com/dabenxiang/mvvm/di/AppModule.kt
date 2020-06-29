package com.dabenxiang.mvvm.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.dabenxiang.mvvm.PREFS_NAME
import com.dabenxiang.mvvm.model.pref.Pref
import org.koin.dsl.module

val appModule = module {
    single { provideGson() }
    single { providePref(get()) }
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}

fun providePref(gson: Gson): Pref {
    return Pref(gson, PREFS_NAME, true)
}