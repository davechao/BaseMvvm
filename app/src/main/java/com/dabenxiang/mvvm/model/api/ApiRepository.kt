package com.dabenxiang.mvvm.model.api

import com.dabenxiang.mvvm.model.api.vo.error.TOKEN_NOT_FOUND

class ApiRepository(private val apiService: ApiService) {

    companion object {
        const val X_APP_VERSION = "x-app-version"
        const val MEDIA_TYPE_JSON = "application/json"
        const val BEARER = "Bearer "
        const val AUTHORIZATION = "Authorization"

        fun isRefreshTokenFailed(code: String?): Boolean {
            return code == TOKEN_NOT_FOUND
        }
    }
}