package com.dabenxiang.mvvm.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabenxiang.mvvm.model.api.ExceptionResult
import com.dabenxiang.mvvm.widget.utility.HttpUtils.getExceptionDetail
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

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