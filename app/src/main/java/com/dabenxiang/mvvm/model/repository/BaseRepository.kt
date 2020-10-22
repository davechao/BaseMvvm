package com.dabenxiang.mvvm.model.repository

import com.apollographql.apollo.ApolloClient
import com.dabenxiang.mvvm.model.datastore.DataStores
import com.google.gson.Gson
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
abstract class BaseRepository : KoinComponent {

    val gson: Gson by inject()
    val dataStores: DataStores by inject()
    val apolloClient: ApolloClient by inject()
}