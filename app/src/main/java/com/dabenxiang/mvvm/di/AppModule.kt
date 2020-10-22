package com.dabenxiang.mvvm.di

import android.content.Context
import com.dabenxiang.mvvm.model.datastore.DataStores
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context, gson: Gson): DataStores {
        return DataStores(context, gson)
    }
}
