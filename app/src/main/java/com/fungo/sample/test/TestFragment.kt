package com.fungo.sample.test

import com.fungo.baseuilib.fragment.BaseNavFragment
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 2018/11/3 21:53
 */
class TestFragment : BaseNavFragment() {

    override fun getNavContentResID(): Int = R.layout.fragment_main

    override fun initContentView() {
        showPageContent()
    }

}