package com.leibo.baselib.player.create


import android.content.Context
import android.view.Surface

/**
 * @author Pinger
 * @since 2017/5/21 0021 下午 6:51
 * 公共方法抽取出来
 */
interface IVideoExecutor {

    /**
     * 获取时长
     */
    fun getDuration(): Long

    /**
     * 获取当前时长位置
     */
    fun getCurrentPosition(): Long

    /**
     * 获取下载速度
     */
    fun getTcpSpeed(): Long

    /**
     * 开始播放
     */
    fun start()

    /**
     * 重置
     */
    fun reset()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 释放
     */
    fun release()

    /**
     * 设置播放地址
     */
    fun setDataSource(context: Context, url: String, header: Map<String, String>)

    /**
     * 播放前准备
     */
    fun prepareAsync()

    /**
     * 拖动进度条
     */
    fun seekTo(position: Long)

    /**
     * 设置播放渲染层
     */
    fun setSurface(surface: Surface)

    /**
     * 设置播放常量
     */
    fun setConstant()

    /**
     * 获取播放速度
     */
    fun getSpeed(speed: Float): Float

    /**
     * 设置播放速度
     */
    fun setSpeed(speed: Float)

    /**
     * 设置音频类型
     */
    fun setAudioStreamType(type: Int)

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
        fun onInfo(executor: IVideoExecutor, what: Int, extra: Int): Boolean
    }

    /**
     * 视频加载错误接口
     */
    interface OnErrorListener {
        fun onError(executor: IVideoExecutor, what: Int, extra: Int): Boolean
    }

    /**
     * 视频尺寸改变接口
     */
    interface OnVideoSizeChangedListener {
        fun onVideoSizeChanged(executor: IVideoExecutor, width: Int, height: Int, sar_num: Int, sar_den: Int)
    }

    /**
     * 视频拖动完成接口
     */
    interface OnSeekCompleteListener {
        fun onSeekComplete(executor: IVideoExecutor)
    }

    /**
     * 视频缓冲接口
     */
    interface OnBufferingUpdateListener {
        fun onBufferingUpdate(executor: IVideoExecutor, percent: Int)
    }

    /**
     * 视频加载完成接口
     */
    interface OnCompletionListener {
        fun onCompletion(executor: IVideoExecutor)
    }

    /**
     * 视频准备播放接口
     */
    interface OnPreparedListener {
        fun onPrepared(executor: IVideoExecutor)
    }

}
