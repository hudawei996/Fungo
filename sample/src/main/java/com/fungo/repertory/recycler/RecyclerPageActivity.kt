package com.fungo.repertory.recycler

import android.view.View

import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:09
 */

class RecyclerPageActivity : BaseActivity() {
    override val layoutResID: Int
        get() = R.layout.activity_recycler_page

    override fun onClick(view: View) {
        if (view.id == R.id.btn_recycler_view) {
            startActivity(RecyclerViewActivity::class.java)
        }
    }

    override fun initView() {
        setActionBar(getString(R.string.recycler_page), true)
    }
}
