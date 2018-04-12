package com.leibo.baselib.net.repository

import com.leibo.baselib.net.api.FungoApi

/**
 * @author Pinger
 * @since 2018/4/12 17:07
 */
class Repository {

    @Volatile
    private var repository: Repository? = null
    @Volatile
    private var netService: FungoApi? = null

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


    private var netWorkDataStore: NetWorkDataStore? = null

    private fun getNetDataStore(): NetWorkDataStore? {
        if (netWorkDataStore == null) {
            netWorkDataStore = NetWorkDataStore(netService!!)
        }
        return netWorkDataStore
    }

}