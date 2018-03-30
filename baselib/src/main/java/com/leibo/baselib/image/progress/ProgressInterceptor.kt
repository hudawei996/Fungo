package com.leibo.baselib.image.progress

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Pinger
 * @since 3/30/18 11:27 AM
 *
 * 拦截器
 */
class ProgressInterceptor(private val progressListener: ProgressListener) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val proceed = chain.proceed(chain.request())
        val responseBody = proceed.body()
        val progressResponseBody = if (responseBody != null) {
            // 定制请求体
            ProgressResponseBody(responseBody, progressListener)
        } else {
            responseBody
        }
        return proceed.newBuilder().body(progressResponseBody).build()
    }
}