package com.dabenxiang.mvvm.view.splash

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
import org.koin.core.component.inject

class SplashViewModel : BaseViewModel() {

    private val postRepository: PostRepository by inject()

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