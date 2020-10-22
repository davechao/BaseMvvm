package com.dabenxiang.mvvm.di

import com.apollographql.apollo.ApolloClient
import com.dabenxiang.mvvm.model.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun providePostRepository(apolloClient: ApolloClient): PostRepository {
        return PostRepository(apolloClient)
    }
}