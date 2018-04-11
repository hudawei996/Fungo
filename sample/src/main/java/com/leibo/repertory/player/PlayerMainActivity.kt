package com.leibo.repertory.player

import android.view.View
import com.leibo.baselib.base.basic.BaseActivity
import com.leibo.baselib.player.VideoPlayerManager
import com.leibo.repertory.R
import com.leibo.repertory.player.controller.LiveVideoPlayerController
import com.leibo.repertory.player.controller.SimpleVideoPlayerController
import kotlinx.android.synthetic.main.activity_player_main.*

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:16
 */

class PlayerMainActivity : BaseActivity() {

    private var mCurrentPlayer = -1

    override val layoutResID: Int
        get() = R.layout.activity_player_main

    override fun initView() {
        setActionBar(getString(R.string.video_player), true)
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
        livePlayer.visibility = View.GONE
        livePlayer.releasePlayer()
        val controller = SimpleVideoPlayerController(this)
        val url = "http://221.228.226.5/14/z/w/y/y/zwyyobhyqvmwslabxyoaixvyubmekc/sh.yinyuetai.com/4599015ED06F94848EBF877EAAE13886.mp4"
        controller.setTitle("喜欢你，黑服你，黑服你")
        controller.setImage("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1522491070&di=863511a7fdc58615663af013bb1b9765&src=http://img.xgo-img.com.cn/pics/1538/1537620.jpg")
        normalPlayer.visibility = View.VISIBLE
        normalPlayer.setController(controller)
        normalPlayer.setUp(url)
        normalPlayer.start()
    }

    private fun setLiveVideo() {
        if (mCurrentPlayer == 1) return
        mCurrentPlayer = 1
        normalPlayer.releasePlayer()
        normalPlayer.visibility = View.GONE
        val controller = LiveVideoPlayerController(this)
        val url = "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8 "
        controller.setImage("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1522491070&di=863511a7fdc58615663af013bb1b9765&src=http://img.xgo-img.com.cn/pics/1538/1537620.jpg")
        livePlayer.visibility = View.VISIBLE
        livePlayer.setController(controller)
        livePlayer.setUp(url)
        livePlayer.start()
    }


    override fun onResume() {
        super.onResume()
        VideoPlayerManager.instance.resumeVideoPlayer()
    }


    override fun onPause() {
        super.onPause()
        VideoPlayerManager.instance.pauseVideoPlayer()
    }

    override fun onBackPressed() {
        if (VideoPlayerManager.instance.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }


}
