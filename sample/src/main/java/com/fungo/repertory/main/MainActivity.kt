package com.fungo.repertory.main

import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.web.WebActivity
import com.fungo.repertory.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Pinger
 * @since 18-7-24 下午2:56
 */

class MainActivity : BaseActivity() {


    override val layoutResID: Int
        get() = R.layout.activity_main


    fun onHost(view: View) {
        startActivity(PageActivity::class.java)
    }

    fun onWeb(view: View) {
        WebActivity.start(this,"https://juejin.im/post/5b5959a15188257f0b582c7e?utm_source=gold_browser_extension")
    }

    fun onTest(view: View) {
        startActivity(TestActivity::class.java)
    }
}
