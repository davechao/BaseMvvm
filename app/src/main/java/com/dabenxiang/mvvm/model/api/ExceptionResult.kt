package com.dabenxiang.mvvm.model.api

import com.dabenxiang.mvvm.model.api.vo.error.HttpExceptionItem

sealed class ExceptionResult {

    object RefreshTokenExpired : ExceptionResult()

    data class HttpError(val httpExceptionItem: HttpExceptionItem) : ExceptionResult()

    data class Crash(val throwable: Throwable) : ExceptionResult()
}
