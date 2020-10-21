package com.dabenxiang.mvvm.widget.utility

import com.dabenxiang.mvvm.model.api.ApiRepository
import com.dabenxiang.mvvm.model.api.vo.error.ErrorItem
import com.dabenxiang.mvvm.model.api.vo.error.HttpExceptionItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter

object HttpUtils {

    fun getExceptionDetail(t: Throwable): String {
        return when (t) {
            is HttpException -> {
                val data = getHttpExceptionData(t)
                "$data, ${t.localizedMessage}"
            }
            else -> getStackTrace(t)
        }
    }

    fun getHttpExceptionData(httpException: HttpException): HttpExceptionItem {
        val oriResponse = httpException.response()

        val url = oriResponse?.raw()?.request?.url.toString()
        Timber.d("url: $url")

        val errorBody = oriResponse?.errorBody()
        val jsonStr = errorBody?.string()
        val type = object : TypeToken<ErrorItem>() {}.type

        val errorItem: ErrorItem = try {
            Gson().fromJson(jsonStr, type)
        } catch (e: Exception) {
            e.printStackTrace()
            ErrorItem(
                null,
                null,
                null
            )
        }
        Timber.d("errorItem: $errorItem")

        val responseBody = Gson().toJson(
            ErrorItem(
                errorItem.code,
                errorItem.message,
                null
            )
        )
            .toResponseBody(ApiRepository.MEDIA_TYPE_JSON.toMediaTypeOrNull())

        val rawResponse = okhttp3.Response.Builder()
            .code(httpException.code())
            .message(httpException.message())
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url(url).build())
            .build()

        val response = Response.error<ErrorItem>(responseBody, rawResponse)

        val httpExceptionClone = HttpException(response)
        return HttpExceptionItem(
            errorItem,
            httpExceptionClone,
            url
        )
    }

    private fun getStackTrace(t: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}