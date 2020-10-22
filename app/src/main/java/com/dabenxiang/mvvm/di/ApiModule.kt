package com.dabenxiang.mvvm.di

import com.apollographql.apollo.ApolloClient
import com.dabenxiang.mvvm.API_HOST_URL
import com.dabenxiang.mvvm.BuildConfig
import com.dabenxiang.mvvm.GRAPHQL_API_HOST_URL
import com.dabenxiang.mvvm.model.api.ApiInterceptor
import com.dabenxiang.mvvm.model.api.ApiService
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    single { provideApiInterceptor() }
    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get(), get()) }
    single { provideApiService(get()) }
    single { provideApolloClient(get()) }
}

fun provideApiInterceptor(): ApiInterceptor {
    return ApiInterceptor()
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
    apiInterceptor: ApiInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
//        .addInterceptor(apiInterceptor)
        .addInterceptor(httpLoggingInterceptor)

    if (BuildConfig.DEBUG) {
        builder.addNetworkInterceptor(StethoInterceptor())
    }

    return builder.build()
}

fun provideApiService(okHttpClient: OkHttpClient): ApiService {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(okHttpClient)
        .baseUrl(API_HOST_URL)
        .build()
        .create(ApiService::class.java)
}

fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
    return ApolloClient.builder()
        .serverUrl(GRAPHQL_API_HOST_URL)
        .okHttpClient(okHttpClient)
        .build()
}

