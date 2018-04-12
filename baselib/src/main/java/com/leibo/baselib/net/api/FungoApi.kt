package com.leibo.baselib.net.api

import com.leibo.baselib.net.entity.BaseEntity
import com.leibo.baselib.net.entity.BaseRequestInfo
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     网络请求api
 */
interface FungoApi {
    @POST("{sourceUrl}")
    fun postRequest(@Path("sourceUrl") sourceUrl: String, @Body params: BaseRequestInfo): Observable<BaseEntity>

    @POST
    fun postRequestWithFullUrl(@Url sourceUrl: String, @Body params: BaseRequestInfo): Observable<BaseEntity>

    @POST("{sourceUrl}")
    fun getRequest(@Path("sourceUrl") sourceUrl: String): Observable<BaseEntity>

    @GET
    fun getRequestWithFullUrl(@Url sourceUrl: String): Observable<BaseEntity>
}
