package com.dabenxiang.mvvm.extension

import com.dabenxiang.mvvm.model.api.ApiRepository
import com.dabenxiang.mvvm.model.api.ExceptionResult
import com.dabenxiang.mvvm.model.api.vo.exception.ApiException
import com.dabenxiang.mvvm.widget.utility.GeneralUtils.getHttpExceptionData
import retrofit2.HttpException
import timber.log.Timber

fun Throwable.handleException(processException: (ExceptionResult) -> Unit): ExceptionResult {
    val result = when (this) {
        is HttpException -> {
            val httpExceptionItem = getHttpExceptionData(this)
            val result = ApiRepository.isRefreshTokenFailed(
                httpExceptionItem.errorItem.code.toString()
            )
            Timber.d("isRefreshTokenFailed: $result")
            if (result) {
                ExceptionResult.RefreshTokenExpired
            } else {
                ExceptionResult.HttpError(httpExceptionItem)
            }
        }
        is ApiException -> {
            ExceptionResult.ApiError(this)
        }
        else -> {
            ExceptionResult.Crash(this)
        }
    }

    processException(result)

    return result
}
