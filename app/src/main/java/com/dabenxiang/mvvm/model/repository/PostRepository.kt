package com.dabenxiang.mvvm.model.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.dabenxiang.mvvm.PostDetailQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class PostRepository constructor(private val apolloClient: ApolloClient) {

    fun getPostById(id: String): Flow<Response<PostDetailQuery.Data>> {
        val postDetailQuery = PostDetailQuery(id)
        return apolloClient.query(postDetailQuery).toFlow()
            .flowOn(Dispatchers.IO)
    }
}