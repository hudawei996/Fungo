package com.fungo.repertory.recycler

import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.net.repository.Repository
import com.fungo.repertory.R
import java.util.function.Consumer

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:11
 */

class RecyclerViewActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_recycler_view

    override fun initView() {
        setActionBar(getString(R.string.recycler_view), true)
    }

    override fun initData() {
        val url = ""




    }
}
