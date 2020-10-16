package com.dabenxiang.mvvm.di

import com.apollographql.apollo.ApolloClient
import com.dabenxiang.mvvm.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val apiModule = module {
    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideApolloClient(get()) }
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = when (BuildConfig.DEBUG) {
        true -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
    }
    return httpLoggingInterceptor
}

fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)

    if (BuildConfig.DEBUG) {
        builder.addNetworkInterceptor(StethoInterceptor())
    }

    return builder.build()
}

fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
    return ApolloClient.builder()
        .serverUrl("https://graphqlzero.almansi.me/api")
        .okHttpClient(okHttpClient)
        .build()
}