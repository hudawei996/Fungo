package com.leibo.repertory.player

import com.leibo.baselib.base.BaseActivity
import com.leibo.repertory.R

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:16
 */

class PlayerMainActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_player_main

    override fun initView() {
        setActionBar(getString(R.string.video_player), true)
    }
}
