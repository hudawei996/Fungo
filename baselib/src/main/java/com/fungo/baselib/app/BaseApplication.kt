package com.fungo.baselib.app

import android.app.Application
import com.fungo.baselib.utils.BaseUtils
import com.fungo.baselib.utils.LayoutUtils
import com.fungo.baselib.utils.LogUtils
import me.yokeyword.fragmentation.Fragmentation


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
        initFragmentation()
    }

    private fun initFragmentation() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.SHAKE)
                .debug(true)
                /**
                 * 可以获取到[me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning]
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException {
                    // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                    // Bugtags.sendException(e);
                    LogUtils.e(it)
                }
                .install()
    }


    /**
     * 初始化工具类
     */
    private fun initUtils() {
        BaseUtils.init(this)
        LayoutUtils.initLayout(this)
    }

}