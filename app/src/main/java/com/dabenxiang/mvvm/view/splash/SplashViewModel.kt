package com.dabenxiang.mvvm.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.coroutines.toFlow
import com.dabenxiang.mvvm.PostDetailQuery
import com.dabenxiang.mvvm.model.api.ApiResult
import com.dabenxiang.mvvm.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SplashViewModel : BaseViewModel() {

    private val _postResult = MutableLiveData<ApiResult<PostDetailQuery.Data>>()
    val postResult: LiveData<ApiResult<PostDetailQuery.Data>> = _postResult

    fun getPostById(id: String) {
        viewModelScope.launch {
            val postDetailQuery = PostDetailQuery(id)
            apolloClient.query(postDetailQuery).toFlow()
                .flowOn(Dispatchers.IO)
                .catch { e -> _postResult.postValue(ApiResult.error(e)) }
                .collect { _postResult.value = ApiResult.success(it.data) }
        }
    }
}