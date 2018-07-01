package com.fungo.player.media

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import com.fungo.player.utils.RawDataSourceProvider
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * @author Pinger
 * @since 18-6-13 下午2:54
 * Ijk播放器，实现顶层的方法，提供播放的内核
 */

class IjkPlayer(private var context: Context) : IPlayer {

    private lateinit var mMediaPlayer: IjkMediaPlayer
    private var isLooping: Boolean = false
    private var isEnableMediaCodec: Boolean = false
    private var mErrorListener: IPlayer.OnErrorListener? = null
    private var mVideoSizeChangedListener: IPlayer.OnVideoSizeChangedListener? = null

    override fun initPlayer() {
        println("---------> initPlayer")
        mMediaPlayer = IjkMediaPlayer()
        setOptions()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setOnNativeInvokeListener { _, _ -> true }
    }

    override fun setDataSource(path: String?, headers: Map<String, String>?) {
        println("---------> setDataSource")

        try {
            val uri = Uri.parse(path)
            if (uri.scheme == ContentResolver.SCHEME_ANDROID_RESOURCE) {
                mMediaPlayer.setDataSource(RawDataSourceProvider.create(context, uri))
            } else {
                mMediaPlayer.setDataSource(context, uri, headers)
            }

        } catch (e: Exception) {
            mErrorListener?.onError()
        }

    }

    override fun setDataSource(fd: AssetFileDescriptor?) {
        println("---------> setDataSource")
        try {
            mMediaPlayer.setDataSource(fd?.fileDescriptor)
        } catch (e: Exception) {
            mErrorListener?.onError()
        }

    }

    override fun start() {
        println("---------> start")

        try {
            mMediaPlayer.start()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun pause() {
        println("---------> pause")

        try {
            mMediaPlayer.pause()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        println("---------> stop")

        try {
            mMediaPlayer.stop()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun prepareAsync() {
        println("---------> prepareAsync")
        try {
            mMediaPlayer.prepareAsync()
        } catch (e: Exception) {
            mErrorListener?.onError()
        }

    }

    override fun reset() {
        println("---------> reset")
        mMediaPlayer.reset()
        if (mVideoSizeChangedListener != null) {
            setOnVideoSizeChangedListener(mVideoSizeChangedListener!!)
        }
        mMediaPlayer.isLooping = isLooping
        setOptions()
        setEnableMediaCodec(isEnableMediaCodec)
    }

    override fun isPlaying(): Boolean {
        println("---------> isPlaying")
        return mMediaPlayer.isPlaying
    }

    override fun seekTo(time: Long) {
        println("---------> seekTo")
        try {
            mMediaPlayer.seekTo(time)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    override fun release() {
        println("---------> release")
        mMediaPlayer.release()
    }

    override fun getCurrentPosition(): Long {
        println("---------> getCurrentPosition")
        return mMediaPlayer.currentPosition
    }

    override fun getDuration(): Long {
        println("---------> getDuration")
        return mMediaPlayer.duration
    }

    override fun setSurface(surface: Surface) {
        println("---------> setSurface")
        mMediaPlayer.setSurface(surface)
    }

    override fun setDisplay(holder: SurfaceHolder) {
        println("---------> setDisplay")
        mMediaPlayer.setDisplay(holder)
    }

    override fun setVolume(v1: Float, v2: Float) {
        println("---------> setVolume")
        mMediaPlayer.setVolume(v1, v2)

    }

    override fun setLooping(isLooping: Boolean) {
        println("---------> setLooping")
        this.isLooping = isLooping
        mMediaPlayer.isLooping = isLooping
    }

    override fun setEnableMediaCodec(isEnable: Boolean) {
        println("---------> setEnableMediaCodec")
        isEnableMediaCodec = isEnable
        val value = if (isEnable) 1 else 0
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value.toLong())//开启硬解码
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value.toLong())
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", value.toLong())

    }

    override fun setOptions() {
        println("---------> setOptions")
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
        // seek只支持关键帧
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)

        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())

        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "safe", "0")

        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)

        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 8)
        // 渲染时候可以丢弃的帧
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 8)
        // 可以配置是否使用OpenSLES替代AudioTrack来最为音频输出
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
    }

    override fun setSpeed(speed: Float) {
        println("---------> setSpeed")

        mMediaPlayer.setSpeed(speed)
    }

    override fun getTcpSpeed(): Long {
        println("---------> getTcpSpeed")

        return mMediaPlayer.tcpSpeed
    }

    override fun setOnPreparedListener(listener: IPlayer.OnPreparedListener) {
        println("---------> setOnPreparedListener")

        val preparedListener = IMediaPlayer.OnPreparedListener { listener.onPrepared() }
        mMediaPlayer.setOnPreparedListener(preparedListener)
    }

    override fun setOnCompletionListener(listener: IPlayer.OnCompletionListener) {
        println("---------> setOnCompletionListener")

        val completionListener = IMediaPlayer.OnCompletionListener { listener.onCompletion() }
        mMediaPlayer.setOnCompletionListener(completionListener)
    }

    override fun setOnBufferingUpdateListener(listener: IPlayer.OnBufferingUpdateListener) {
        println("---------> setOnBufferingUpdateListener")

        val bufferingUpdateListener = IMediaPlayer.OnBufferingUpdateListener { _, i -> listener.onBufferingUpdate(i) }
        mMediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener)
    }


    override fun setOnVideoSizeChangedListener(listener: IPlayer.OnVideoSizeChangedListener) {
        println("---------> setOnVideoSizeChangedListener")

        this.mVideoSizeChangedListener = listener
        val sizeChangedListener = IMediaPlayer.OnVideoSizeChangedListener { _, i, i1, _, _ -> listener.onVideoSizeChanged(i, i1) }
        mMediaPlayer.setOnVideoSizeChangedListener(sizeChangedListener)
    }


    override fun setOnErrorListener(listener: IPlayer.OnErrorListener) {
        println("---------> setOnErrorListener")

        this.mErrorListener = listener
        val errorListener = IMediaPlayer.OnErrorListener { _, _, _ -> listener.onError() }
        mMediaPlayer.setOnErrorListener(errorListener)
    }

    override fun setOnInfoListener(listener: IPlayer.OnInfoListener) {
        println("---------> setOnInfoListener")

        val infoListener = IMediaPlayer.OnInfoListener { _, i, i1 -> listener.onInfo(i, i1) }
        mMediaPlayer.setOnInfoListener(infoListener)
    }
}