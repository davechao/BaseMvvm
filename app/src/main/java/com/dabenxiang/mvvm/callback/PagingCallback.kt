package com.dabenxiang.mvvm.callback

interface PagingCallback {
    fun onGetTotalCount(count: Int)
    fun onThrowable(throwable: Throwable)
    fun onLoaded()
    fun onLoading()
}