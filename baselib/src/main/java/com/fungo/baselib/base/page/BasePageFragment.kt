package com.fungo.baselib.base.page

import com.fungo.baselib.base.basic.BaseFragment

/**
 * @author Pinger
 * @since 18-7-24 下午5:21
 *
 */

abstract class BasePageFragment : BaseFragment() {


    /**
     * 获取资源ID
     */
    abstract fun getLayoutResId(): Int


    /**
     * 是否支持侧滑返回
     */
    protected open fun isSwipeBack() = true


}