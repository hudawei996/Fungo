package com.fungo.baselib.base.page

import com.fungo.baselib.R
import com.fungo.baselib.base.page.swipeback.SwipeBackActivity

/**
 * @author Pinger
 * @since 18-7-20 下午5:34
 * 页面的中转Activity的基类
 */

abstract class BasePageActivity : SwipeBackActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_page


    /**
     * View填充后，继续填充根Fragment
     */
    override fun initAfter() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(layoutResID, BasePageFragment())
        transaction.commitAllowingStateLoss()
    }
}