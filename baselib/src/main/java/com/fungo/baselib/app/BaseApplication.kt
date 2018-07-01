package com.fungo.baselib.app

import android.app.Application
import com.fungo.baselib.utils.BaseUtils
import com.fungo.baselib.utils.LayoutUtils

/**
 * @author Pinger
 * @since 18-3-31 下午5:40
 *
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
        initSDK()
    }

    open fun initSDK() {
    }

    private fun init() {
        initUtils()
    }


    /**
     * 初始化工具类
     */
    private fun initUtils() {
        BaseUtils.init(this)
        LayoutUtils.initLayout(this)
    }

}