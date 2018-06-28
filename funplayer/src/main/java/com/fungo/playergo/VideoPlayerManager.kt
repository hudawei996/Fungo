package com.fungo.playergo


import android.content.Context
import com.fungo.playergo.create.IVideoExecutor
import com.fungo.playergo.create.VideoPlayerFactory


/**
 * @author Pinger
 * @since 2017/10/29 0030 下午 18:11
 * 视频播放器管理器，方便在条目上播放
 */

class VideoPlayerManager private constructor() {

    private var mVideoPlayer: VideoPlayer? = null
    private var mPlayerFactory: VideoPlayerFactory? = null

    /**
     * 获取当前播放器
     */
    fun getCurrentVideoPlayer(): VideoPlayer? {
        return mVideoPlayer
    }

    /**
     * 设置当前播放器
     */
    fun setCurrentVideoPlayer(videoPlayer: VideoPlayer) {
        if (mVideoPlayer !== videoPlayer) {
            releaseVideoPlayer()
            mVideoPlayer = videoPlayer
        }
    }


    /**
     * 设置抽象工厂
     */
    fun setVideoPlayer(factory: VideoPlayerFactory) {
        mPlayerFactory = factory
    }

    /**
     * 获取工厂播放器
     */
    fun getVideoPlayer(context:Context): IVideoExecutor {
        return if (mPlayerFactory == null) {
            throw NullPointerException("please first initView video player factory...")
        } else {
            val executor = mPlayerFactory!!.create(context)
            executor.setConstant()
            executor
        }
    }

    /**
     * 暂停播放
     */
    fun pauseVideoPlayer() {
        if (mVideoPlayer != null && (mVideoPlayer!!.isPlaying() || mVideoPlayer!!.isBufferingPlaying())) {
            mVideoPlayer!!.pause()
        }
    }

    /**
     * 重新播放
     */
    fun resumeVideoPlayer() {
        if (mVideoPlayer != null && (mVideoPlayer!!.isPaused() || mVideoPlayer!!.isBufferingPaused())) {
            mVideoPlayer!!.restart()
        }
    }

    /**
     * 释放播放器
     */
    fun releaseVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer!!.release()
            mVideoPlayer = null
        }
    }

    /**
     * 返回按钮，退出全屏或者退出小窗口模式
     */
    fun onBackPressed(): Boolean {
        if (mVideoPlayer != null) {
            if (mVideoPlayer!!.isFullScreen()) {
                return mVideoPlayer!!.exitFullScreen()
            } else if (mVideoPlayer!!.isTinyWindow()) {
                return mVideoPlayer!!.exitTinyWindow()
            }
        }
        return false
    }

    /**
     * 伴生对象获取单利
     */
    companion object {
        private var sInstance: VideoPlayerManager? = null
        val instance: VideoPlayerManager
            @Synchronized get() {
                if (sInstance == null) {
                    sInstance = VideoPlayerManager()
                }
                return sInstance!!
            }
    }
}
