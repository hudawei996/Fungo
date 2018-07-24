package com.fungo.repertory.main

import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
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

    override fun getStatusBarView(): View? {
        return statusBar
    }
}
