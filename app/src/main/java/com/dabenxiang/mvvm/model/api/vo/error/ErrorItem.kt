package com.dabenxiang.mvvm.model.api.vo.error

import com.google.gson.annotations.SerializedName

data class ErrorItem(

    @SerializedName("code")
    var code: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("details")
    var details: String? = null
)
