package com.fungo.baselib.base.page

import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseActivity


/**
 * @author Pinger
 * @since 18-7-20 下午5:34
 * 页面的中转Activity的基类
 */

abstract class BasePageActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_page


    override fun initView() {
        val fragment = getRootFragment()
        fragment.arguments = intent.extras
        loadRootFragment(R.id.pageContainer, fragment)
    }


    /**
     * 获取跟节点的Fragment
     */
    abstract fun getRootFragment(): BasePageFragment


    /**
     * 返回的话先对Activity中的Fragment做弹栈处理
     * 如果只有一个就直接关闭页面
     */
    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            finish()
        }
    }
}