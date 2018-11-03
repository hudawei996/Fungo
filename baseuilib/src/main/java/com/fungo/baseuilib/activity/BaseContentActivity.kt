package com.fungo.baseuilib.activity

import com.fungo.baseuilib.fragment.BaseFragment


/**
 * @author Pinger
 * @since 18-7-20 下午5:34
 *
 * 专用用来填充Fragment的基类,没有标题导航栏
 * 如果需要标题导航栏的Activity，请使用[BaseNavActivity]
 */
abstract class BaseContentActivity : BaseNavActivity() {

    override fun getNavFragment(): BaseFragment {
        return getContentFragment()
    }

    /**
     * 获取跟节点的Fragment
     */
    abstract fun getContentFragment(): BaseFragment

    /**
     * 专用用来填充Fragment，禁用标题栏
     */
    override fun isShowToolBar(): Boolean = false

    final override fun getPageTitle(): String? {
        return super.getPageTitle()
    }

    final override fun setPageTitle(title: String?) {
        super.setPageTitle(title)
    }
}