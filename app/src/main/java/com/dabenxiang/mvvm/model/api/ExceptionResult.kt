package com.dabenxiang.mvvm.model.api

import com.dabenxiang.mvvm.model.api.vo.error.HttpExceptionItem
import com.dabenxiang.mvvm.model.api.vo.exception.ApiException

sealed class ExceptionResult {

    object RefreshTokenExpired : ExceptionResult()

    data class HttpError(val httpExceptionItem: HttpExceptionItem) : ExceptionResult()

    data class Crash(val throwable: Throwable) : ExceptionResult()

    data class ApiError(val apiException: ApiException) : ExceptionResult()
}
