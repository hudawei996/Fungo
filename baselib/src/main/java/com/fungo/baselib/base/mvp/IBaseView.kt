package com.fungo.baselib.base.mvp

import android.content.Context
import com.fungo.baselib.base.page.IPageView

/**
 * @author Pinger
 * @since 18-7-25 下午5:06
 * MVP的基类View
 */

interface IBaseView : IPageView {


    /**
     * View是否激活
     */
    fun isActive(): Boolean


    /**
     * 获取当前页面的Context
     */
    fun getContext(): Context?
}