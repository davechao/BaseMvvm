package com.dabenxiang.mvvm.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.dabenxiang.mvvm.model.api.ApiRepository
import com.dabenxiang.mvvm.model.api.ExceptionResult
import com.dabenxiang.mvvm.model.datastore.DataStores
import com.dabenxiang.mvvm.widget.utility.HttpUtils.getExceptionDetail
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val gson: Gson by inject()
    val dataStores: DataStores by inject()
    val apiRepository: ApiRepository by inject()
    val apolloClient: ApolloClient by inject()

    fun processException(exceptionResult: ExceptionResult) {
        when (exceptionResult) {
            is ExceptionResult.Crash -> {
                sendCrashReport(getExceptionDetail(exceptionResult.throwable))
            }
            is ExceptionResult.HttpError -> {
                sendCrashReport(getExceptionDetail(exceptionResult.httpExceptionItem.httpExceptionClone))
            }
            else -> {
            }
        }
    }

    private fun sendCrashReport(data: String) {
        viewModelScope.launch {
            // TODO: send crash report
        }
    }

}