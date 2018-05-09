package com.fungo.baselib.net.repository

import com.fungo.baselib.net.api.FungoRequest
import com.fungo.baselib.net.entity.BaseEntity
import com.fungo.baselib.net.entity.BaseNewsEntity
import com.fungo.baselib.net.retrofit.RetrofitManager

/**
 * @author Pinger
 * @since 2018/4/12 17:07
 */
class Repository {

    @Volatile
    private var mRepository: Repository? = null

    companion object {
        private var mInstance: Repository? = null
        val instance: Repository
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = Repository()
                }
                return mInstance!!
            }
    }

    private var mFungoRequest: FungoRequest<BaseEntity>? = null
    fun getFungoRequest(): FungoRequest<BaseEntity> {
        if (mFungoRequest == null) {
            mFungoRequest = FungoRequest(RetrofitManager.instance.getFungoApi())
        }
        return mFungoRequest!!
    }



    fun getNetDateStore(){
        val netDateStore = NetDataStore(getFungoRequest() )
    }








}