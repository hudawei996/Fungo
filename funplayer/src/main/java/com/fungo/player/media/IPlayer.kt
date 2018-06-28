package com.fungo.player.media

import android.content.res.AssetFileDescriptor
import android.view.Surface
import android.view.SurfaceHolder

/**
 * @author Pinger
 * @since 18-6-13 下午2:34
 *　视频播放的方法抽取，包含使用的播放器的所有方法，只需要给子类去实现即可
 */

interface IPlayer {

    /**
     * 初始化播放器实例
     */
    fun initPlayer()

    /**
     * 设置播放地址
     * @param path 播放地址
     * @param headers 播放地址请求头
     */
    fun setDataSource(path: String?, headers: Map<String, String>?)

    /**
     * 用于播放raw和asset里面的视频文件
     */
    fun setDataSource(fd: AssetFileDescriptor?)

    /**
     * 播放
     */
    fun start()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 停止
     */
    fun stop()

    /**
     * 准备开始播放（异步）
     */
    fun prepareAsync()

    /**
     * 重置播放器
     */
    fun reset()

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean

    /**
     * 调整进度
     */
    fun seekTo(time: Long)

    /**
     * 释放播放器
     */
    fun release()

    /**
     * 获取当前播放的位置
     */
    fun getCurrentPosition(): Long

    /**
     * 获取视频总时长
     */
    fun getDuration(): Long

    /**
     * 设置渲染视频的View,主要用于TextureView
     */
    fun setSurface(surface: Surface)

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     */
    fun setDisplay(holder: SurfaceHolder)

    /**
     * 设置音量
     */
    fun setVolume(v1: Float, v2: Float)

    /**
     * 设置是否循环播放
     */
    fun setLooping(isLooping: Boolean)

    /**
     * 设置硬解码
     */
    fun setEnableMediaCodec(isEnable: Boolean)

    /**
     * 设置其他播放配置
     */
    fun setOptions()

    /**
     * 设置播放速度
     */
    fun setSpeed(speed: Float)

    /**
     * 获取当前缓冲的网速
     */
    fun getTcpSpeed(): Long


    //-----------------------------------------
    //-----------------------------------------
    //-----------------------------------------

    /**
     * 设置准备监听
     */
    fun setOnPreparedListener(listener: OnPreparedListener)

    /**
     * 设置加载完成监听
     */
    fun setOnCompletionListener(listener: OnCompletionListener)

    /**
     * 设置缓冲更新
     */
    fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener)

    /**
     * 设置视频尺寸改变监听
     */
    fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener)

    /**
     * 设置加载错误监听
     */
    fun setOnErrorListener(listener: OnErrorListener)

    /**
     * 设置视频信息更新监听
     */
    fun setOnInfoListener(listener: OnInfoListener)


    /**
     * 视频信息更新接口
     */
    interface OnInfoListener {
        fun onInfo(what: Int, extra: Int): Boolean
    }

    /**
     * 视频加载错误接口
     */
    interface OnErrorListener {
        fun onError(): Boolean
    }

    /**
     * 视频尺寸改变接口
     */
    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(width: Int, height: Int)
    }

    /**
     * 视频缓冲接口
     */
    interface OnBufferingUpdateListener {
        fun onBufferingUpdate(percent: Int)
    }

    /**
     * 视频加载完成接口
     */
    interface OnCompletionListener {
        fun onCompletion()
    }

    /**
     * 视频准备播放接口
     */
    interface OnPreparedListener {
        fun onPrepared()
    }
}