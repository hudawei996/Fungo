package com.leibo.baselib.net.retrofit

import com.leibo.baselib.net.constant.CommonData
import com.leibo.baselib.net.utils.Logger
import android.annotation.SuppressLint
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des
 */

class LoggingInterceptor : Interceptor {
    @SuppressLint("DefaultLocale")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()


        val url = original.url()
        Logger.i("LoggingInterceptor--->" + url.host())
        val host = url.host()
        //添加参数
        if (!TextUtils.isEmpty(CommonData.DNS_DYNAMICS_URL) && !CommonData.IGNORE_INTERCEPTE_URL.contains(host)) {
            val newUrl = url.newBuilder()
                    .scheme(CommonData.DNS_DYNAMICS_URL_SHCEME)
                    .host(CommonData.DNS_DYNAMICS_URL)
                    .build()
            val newRequest = original.newBuilder().url(newUrl).build()

            return chain.proceed(newRequest)

        }

        val t1 = System.nanoTime()
        Logger.i("LoggingInterceptor--->" + String.format("Sending request %s on %s%n%s", original.url(), chain
                .connection(), original.headers()))
        val response = chain.proceed(original)
        val t2 = System.nanoTime()
        Logger.i("LoggingInterceptor--->" + String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6, response.headers()))
        return response
    }
}
