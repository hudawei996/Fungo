package com.fungo.sample.main

import com.fungo.baselib.base.page.BasePageActivity
import com.fungo.baselib.base.page.BasePageFragment

/**
 * @author Pinger
 * @since 18-7-24 下午2:56
 * 填充一个主页
 */

class MainActivity : BasePageActivity() {


    override fun getPageFragment(): BasePageFragment {
        return MainFragment()
    }

    override fun isSwipeBackEnable(): Boolean = false


}
