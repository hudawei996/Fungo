package com.fungo.baselib.net.repository

import com.fungo.baselib.net.api.FungoRequest
import com.fungo.baselib.net.entity.BaseEntity
import com.fungo.baselib.net.entity.BaseNewsEntity

/**
 * @author Pinger
 * @since 2018/4/30 22:36
 */
class NetDataStore(private val fungoRequest:  FungoRequest<BaseEntity>) {

    fun loadNewsList() {
        fungoRequest.getRequest<TestData>("")
    }



}
