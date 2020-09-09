package com.dabenxiang.mvvm.di

import android.content.Context
import com.dabenxiang.mvvm.PREFS_NAME
import com.dabenxiang.mvvm.model.datastore.DataStores
import com.dabenxiang.mvvm.model.pref.Pref
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val appModule = module {
    single { provideGson() }
    single { providePref(get()) }
    single { provideDataStore(get(), get()) }
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}

fun providePref(gson: Gson): Pref {
    return Pref(gson, PREFS_NAME, true)
}

fun provideDataStore(context: Context, gson: Gson): DataStores {
    return DataStores(context, gson)
}