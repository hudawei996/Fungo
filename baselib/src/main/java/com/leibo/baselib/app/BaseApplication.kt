package com.leibo.baselib.app

import android.app.Application
import com.leibo.baselib.net.dependency.DaggerNetComponent
import com.leibo.baselib.net.dependency.NetHolder
import com.leibo.baselib.net.dependency.NetModule
import com.leibo.baselib.player.VideoPlayerManager
import com.leibo.baselib.player.ijkplayer.IjkVideoPlayerFactory
import com.leibo.baselib.utils.BaseUtils

/**
 * @author Pinger
 * @since 18-3-31 下午5:40
 *
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        init()
        val netComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule(this, ""))
                .build()
        NetHolder.setNetComponent(netComponent)
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