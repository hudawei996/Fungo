package com.fungo.baseuilib.fragment

/**
 * @author Pinger
 * @since 2018/11/3 22:16
 * 支持滑动返回的Fragment
 */
abstract class BaseSwipeBackFragment : BaseNavFragment() {

    /**
     * 支持滑动返回
     */
    final override fun isSwipeBackEnable(): Boolean = true

    /**
     * 是否展示标题栏
     */
    override fun isShowToolBar(): Boolean = true

    /**
     * 是否展示返回按钮
     */
    override fun isShowBackIcon(): Boolean = false

    override fun getNavContentResID(): Int {
        return getContentResID()
    }

    abstract fun getContentResID(): Int
}