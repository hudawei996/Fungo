package com.leibo.baselib.net.dependency

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.leibo.baselib.net.api.FungoApi
import com.leibo.baselib.net.retrofit.FungoRequest
import com.leibo.baselib.net.retrofit.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des
 */
@Module
class NetModule(private val context: Context, private val baseUrl: String) {

    @Provides
    @Singleton
    internal fun provideFungoRequest(fungoApi: FungoApi): FungoRequest {
        return FungoRequest(context, fungoApi)
    }

    @Provides
    @Singleton
    internal fun provideFungoSevice(retrofit: Retrofit): FungoApi {
        return retrofit.create(FungoApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(cache: Cache, interceptor: LoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .cache(cache)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideLogginInterceptor(): LoggingInterceptor {
        return LoggingInterceptor()
    }
}
