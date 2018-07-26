package com.fungo.repertory.main

import android.os.Bundle
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 18-7-26 下午12:56
 *
 */

class TestActivity(override val layoutResID: Int = R.layout.activity_test) : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
    }


}