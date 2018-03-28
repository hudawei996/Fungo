package com.leibo.basenet.api

import io.reactivex.Observable
import org.fungo.basenetlib.config.BaseEntity
import org.fungo.basenetlib.config.BaseRequestInfo
import retrofit2.http.*

/**
 * @author Pinger
 * @since 3/27/18 7:44 PM
 * 网络请求Api
 */
interface NetApi {

    /**
     * Get请求
     */
    @GET("{url}")
    fun getRequest(@Path("url") url: String): Observable<BaseEntity>

    /**
     * Get全路径请求
     */
    @GET
    fun getRequestWithFullUrl(@Url url: String): Observable<BaseEntity>

    /**
     * Post请求
     */
    @POST("{url}")
    fun postRequest(@Path("url") sourceUrl: String, @Body params: BaseRequestInfo): Observable<BaseEntity>

    /**
     * Post全路径请求
     */
    @POST
    fun postRequestWithFullUrl(@Url url: String, @Body params: BaseRequestInfo): Observable<BaseEntity>
}
