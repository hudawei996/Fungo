package com.leibo.repertory.recycler

import com.leibo.baselib.base.BaseActivity
import com.leibo.repertory.R

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
}
