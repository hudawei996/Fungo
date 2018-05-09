package com.fungo.repertory.recycler

import com.fungo.baselib.net.api.FungoRequest
import com.fungo.baselib.net.entity.BaseNewsEntity

/**
 * @author Pinger
 * @since 2018/4/30 22:36
 */
class NetDataStore(private val fungoRequest:  FungoRequest) {

    fun loadNewsList() {
        fungoRequest.getRequest<BaseNewsEntity>("")
    }



}
