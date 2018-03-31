package com.leibo.baselib.player.ijkplayer

import android.content.Context
import android.net.Uri
import android.view.Surface
import com.leibo.baselib.player.PlayerConstant
import com.leibo.baselib.player.create.IVideoExecutor

import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/**
 * @author Pinger
 * @since 2017/12/21 0021 下午 2:17
 */

class IjkVideoPlayer : IVideoExecutor {

    private val mMediaPlayer: IjkMediaPlayer?

    init {
        mMediaPlayer = IjkMediaPlayer()
        mMediaPlayer.setOption(1, "analyzemaxduration", 100L)
        mMediaPlayer.setOption(1, "probesize", 10240L)
        mMediaPlayer.setOption(1, "flush_packets", 1L)
        mMediaPlayer.setOption(4, "packet-buffering", 0L)
        mMediaPlayer.setOption(4, "framedrop", 8)
    }

    override fun start() {
        mMediaPlayer?.start()
    }

    override fun reset() {
        mMediaPlayer?.reset()
    }

    override fun pause() {
        mMediaPlayer?.pause()
    }

    override fun release() {
        mMediaPlayer?.release()
    }

    override fun setDataSource(context: Context, url: String, header: Map<String, String>) {
        try {
            mMediaPlayer?.setDataSource(context, Uri.parse(url), header)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun prepareAsync() {
        try {
            mMediaPlayer?.prepareAsync()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    override fun seekTo(position: Long) {
        mMediaPlayer?.seekTo(position)
    }

    override fun setSurface(surface: Surface) {
        mMediaPlayer?.setSurface(surface)
    }

    override fun getDuration(): Long {
        return mMediaPlayer?.duration ?: 0
    }

    override fun setConstant() {
        PlayerConstant.MEDIA_INFO_VIDEO_RENDERING_START = IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START
        PlayerConstant.MEDIA_INFO_BUFFERING_START = IMediaPlayer.MEDIA_INFO_BUFFERING_START
        PlayerConstant.MEDIA_INFO_BUFFERING_END = IMediaPlayer.MEDIA_INFO_BUFFERING_END
        PlayerConstant.MEDIA_INFO_VIDEO_ROTATION_CHANGED = IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED
        PlayerConstant.MEDIA_INFO_NOT_SEEK = IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE
    }

    override fun getCurrentPosition(): Long {
        return mMediaPlayer?.currentPosition ?: 0
    }

    override fun getSpeed(speed: Float): Float {
        return mMediaPlayer?.getSpeed(speed) ?: 0f
    }

    override fun setSpeed(speed: Float) {
        mMediaPlayer?.setSpeed(speed)
    }

    override fun getTcpSpeed(): Long {
        return mMediaPlayer?.tcpSpeed ?: 0
    }

    override fun setAudioStreamType(type: Int) {
        mMediaPlayer?.setAudioStreamType(type)
    }

    override fun setOnPreparedListener(listener: IVideoExecutor.OnPreparedListener) {
        val preparedListener = IMediaPlayer.OnPreparedListener { listener.onPrepared(this@IjkVideoPlayer) }
        mMediaPlayer?.setOnPreparedListener(preparedListener)
    }

    override fun setOnCompletionListener(listener: IVideoExecutor.OnCompletionListener) {
        val completionListener = IMediaPlayer.OnCompletionListener { listener.onCompletion(this@IjkVideoPlayer) }
        mMediaPlayer?.setOnCompletionListener(completionListener)
    }

    override fun setOnBufferingUpdateListener(listener: IVideoExecutor.OnBufferingUpdateListener) {
        val bufferingUpdateListener = IMediaPlayer.OnBufferingUpdateListener { _, i -> listener.onBufferingUpdate(this@IjkVideoPlayer, i) }
        mMediaPlayer?.setOnBufferingUpdateListener(bufferingUpdateListener)
    }


    override fun setOnVideoSizeChangedListener(listener: IVideoExecutor.OnVideoSizeChangedListener) {
        val sizeChangedListener = IMediaPlayer.OnVideoSizeChangedListener { _, i, i1, i2, i3 -> listener.onVideoSizeChanged(this@IjkVideoPlayer, i, i1, i2, i3) }
        mMediaPlayer?.setOnVideoSizeChangedListener(sizeChangedListener)
    }

    override fun setOnErrorListener(listener: IVideoExecutor.OnErrorListener) {
        val errorListener = IMediaPlayer.OnErrorListener { _, i, i1 -> listener.onError(this@IjkVideoPlayer, i, i1) }
        mMediaPlayer?.setOnErrorListener(errorListener)
    }

    override fun setOnInfoListener(listener: IVideoExecutor.OnInfoListener) {
        val infoListener = IMediaPlayer.OnInfoListener { _, i, i1 -> listener.onInfo(this@IjkVideoPlayer, i, i1) }
        mMediaPlayer?.setOnInfoListener(infoListener)
    }
}
