package com.fungo.repertory.player

import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.imagego.ImageManager
import com.fungo.player.controller.StandardPlayerController
import com.fungo.repertory.R
import kotlinx.android.synthetic.main.activity_player_main.*

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:16
 */

class PlayerMainActivity : BaseActivity() {

    private var mCurrentPlayer = -1

    private var mUrl = "http://221.228.226.5/14/z/w/y/y/zwyyobhyqvmwslabxyoaixvyubmekc/sh.yinyuetai.com/4599015ED06F94848EBF877EAAE13886.mp4"
    private  var mLiveUrl = "http://asp.cntv.lxdns.com/asp/hls/850/0303000a/3/default/4e8c094b10014053811af7bd685f8953/850.m3u8"
    private var mImageUrl = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1522491070&di=863511a7fdc58615663af013bb1b9765&src=http://img.xgo-img.com.cn/pics/1538/1537620.jpg"

    override val layoutResID: Int
        get() = R.layout.activity_player_main

    override fun initView() {
        setToolBar(getString(R.string.video_player), true)
        video_normal.performClick()
    }

    override fun onClick(view: View) {
        when (view) {
            video_normal -> setNormalVideo()
            video_live -> setLiveVideo()
        }
    }

    private fun setNormalVideo() {
        if (mCurrentPlayer == 0) return
        mCurrentPlayer = 0
        funPlayer.release()
        val controller = StandardPlayerController(this)
        controller.setTitle("喜欢你，黑服你，黑服你")
        ImageManager.instance.loadImage(mImageUrl,controller.getImage())
        funPlayer.setController(controller)
        funPlayer.setUrl(mUrl)
        funPlayer.start()
    }

    private fun setLiveVideo() {
        if (mCurrentPlayer == 1) return
        mCurrentPlayer = 1
        funPlayer.release()
        val controller = StandardPlayerController(this)
        ImageManager.instance.loadImage(mImageUrl,controller.getImage())
        funPlayer.setController(controller)
        funPlayer.setUrl(mLiveUrl)
        funPlayer.start()
    }


    override fun onResume() {
        super.onResume()
        funPlayer.resume()
    }


    override fun onPause() {
        super.onPause()
        funPlayer.pause()
    }

    override fun onBackPressed() {
        if (funPlayer.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }


}
