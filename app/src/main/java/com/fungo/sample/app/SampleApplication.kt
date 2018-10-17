package com.fungo.sample.app

import com.fungo.baselib.app.BaseApplication
import com.fungo.netgo.NetGo
import com.fungo.sample.BuildConfig


/**
 * @author Pinger
 * @since 3/29/18 9:13 PM
 */
class SampleApplication : BaseApplication() {

    override fun isCanSwitchEnv(): Boolean {
        return BuildConfig.CAN_SWITCH_ENV
    }

    override fun isCanPrintLog(): Boolean {
        return BuildConfig.CAN_PRINT_LOG
    }

    override fun getCurrentEnvModel(): Int {
        return BuildConfig.CURRENT_DEV_ENV
    }

    override fun initSDK() {
        initNetGo()
    }

    private fun initNetGo() {
        NetGo.getInstance().init(this).debug(true)
    }


}