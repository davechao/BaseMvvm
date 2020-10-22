package com.dabenxiang.mvvm.di

import com.dabenxiang.mvvm.model.repository.PostRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { providePostRepository() }
}

fun providePostRepository(): PostRepository {
    return PostRepository()
}