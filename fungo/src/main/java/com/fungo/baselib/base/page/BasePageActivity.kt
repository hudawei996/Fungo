package com.fungo.baselib.base.page

import android.os.Bundle
import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseActivity


/**
 * @author Pinger
 * @since 18-7-20 下午5:34
 * 页面的中转Activity的基类
 */

abstract class BasePageActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.base_activity_page


    final override fun initView() {
        // 设置Fragment的默认背景颜色
        setDefaultFragmentBackground(R.color.grey_f7)
        val fragment = getPageFragment()
        // 转移Activity的extras给Fragment
        if (fragment.arguments == null && intent.extras != null) {
            fragment.arguments = intent.extras
        } else if (fragment.arguments != null && intent.extras != null) {
            fragment.arguments!!.putAll(intent.extras)
        }
        // 填充Fragment
        loadRootFragment(R.id.pageContainer, fragment)
    }

    /**
     * 给子类初始化页面View
     */
    protected open fun initPageView() {

    }

    /**
     * 获取跟节点的Fragment
     */
    abstract fun getPageFragment(): BasePageFragment

}