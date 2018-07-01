package com.fungo.repertory.widget

import android.view.View

import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.repertory.R
import com.fungo.repertory.widget.banner.BannerViewActivity
import com.fungo.repertory.widget.falling.FallingViewActivity

/**
 * @author Pinger
 * @since 2018/1/11 0011 上午 11:28
 */

class CustomViewActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_custom_view

    override fun initView() {
        setActionBar(getString(R.string.widget_custom), true)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_falling -> startActivity(FallingViewActivity::class.java)
            R.id.btn_banner -> startActivity(BannerViewActivity::class.java)
        }
    }
}
