package com.fungo.player.controller

import android.graphics.Bitmap

/**
 * @author Pinger
 * @since 18-6-13 下午2:20
 * 控制器接口，定义好控制器里的操作方法
 */

interface IPlayerController {

    //　－－－－－－－－－－播放器常用API－－－－－－－－－－
    fun start()

    fun pause()

    fun getDuration(): Long

    fun getCurrentPosition(): Long

    fun seekTo(position: Long)

    fun getBufferPercentage(): Int

    fun setMute()

    fun isMute(): Boolean

    fun setLock(isLocked: Boolean)

    fun setScreenScale(screenScale: Int)

    fun retry()

    fun setSpeed(speed: Float)

    fun getTcpSpeed(): Long

    fun setMirrorRotation(enable: Boolean)

    fun doScreenShot(): Bitmap?

    fun enterFullScreen()

    fun exitFullScreen()

    fun enterTinyWindow()

    fun exitTinyWindow()

    //　－－－－－－－－－－播放器当前状态－－－－－－－－－－
    fun isPlaying(): Boolean

    fun isPaused():Boolean

    fun isIdle(): Boolean

    fun isPreparing(): Boolean

    fun isPrepared(): Boolean

    fun isBuffering(): Boolean

    fun isBuffered(): Boolean

    fun isError(): Boolean

    fun isCompleted(): Boolean


    //　－－－－－－－－－－播放器当前播放模式－－－－－－－－－－
    fun isFullScreen(): Boolean

    fun isTinyWindow(): Boolean

    fun isNormal(): Boolean
}