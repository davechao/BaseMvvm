package com.dabenxiang.mvvm.di

import android.content.Context
import com.dabenxiang.mvvm.model.datastore.DataStores
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val appModule = module {
    single { provideGson() }
    single { provideDataStore(get(), get()) }
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}

fun provideDataStore(context: Context, gson: Gson): DataStores {
    return DataStores(context, gson)
}