package com.fungo.baseuilib.fragment

/**
 * @author Pinger
 * @since 18-7-24 下午5:21
 *
 * 没有导航栏的Fragment页面，专门用来填充页面
 */
abstract class BaseContentFragment : BaseNavFragment() {

    override fun isShowToolBar(): Boolean = false
}