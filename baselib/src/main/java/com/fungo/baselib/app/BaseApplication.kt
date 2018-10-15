package com.fungo.baselib.app

import android.app.Application
import com.fungo.baselib.utils.AppUtils
import com.fungo.baselib.utils.DebugUtils
import com.fungo.baselib.utils.LayoutUtils
import com.fungo.baselib.utils.LogUtils
import me.yokeyword.fragmentation.Fragmentation


/**
 * @author Pinger
 * @since 18-3-31 下午5:40
 *
 */
abstract class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        init()
        initSDK()
    }


    private fun init() {
        initUtils()
        initEnv()
        initFragmentation()
    }

    /**
     * 初始化环境
     */
    private fun initEnv() {
        DebugUtils.setInnerUseModel(isInnerUseModel())
        DebugUtils.setCurrentEnvModel(getCurrentEnvModel())
    }

    private fun initFragmentation() {
        val stackViewMode = if (DebugUtils.isDevModel())
            Fragmentation.SHAKE
        else Fragmentation.NONE

        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(stackViewMode)
                .debug(DebugUtils.isDebugModel())
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
        AppUtils.init(this)
        LayoutUtils.initLayout(this)
    }


    /**
     * 给子类初始化第三方的SDK
     */
    abstract fun initSDK()

    /**
     * 当前的环境是不是只提示给内部使用，由具体的application重写
     */
    abstract fun isInnerUseModel(): Boolean

    /**
     * 获取当前的开发环境模式
     */
    abstract fun getCurrentEnvModel(): Int
}