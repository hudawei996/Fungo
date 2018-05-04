package com.fungo.baselib.app

import android.app.Application
import com.fungo.baselib.player.VideoPlayerManager
import com.fungo.baselib.player.ijkplayer.IjkVideoPlayerFactory
import com.fungo.baselib.utils.BaseUtils

/**
 * @author Pinger
 * @since 18-3-31 下午5:40
 *
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
        initSDK()
    }

    open fun initSDK() {
    }

    private fun init() {
        initUtils()
        initPlayer()
    }


    /**
     * 初始化工具类
     */
    private fun initUtils() {
        BaseUtils.init(this)
    }


    /**
     * 播放器策略设置
     */
    private fun initPlayer() {
        VideoPlayerManager.instance.setVideoPlayer(IjkVideoPlayerFactory())
    }
}