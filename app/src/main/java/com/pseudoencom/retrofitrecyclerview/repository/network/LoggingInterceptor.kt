package com.yolla.android.mvvm.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.IOException
import java.util.*


class LoggingInterceptor : Interceptor {
    private val TAG: String = "NEWS.REST"

    override fun intercept(chain: Interceptor.Chain): Response {
        val time: Long = System.currentTimeMillis()
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val responseBody: String? = response.body?.string()
        try {
            Log.d(TAG, "-----------------------------START-------------------------------")
            Log.d(
                TAG,
                response.request.method + " " + response.request.url + " " + bodyToString(request)
            )
            Log.d(
                TAG, String.format(
                    Locale.getDefault(),
                    "Response by %dms: code:%s, body=%s",
                    (System.currentTimeMillis() - time), response.code, responseBody
                )
            )
            Log.d(TAG, "------------------------------END-------------------------------")
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString())
        }
        return response.newBuilder()
            .body(responseBody?.toResponseBody(response.body?.contentType())).build()
    }

    private fun bodyToString(request: Request): String {
        try {
            val copy: Request = request.newBuilder().build();
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            return buffer.readUtf8()
        } catch (e: IOException) {
            e.message?.let {
                Log.e(TAG, it)
            }
            return ""
        }
    }
}