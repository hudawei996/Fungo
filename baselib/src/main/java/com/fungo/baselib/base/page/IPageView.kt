package com.fungo.baselib.base.page

/**
 * @author Pinger
 * @since 2018/4/1 15:12
 *
 * 页面展示的一些接口
 *
 */
interface IPageView : IView {

    fun showPageLoading()


    fun showPageEmpty()


    fun showPageError()
}