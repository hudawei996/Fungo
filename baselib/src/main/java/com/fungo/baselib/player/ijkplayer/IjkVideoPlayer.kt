package com.fungo.baselib.player.ijkplayer

import android.content.Context
import android.net.Uri
import android.view.Surface
import com.fungo.baselib.player.PlayerConstant
import com.fungo.baselib.player.create.IVideoExecutor

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

        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)

        // seek只支持关键帧
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)

        // RV32是通用配置，也可以选择YV12来提高性能（免去了YUV-RGB的转换，但是部分手机不支持）
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
        //ijkMediaPlayer.setOverlayFormat(AvFourCC.SDL_FCC_RV16);
        // ijkplayer-0.2.0 开始，不在ffmpeg层强制concat的safe选项为0，而是动态配置（用于cntv分段的回看源）
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "safe", "0")

        // 清除本地dns缓存，避免播放完http后播不了https
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)

        // FIXME 官方ijkplayer-n0.2.0之后，可以在java层手动配置丢帧策略
        // 参照ffmpeg里面的定义（影响画面质量，尤其是马赛克，但是值越小，可能导致CPU繁忙或者低端手机音画不同步，图像处理跟不上音频时钟）

        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 8)
        // 渲染时候可以丢弃的帧
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 8)
        // FIXME 官方ijkplayer-n0.2.0之后，可以配置是否使用MediaCodec的硬解码
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
        // 可以配置是否使用OpenSLES替代AudioTrack来最为音频输出
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
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
