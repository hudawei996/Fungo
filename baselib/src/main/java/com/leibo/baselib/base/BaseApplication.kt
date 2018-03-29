package com.leibo.baselib.base

import android.app.Application
import com.leibo.baselib.utils.BaseUtils

/**
 * @author Pinger
 * @since 3/26/18 8:58 PM
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseUtils.init(this)
    }

}