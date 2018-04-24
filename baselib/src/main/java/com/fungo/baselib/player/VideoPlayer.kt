package com.fungo.baselib.player

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.fungo.baselib.player.controller.BaseVideoPlayerController
import com.fungo.baselib.player.create.IVideoExecutor


/**
 * @author Pinger
 * @since 2017/10/29 0030 上午 11:45
 * 播放器，内部集成，将具体实现进行抽离
 */
class VideoPlayer @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null) : FrameLayout(mContext, attrs), IVideoPlayer, TextureView.SurfaceTextureListener {

    private var mCurrentState = STATE_IDLE
    private var mCurrentMode = MODE_NORMAL
    private var mAudioManager: AudioManager? = null
    private var mVideoExecutor: IVideoExecutor? = null
    private var mContainer = FrameLayout(mContext)
    private var mTextureView: AspectTextureView? = null
    private var mController: BaseVideoPlayerController? = null
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mSurface: Surface? = null
    private var mUrl: String? = null
    private var mHeaders: Map<String, String>? = null
    private var mBufferPercentage: Int = 0
    private var continueFromLastPosition: Boolean = false
    private var skipToPosition: Long = 0


    companion object {

        private val TAG = VideoPlayer::class.java.simpleName

        /**
         * 播放错误
         */
        const val STATE_ERROR = -1
        /**
         * 播放未开始
         */
        const val STATE_IDLE = 0
        /**
         * 播放准备中
         */
        const val STATE_PREPARING = 1
        /**
         * 播放准备就绪
         */
        const val STATE_PREPARED = 2
        /**
         * 正在播放
         */
        const val STATE_PLAYING = 3
        /**
         * 暂停播放
         */
        const val STATE_PAUSED = 4
        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
         */
        const val STATE_BUFFERING_PLAYING = 5
        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
         */
        const val STATE_BUFFERING_PAUSED = 6
        /**
         * 播放完成
         */
        const val STATE_COMPLETED = 7

        /**
         * 普通模式
         */
        const val MODE_NORMAL = 10
        /**
         * 全屏模式
         */
        const val MODE_FULL_SCREEN = 11
        /**
         * 小窗口模式
         */
        const val MODE_TINY_WINDOW = 12
    }

    init {
        mContainer.setBackgroundColor(Color.BLACK)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        this.addView(mContainer, params)
    }

    override fun setUp(url: String, headers: Map<String, String>) {
        mUrl = url
        mHeaders = headers
    }

    override fun setUp(url: String) {
        setUp(url, HashMap())
    }

    /**
     * 设置控制器
     */
    fun setController(controller: BaseVideoPlayerController) {
        mContainer.removeView(mController)
        mController = controller
        mController!!.reset()
        mController!!.setVideoPlayer(this)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        mContainer.addView(mController, params)
    }

    /**
     * 是否从上一次的位置继续播放
     *
     * @param continueFromLastPosition true从上一次的位置继续播放
     */
    override fun continueFromLastPosition(continueFromLastPosition: Boolean) {
        this.continueFromLastPosition = continueFromLastPosition
    }

    override fun setSpeed(speed: Float) {
        if (mVideoExecutor != null) {
            mVideoExecutor!!.setSpeed(speed)
        }
    }

    override fun start() {
        if (mCurrentState == STATE_IDLE) {
            VideoPlayerManager.Companion.instance.setCurrentVideoPlayer(this)
            initAudioManager()
            initMediaPlayer()
            initTextureView()
            addTextureView()
        } else {
            Log.d(TAG, "VideoPlayer只有在mCurrentState == STATE_IDLE时才能调用start方法.")
        }
    }

    override fun start(position: Long) {
        skipToPosition = position
        start()
    }

    override fun restart() {
        if (mCurrentState == STATE_PAUSED) {
            mVideoExecutor!!.start()
            mCurrentState = STATE_PLAYING
            mController!!.onPlayStateChanged(mCurrentState)
            Log.d(TAG, "STATE_PLAYING")
        } else if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mVideoExecutor!!.start()
            mCurrentState = STATE_BUFFERING_PLAYING
            mController!!.onPlayStateChanged(mCurrentState)
            Log.d(TAG, "STATE_BUFFERING_PLAYING")
        } else if (mCurrentState == STATE_COMPLETED || mCurrentState == STATE_ERROR) {
            mVideoExecutor!!.reset()
            openMediaPlayer()
        } else {
            Log.d(TAG, "VideoPlayer在mCurrentState == " + mCurrentState + "时不能调用restart()方法.")
        }
    }

    override fun pause() {
        if (mCurrentState == STATE_PLAYING) {
            mVideoExecutor!!.pause()
            mCurrentState = STATE_PAUSED
            mController!!.onPlayStateChanged(mCurrentState)
            Log.d(TAG, "STATE_PAUSED")
        }
        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mVideoExecutor!!.pause()
            mCurrentState = STATE_BUFFERING_PAUSED
            mController!!.onPlayStateChanged(mCurrentState)
            Log.d(TAG, "STATE_BUFFERING_PAUSED")
        }
    }

    override fun seekTo(pos: Long) {
        if (mVideoExecutor != null) {
            mVideoExecutor!!.seekTo(pos)
        }
    }

    override fun setVolume(volume: Int) {
        if (mAudioManager != null) {
            mAudioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
        }
    }

    override fun isIdle(): Boolean {
        return mCurrentState == STATE_IDLE
    }

    override fun isPreparing(): Boolean {
        return mCurrentState == STATE_PREPARING
    }

    override fun isPrepared(): Boolean {
        return mCurrentState == STATE_PREPARED
    }

    override fun isBufferingPlaying(): Boolean {
        return mCurrentState == STATE_BUFFERING_PLAYING
    }

    override fun isBufferingPaused(): Boolean {
        return mCurrentState == STATE_BUFFERING_PAUSED
    }

    override fun isPlaying(): Boolean {
        return mCurrentState == STATE_PLAYING
    }

    override fun isPaused(): Boolean {
        return mCurrentState == STATE_PAUSED
    }

    override fun isError(): Boolean {
        return mCurrentState == STATE_ERROR
    }

    override fun isCompleted(): Boolean {
        return mCurrentState == STATE_COMPLETED
    }

    override fun isFullScreen(): Boolean {
        return mCurrentMode == MODE_FULL_SCREEN
    }

    override fun isTinyWindow(): Boolean {
        return mCurrentMode == MODE_TINY_WINDOW
    }

    override fun isNormal(): Boolean {
        return mCurrentMode == MODE_NORMAL
    }

    override fun getMaxVolume(): Int {
        return if (mAudioManager != null) {
            mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        } else 0
    }

    override fun getVolume(): Int {
        return if (mAudioManager != null) {
            mAudioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        } else 0
    }

    override fun getDuration(): Long {
        return if (mVideoExecutor != null) mVideoExecutor!!.getDuration() else 0
    }

    override fun getCurrentPosition(): Long {
        return if (mVideoExecutor != null) mVideoExecutor!!.getCurrentPosition() else 0
    }

    override fun getBufferPercentage(): Int {
        return mBufferPercentage
    }

    override fun getSpeed(speed: Float): Float {
        return if (mVideoExecutor != null) {
            mVideoExecutor!!.getSpeed(speed)
        } else 0f
    }

    override fun getTcpSpeed(): Long {
        return if (mVideoExecutor != null) {
            mVideoExecutor!!.getTcpSpeed()
        } else 0
    }

    private fun initAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioManager!!.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
    }

    private fun initMediaPlayer() {
        if (mVideoExecutor == null) {
            mVideoExecutor = VideoPlayerManager.Companion.instance.getVideoPlayer(context)
            mVideoExecutor!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
    }

    private fun initTextureView() {
        if (mTextureView == null) {
            mTextureView = AspectTextureView(mContext)
            mTextureView!!.surfaceTextureListener = this
        }
    }

    private fun addTextureView() {
        mContainer.removeView(mTextureView)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        mContainer.addView(mTextureView, 0, params)
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture
            openMediaPlayer()
        } else {
            mTextureView!!.surfaceTexture = mSurfaceTexture
        }
    }

    private fun openMediaPlayer() {
        // 屏幕常亮
        mContainer.keepScreenOn = true
        // 设置监听
        mVideoExecutor!!.setOnPreparedListener(mOnPreparedListener)
        mVideoExecutor!!.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener)
        mVideoExecutor!!.setOnCompletionListener(mOnCompletionListener)
        mVideoExecutor!!.setOnErrorListener(mOnErrorListener)
        mVideoExecutor!!.setOnInfoListener(mOnInfoListener)
        mVideoExecutor!!.setOnBufferingUpdateListener(mOnBufferingUpdateListener)
        // 设置dataSource
        mVideoExecutor!!.setDataSource(mContext.applicationContext, mUrl!!, mHeaders!!)
        if (mSurface == null) {
            mSurface = Surface(mSurfaceTexture)
        }
        mVideoExecutor!!.setSurface(mSurface!!)
        mVideoExecutor!!.prepareAsync()
        mCurrentState = STATE_PREPARING
        mController!!.onPlayStateChanged(mCurrentState)
        Log.d(TAG, "STATE_PREPARING")
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return mSurfaceTexture == null
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    /**
     * 全屏，将mContainer(内部包含mTextureView和mController)从当前容器中移除，并添加到android.R.content中.
     * 切换横屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期
     */
    override fun enterFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) return

        // 隐藏ActionBar、状态栏，并横屏
        VideoHandleUtils.hideActionBar(mContext)
        VideoHandleUtils.scanForActivity(mContext)!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val contentView = VideoHandleUtils.scanForActivity(mContext)!!
                .findViewById<ViewGroup>(android.R.id.content)
        if (mCurrentMode == MODE_TINY_WINDOW) {
            contentView.removeView(mContainer)
        } else {
            this.removeView(mContainer)
        }
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        contentView.addView(mContainer, params)

        mCurrentMode = MODE_FULL_SCREEN
        mController!!.onPlayModeChanged(mCurrentMode)
        Log.d(TAG, "MODE_FULL_SCREEN")
    }

    /**
     * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
     * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期.
     *
     * @return true退出全屏.
     */
    override fun exitFullScreen(): Boolean {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            VideoHandleUtils.showActionBar(mContext)
            VideoHandleUtils.scanForActivity(mContext)!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            val contentView = VideoHandleUtils.scanForActivity(mContext)!!
                    .findViewById<ViewGroup>(android.R.id.content)
            contentView.removeView(mContainer)
            val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            this.addView(mContainer, params)

            mCurrentMode = MODE_NORMAL
            mController!!.onPlayModeChanged(mCurrentMode)
            Log.d(TAG, "MODE_NORMAL")
            return true
        }
        return false
    }

    /**
     * 进入小窗口播放，小窗口播放的实现原理与全屏播放类似。
     */
    override fun enterTinyWindow() {
        if (mCurrentMode == MODE_TINY_WINDOW) return
        this.removeView(mContainer)

        val contentView = VideoHandleUtils.scanForActivity(mContext)!!
                .findViewById<ViewGroup>(android.R.id.content)
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        val params = FrameLayout.LayoutParams(
                (VideoHandleUtils.getScreenWidth(mContext) * 0.6f).toInt(),
                (VideoHandleUtils.getScreenWidth(mContext).toFloat() * 0.6f * 9f / 16f).toInt())
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.rightMargin = VideoHandleUtils.dp2px(mContext, 8f)
        params.bottomMargin = VideoHandleUtils.dp2px(mContext, 8f)

        contentView.addView(mContainer, params)

        mCurrentMode = MODE_TINY_WINDOW
        mController!!.onPlayModeChanged(mCurrentMode)
        Log.d(TAG, "MODE_TINY_WINDOW")
    }

    /**
     * 退出小窗口播放
     */
    override fun exitTinyWindow(): Boolean {
        if (mCurrentMode == MODE_TINY_WINDOW) {
            val contentView = VideoHandleUtils.scanForActivity(mContext)!!
                    .findViewById<ViewGroup>(android.R.id.content)
            contentView.removeView(mContainer)
            val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            this.addView(mContainer, params)

            mCurrentMode = MODE_NORMAL
            mController!!.onPlayModeChanged(mCurrentMode)
            Log.d(TAG, "MODE_NORMAL")
            return true
        }
        return false
    }

    override fun releasePlayer() {
        if (mAudioManager != null) {
            mAudioManager!!.abandonAudioFocus(null)
            mAudioManager = null
        }
        if (mVideoExecutor != null) {
            mVideoExecutor!!.release()
            mVideoExecutor = null

        }
        mContainer.removeView(mTextureView)
        if (mSurface != null) {
            mSurface!!.release()
            mSurface = null
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture!!.release()
            mSurfaceTexture = null
        }
        mCurrentState = STATE_IDLE
    }

    override fun release() {
        // 保存播放位置
        if (isPlaying() || isBufferingPlaying() || isBufferingPaused() || isPaused()) {
            VideoHandleUtils.savePlayPosition(mContext, mUrl!!, getCurrentPosition())
        } else if (isCompleted()) {
            VideoHandleUtils.savePlayPosition(mContext, mUrl!!, 0)
        }
        // 退出全屏或小窗口
        if (isFullScreen()) {
            exitFullScreen()
        }
        if (isTinyWindow()) {
            exitTinyWindow()
        }
        mCurrentMode = MODE_NORMAL

        // 释放播放器
        releasePlayer()

        // 恢复控制器
        if (mController != null) {
            mController!!.reset()
        }
        Runtime.getRuntime().gc()
    }


    private val mOnPreparedListener = object : IVideoExecutor.OnPreparedListener {
        override fun onPrepared(executor: IVideoExecutor) {
            mCurrentState = STATE_PREPARED
            mController!!.onPlayStateChanged(mCurrentState)
            Log.d(TAG, "onPrepared ——> STATE_PREPARED")
            executor.start()
            // 从上次的保存位置播放
            if (continueFromLastPosition) {
                val savedPlayPosition = VideoHandleUtils.getSavedPlayPosition(mContext, mUrl!!)
                executor.seekTo(savedPlayPosition)
            }
            // 跳到指定位置播放
            if (skipToPosition != 0L) {
                executor.seekTo(skipToPosition)
            }
        }
    }

    private val mOnVideoSizeChangedListener = object : IVideoExecutor.OnVideoSizeChangedListener {

        override fun onVideoSizeChanged(executor: IVideoExecutor, width: Int, height: Int, sar_num: Int, sar_den: Int) {
            mTextureView!!.adaptVideoSize(width, height)
            Log.d(TAG, "onVideoSizeChanged ——> width：$width， height：$height")
        }
    }

    private val mOnCompletionListener = object : IVideoExecutor.OnCompletionListener {

        override fun onCompletion(executor: IVideoExecutor) {
            mCurrentState = STATE_COMPLETED
            mController!!.onPlayStateChanged(mCurrentState)
            Log.d(TAG, "onCompletion ——> STATE_COMPLETED")
            // 清除屏幕常亮
            mContainer.keepScreenOn = false
        }
    }

    private val mOnErrorListener = object : IVideoExecutor.OnErrorListener {

        override fun onError(executor: IVideoExecutor, what: Int, extra: Int): Boolean {
            // 直播流播放时去调用mediaPlayer.getDuration会导致-38和-2147483648错误，忽略该错误
            if (what != -38) {
                mCurrentState = STATE_ERROR
                mController!!.onPlayStateChanged(mCurrentState)
                Log.d(TAG, "onError ——> STATE_ERROR ———— what：$what, extra: $extra")
            }
            return true
        }
    }

    private val mOnInfoListener = object : IVideoExecutor.OnInfoListener {

        override fun onInfo(executor: IVideoExecutor, what: Int, extra: Int): Boolean {
            if (what == PlayerConstant.MEDIA_INFO_VIDEO_RENDERING_START) {
                // 播放器开始渲染
                mCurrentState = STATE_PLAYING
                mController!!.onPlayStateChanged(mCurrentState)
                Log.d(TAG, "onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING")
            } else if (what == PlayerConstant.MEDIA_INFO_BUFFERING_START) {
                // MediaPlayer暂时不播放，以缓冲更多的数据
                if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_BUFFERING_PAUSED
                    Log.d(TAG, "onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED")
                } else {
                    mCurrentState = STATE_BUFFERING_PLAYING
                    Log.d(TAG, "onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING")
                }
                mController!!.onPlayStateChanged(mCurrentState)
            } else if (what == PlayerConstant.MEDIA_INFO_BUFFERING_END) {
                // 填充缓冲区后，MediaPlayer恢复播放/暂停
                if (mCurrentState == STATE_BUFFERING_PLAYING) {
                    mCurrentState = STATE_PLAYING
                    mController!!.onPlayStateChanged(mCurrentState)
                    Log.d(TAG, "onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING")
                }
                if (mCurrentState == STATE_BUFFERING_PAUSED) {
                    mCurrentState = STATE_PAUSED
                    mController!!.onPlayStateChanged(mCurrentState)
                    Log.d(TAG, "onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED")
                }
            } else if (what == PlayerConstant.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
                // 视频旋转了extra度，需要恢复
                if (mTextureView != null) {
                    mTextureView!!.rotation = extra.toFloat()
                    Log.d(TAG, "视频旋转角度：$extra")
                }
            } else if (what == PlayerConstant.MEDIA_INFO_NOT_SEEK) {
                Log.d(TAG, "视频不能seekTo，为直播视频")
            } else {
                Log.d(TAG, "onInfo ——> what：$what")
            }
            return true
        }
    }

    private val mOnBufferingUpdateListener = object : IVideoExecutor.OnBufferingUpdateListener {

        override fun onBufferingUpdate(executor: IVideoExecutor, percent: Int) {
            mBufferPercentage = percent
        }
    }
}
