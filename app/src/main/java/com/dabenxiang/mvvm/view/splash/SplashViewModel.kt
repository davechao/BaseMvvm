package com.dabenxiang.mvvm.view.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dabenxiang.mvvm.PostDetailQuery
import com.dabenxiang.mvvm.model.api.ApiResult
import com.dabenxiang.mvvm.model.repository.PostRepository
import com.dabenxiang.mvvm.view.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashViewModel @ViewModelInject constructor(
    private var postRepository: PostRepository
) : BaseViewModel() {

    private val _postResult = MutableLiveData<ApiResult<PostDetailQuery.Data>>()
    val postResult: LiveData<ApiResult<PostDetailQuery.Data>> = _postResult

    fun getPostById(id: String) {
        viewModelScope.launch {
            postRepository.getPostById(id)
                .catch { e -> _postResult.postValue(ApiResult.error(e)) }
                .collect { _postResult.value = ApiResult.success(it.data) }
        }
    }
}