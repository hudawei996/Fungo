package com.fungo.sample.app

import com.fungo.baselib.app.BaseApplication

/**
 * @author Pinger
 * @since 3/29/18 9:13 PM
 */
class SampleApplication : BaseApplication() {
    override fun initSDK() {

    }

    override fun isInnerUseModel(): Boolean {
        return true
    }

    override fun getCurrentEnvModel(): Int {
        return 0
    }


}