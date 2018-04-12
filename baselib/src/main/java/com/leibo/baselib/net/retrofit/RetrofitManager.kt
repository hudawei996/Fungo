package com.leibo.baselib.net.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.leibo.baselib.net.api.FungoApi
import com.leibo.baselib.net.utils.FungoUrls
import com.leibo.baselib.utils.BaseUtils
import com.leibo.baselib.utils.FileUtils
import com.leibo.baselib.utils.GsonUtils
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author Pinger
 * @since 2018/4/12 16:33
 */

class RetrofitManager private constructor() {

    private val netService: FungoApi

    companion object {
        private var mInstance: RetrofitManager? = null
        val instance: RetrofitManager
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = RetrofitManager()
                }
                return mInstance!!
            }
    }

    init {
        val cacheSize = 10 * 1024 * 1024
        val cachePath = FileUtils.getCachePath(BaseUtils.getApp()) + "cache.net"
        val cache = Cache(File(cachePath), cacheSize.toLong())

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(8, TimeUnit.SECONDS)
                .build()

        val netRetrofit = Retrofit.Builder()
                .baseUrl(FungoUrls.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        netService = netRetrofit.create(FungoApi::class.java)

        println("---RetrofitManager---> netService：$netService")
    }

    fun getNetService(): FungoApi {
        return netService
    }
}