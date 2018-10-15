package com.fungo.sample.main

import android.os.Bundle
import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.utils.AppUtils
import com.fungo.baselib.utils.LogUtils
import com.fungo.baselib.utils.StatusBarUtils
import com.fungo.baselib.web.WebActivity
import com.fungo.sample.R
import com.fungo.sample.main.normal.NormalActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Pinger
 * @since 18-7-24 下午2:56
 */

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
        StatusBarUtils.setStatusBarHeight(statusBar)

        setSupportActionBar(toolbar)
    }

    override val layoutResID: Int
        get() = R.layout.activity_main

    fun onNormal(view: View) {
        LogUtils.d(view.transitionName)
        startActivity(NormalActivity::class.java)
    }

    fun onRecycler(view: View) {
        LogUtils.d(view.transitionName)
        PageActivity.start(this, FragmentFactory.FRAGMENT_KEY_RECYCLER, "列表页面")
    }

    fun onWeb(view: View) {
        LogUtils.d(view.transitionName)
        WebActivity.start(this, "https://juejin.im/post/5b5959a15188257f0b582c7e?utm_source=gold_browser_extension")
    }

    override fun onBackPressedSupport() {

        AppUtils.moveTaskToBack(this)

        //super.onBackPressedSupport()
    }
}
