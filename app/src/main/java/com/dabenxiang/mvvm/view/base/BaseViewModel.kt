package com.dabenxiang.mvvm.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabenxiang.mvvm.model.api.ExceptionResult
import com.dabenxiang.mvvm.model.datastore.DataStores
import com.dabenxiang.mvvm.model.pref.Pref
import com.dabenxiang.mvvm.widget.utility.GeneralUtils.getExceptionDetail
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val gson: Gson by inject()
    val pref: Pref by inject()
    val dataStores: DataStores by inject()

    fun processException(exceptionResult: ExceptionResult) {
        when (exceptionResult) {
            is ExceptionResult.Crash -> {
                sendCrashReport(getExceptionDetail(exceptionResult.throwable))
            }
            is ExceptionResult.HttpError -> {
                sendCrashReport(getExceptionDetail(exceptionResult.httpExceptionItem.httpExceptionClone))
            }
            is ExceptionResult.ApiError -> {
                sendCrashReport(getExceptionDetail(exceptionResult.apiException))
            }
        }
    }

    private fun sendCrashReport(data: String) {
        viewModelScope.launch {
            // TODO: send crash report
        }
    }

}