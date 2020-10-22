package com.dabenxiang.mvvm.view.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabenxiang.mvvm.model.api.ExceptionResult
import com.dabenxiang.mvvm.widget.utility.HttpUtils.getExceptionDetail
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@OptIn(KoinApiExtension::class)
abstract class BaseViewModel : ViewModel(), KoinComponent {

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