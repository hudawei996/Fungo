package com.fungo.player.listener

/**
 * @author Pinger
 * @since 18-6-13 下午2:23
 * 播放器播放状态监听器
 */

interface OnPlayerListener {


    /**
     * 播放暂停
     */
    fun onVideoStarted()

    /**
     * 继续播放
     */
    fun onVideoPaused()

    /**
     * 播放完成
     */
    fun onComplete()

    /**
     * 准备播放完成
     */
    fun onPrepared()

    /**
     * 播放出错
     */
    fun onError()

    /**
     * 播放器内部事件回调
     */
    fun onInfo(what: Int, extra: Int)

}