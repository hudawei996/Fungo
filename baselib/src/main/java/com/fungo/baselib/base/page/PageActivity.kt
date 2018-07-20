package com.fungo.baselib.base.page

import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseActivity

/**
 * @author Pinger
 * @since 18-7-20 下午5:34
 * 页面的中转Activity,用来分发各种Fragment
 */

class PageActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_page



}