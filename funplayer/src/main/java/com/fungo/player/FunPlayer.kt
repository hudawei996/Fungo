package com.fungo.player

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.AssetFileDescriptor
import android.graphics.*
import android.media.AudioManager
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.danikula.videocache.CacheListener
import com.danikula.videocache.HttpProxyCacheServer
import com.fungo.player.controller.BasePlayerController
import com.fungo.player.controller.IPlayerController
import com.fungo.player.media.IPlayer
import com.fungo.player.media.IjkPlayer
import com.fungo.player.listener.OnPlayerListener
import com.fungo.player.utils.*
import com.fungo.player.utils.PlayerConstant.LANDSCAPE
import com.fungo.player.utils.PlayerConstant.MODE_FULL_SCREEN
import com.fungo.player.utils.PlayerConstant.MODE_NORMAL
import com.fungo.player.utils.PlayerConstant.MODE_TINY_WINDOW
import com.fungo.player.utils.PlayerConstant.NETWORK_MOBILE
import com.fungo.player.utils.PlayerConstant.NO_NETWORK
import com.fungo.player.utils.PlayerConstant.PORTRAIT
import com.fungo.player.utils.PlayerConstant.REVERSE_LANDSCAPE
import com.fungo.player.utils.PlayerConstant.SCREEN_SCALE_DEFAULT
import com.fungo.player.utils.PlayerConstant.STATE_BUFFERED
import com.fungo.player.utils.PlayerConstant.STATE_BUFFERING
import com.fungo.player.utils.PlayerConstant.STATE_ERROR
import com.fungo.player.utils.PlayerConstant.STATE_IDLE
import com.fungo.player.utils.PlayerConstant.STATE_PAUSED
import com.fungo.player.utils.PlayerConstant.STATE_PLAYBACK_COMPLETED
import com.fungo.player.utils.PlayerConstant.STATE_PLAYING
import com.fungo.player.utils.PlayerConstant.STATE_PREPARED
import com.fungo.player.utils.PlayerConstant.STATE_PREPARING
import com.fungo.player.widget.ResizeSurfaceView
import com.fungo.player.widget.ResizeTextureView
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.lang.ref.WeakReference

/**
 * @author Pinger
 * @since 18-6-13 下午8:40
 * 视频播放器
 */

open class FunPlayer : FrameLayout, IPlayerController {

    var mPlayer: IPlayer? = null//播放器
    private var mPlayerController: BasePlayerController? = null//控制器
    private var mPlayerListener: OnPlayerListener? = null
    private var bufferPercentage: Int = 0//缓冲百分比
    private var isMute: Boolean = false//是否静音

    private var mCurrentUrl: String? = null//当前播放视频的地址
    private var mHeaders: Map<String, String>? = null//当前视频地址的请求头
    private var mAssetFileDescriptor: AssetFileDescriptor? = null//assets文件
    private var mCurrentPosition: Long = 0//当前正在播放视频的位置

    private var mCurrentPlayState = STATE_IDLE
    private var mCurrentPlayMode = MODE_NORMAL

    private var mAudioManager: AudioManager? = null//系统音频管理器
    private var mAudioFocusHelper: AudioFocusHelper? = null

    private var currentOrientation = 0

    private var isLockFullScreen: Boolean = false//是否锁定屏幕
    // 播放器配置
    private var mConfig: PlayerConfig = PlayerConfig.Builder().build()
    private var mCacheServer: HttpProxyCacheServer? = null

    private var mSurfaceView: ResizeSurfaceView? = null
    private var mTextureView: ResizeTextureView? = null
    private var mSurfaceTexture: SurfaceTexture? = null
    private lateinit var mContainer: FrameLayout
    private var mCurrentScreenScale = SCREEN_SCALE_DEFAULT


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        mContainer = FrameLayout(context)
        mContainer.setBackgroundColor(Color.BLACK)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        this.addView(mContainer, params)
    }


    private fun initPlayer() {
        if (mPlayer == null) {
            mPlayer = if (mConfig.player != null) {
                mConfig.player
            } else {
                IjkPlayer(context)
            }
            // 初始化播放器
            mPlayer?.initPlayer()
            // 设置播放器的各种监听事件
            mPlayer?.setOnPreparedListener(onPreparedListener)
            mPlayer?.setOnCompletionListener(onCompletionListener)
            mPlayer?.setOnBufferingUpdateListener(onBufferingUpdateListener)
            mPlayer?.setOnVideoSizeChangedListener(onVideoSizeChangedListener)
            mPlayer?.setOnErrorListener(onErrorListener)
            mPlayer?.setOnInfoListener(onInfoListener)
            // 设置软硬编解码
            mPlayer?.setEnableMediaCodec(mConfig.enableMediaCodec)
            mPlayer?.setLooping(mConfig.isLooping)
        }
        addDisplay()
    }

    private fun startPrepare(needReset: Boolean) {

        PlayerLogUtils.d("-------> startPrepare")

        if (TextUtils.isEmpty(mCurrentUrl) && mAssetFileDescriptor == null) return
        if (needReset) mPlayer?.reset()
        if (mAssetFileDescriptor != null) {
            mPlayer?.setDataSource(mAssetFileDescriptor!!)
        } else if (mConfig.isCache) {
            mCacheServer = getCacheServer()
            val proxyPath = mCacheServer?.getProxyUrl(mCurrentUrl)
            mCacheServer?.registerCacheListener(cacheListener, mCurrentUrl)
            if (mCacheServer?.isCached(mCurrentUrl) == true) {
                bufferPercentage = 100
            }
            mPlayer?.setDataSource(proxyPath, mHeaders)
        } else {
            mPlayer?.setDataSource(mCurrentUrl, mHeaders)
        }
        mPlayer?.prepareAsync()
        onPlayStateChanged(STATE_PREPARING)
        onPlayModeChanged(if (isFullScreen()) MODE_FULL_SCREEN else MODE_NORMAL)
    }


    fun addDisplay() {
        if (mConfig.usingSurfaceView) {
            addSurfaceView()
        } else {
            addTextureView()
        }
    }

    /**
     * 添加SurfaceView
     */
    private fun addSurfaceView() {
        mContainer.removeView(mSurfaceView)
        mSurfaceView = ResizeSurfaceView(context)
        val surfaceHolder = mSurfaceView?.holder
        surfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {}

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                mPlayer?.setDisplay(holder)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })
        surfaceHolder?.setFormat(PixelFormat.RGBA_8888)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        mContainer.addView(mSurfaceView, 0, params)
    }

    /**
     * 添加TextureView
     */
    private fun addTextureView() {
        mContainer.removeView(mTextureView)
        mSurfaceTexture = null
        mTextureView = ResizeTextureView(context)
        mTextureView?.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
                if (mSurfaceTexture != null) {
                    mTextureView?.surfaceTexture = mSurfaceTexture
                } else {
                    mSurfaceTexture = surfaceTexture
                    mPlayer?.setSurface(Surface(surfaceTexture))
                }
            }

            override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {}

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                return mSurfaceTexture == null
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
        }
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        mContainer.addView(mTextureView, 0, params)
    }


    /**
     * 播放状态改变
     */
    private fun onPlayStateChanged(playState: Int) {
        mCurrentPlayState = playState
        mPlayerController?.onPlayStateChanged(playState)
    }

    /**
     * 播放模式改变
     */
    private fun onPlayModeChanged(playMode: Int) {
        mCurrentPlayMode = playMode
        mPlayerController?.onPlayModeChanged(playMode)
    }

    /**
     * 第一次播放
     */
    fun startPlay() {
        PlayerLogUtils.d("-------> startPlay")

        if (mConfig.addToPlayerManager) {
            FunPlayerManager.instance.releasePlayer()
            FunPlayerManager.instance.setCurrentVideoPlayer(this)
        }
        if (checkNetwork()) return

        if (!mConfig.disableAudioFocus) {
            mAudioManager = context.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioFocusHelper = AudioFocusHelper(WeakReference(this),WeakReference<AudioManager>(mAudioManager) )
        }
        if (mConfig.savingProgress) {
            mCurrentPosition = PlayerProgressUtils.getSavedProgress(mCurrentUrl)
        }
        if (mConfig.autoRotate)
            orientationEventListener?.enable()
        initPlayer()
        startPrepare(false)
    }


    private fun checkNetwork(): Boolean {
        if (!PlayerNetWorkUtils.isNetWorkAvailable(context)) {
            mPlayerController?.showNetWorkState(NO_NETWORK)
            return true
        } else if (PlayerNetWorkUtils.isMobileNetWork(context) && !PlayerConstant.IS_PLAY_ON_MOBILE_NETWORK) {
            mPlayerController?.showNetWorkState(NETWORK_MOBILE)
            return true
        }
        return false
    }

    /**
     * 开始播放
     */
    override fun start() {
        PlayerLogUtils.d("-------> start")

        if (mCurrentPlayState == STATE_IDLE) {
            startPlay()
        } else if (isInPlaybackState()) {
            startInPlaybackState()
        }
        keepScreenOn = true
        mAudioFocusHelper?.requestFocus()
    }

    /**
     * 播放状态下开始播放
     */
    private fun startInPlaybackState() {
        PlayerLogUtils.d("-------> startInPlaybackState")

        mPlayer?.start()
        onPlayStateChanged(STATE_PLAYING)
        mPlayerListener?.onVideoStarted()
    }

    /**
     * 是否处于播放状态
     */
    private fun isInPlaybackState(): Boolean {
        return (mPlayer != null
                && mCurrentPlayState != STATE_ERROR
                && mCurrentPlayState != STATE_IDLE
                && mCurrentPlayState != STATE_PREPARING
                && mCurrentPlayState != STATE_PLAYBACK_COMPLETED)
    }

    /**
     * 继续播放
     */
    fun resume() {
        if (isInPlaybackState() && mPlayer?.isPlaying() == false) {
            mPlayer?.start()
            onPlayStateChanged(STATE_PLAYING)
            keepScreenOn = true
            mAudioFocusHelper?.requestFocus()
            mPlayerListener?.onVideoStarted()
        }
    }

    override fun pause() {
        if (isPlaying()) {
            mPlayer?.pause()
            onPlayStateChanged(STATE_PAUSED)
            keepScreenOn = false
            mAudioFocusHelper?.abandonFocus()
            mPlayerListener?.onVideoPaused()
        }
    }


    /**
     * 停止播放
     */
    fun stopPlayback() {
        if (mConfig.savingProgress && isInPlaybackState())
            PlayerProgressUtils.saveProgress(mCurrentUrl, mCurrentPosition)
        if (mPlayer != null) {
            mPlayer?.stop()
            onPlayStateChanged(STATE_IDLE)
            if (mAudioFocusHelper != null)
                mAudioFocusHelper!!.abandonFocus()
            keepScreenOn = false
        }
        onPlayStopped()
    }


    /**
     * 释放播放器
     */
    fun release() {
        if (mConfig.savingProgress && isInPlaybackState())
            PlayerProgressUtils.saveProgress(mCurrentUrl, mCurrentPosition)
        if (mPlayer != null) {
            mPlayer?.release()
            mPlayer = null
            onPlayStateChanged(STATE_IDLE)
            if (mAudioFocusHelper != null)
                mAudioFocusHelper!!.abandonFocus()
            keepScreenOn = false
        }
        onPlayStopped()

        mContainer.removeView(mTextureView)
        mContainer.removeView(mSurfaceView)
        mSurfaceTexture?.release()
        mSurfaceTexture = null
        mCurrentScreenScale = SCREEN_SCALE_DEFAULT
    }

    private fun onPlayStopped() {
        orientationEventListener?.disable()
        orientationEventListener = null
        mCacheServer?.unregisterCacheListener(cacheListener)
        isLockFullScreen = false
        mCurrentPosition = 0
    }

    override fun getDuration(): Long {
        return if (isInPlaybackState()) {
            mPlayer?.getDuration() ?: 0
        } else 0
    }

    override fun getCurrentPosition(): Long {
        if (isInPlaybackState()) {
            mCurrentPosition = mPlayer?.getCurrentPosition() ?: 0
            return mCurrentPosition
        }
        return 0
    }

    override fun seekTo(position: Long) {
        if (isInPlaybackState()) {
            mPlayer?.seekTo(position)
        }
    }

    override fun isPlaying(): Boolean {
        return isInPlaybackState() && mPlayer?.isPlaying() ?: false
    }


    override fun isPaused(): Boolean {
        return mCurrentPlayState== STATE_PAUSED
    }

    override fun isIdle(): Boolean {
        return mCurrentPlayState== STATE_IDLE
    }

    override fun isPreparing(): Boolean {
        return mCurrentPlayState== STATE_PREPARING
    }

    override fun isPrepared(): Boolean {
        return mCurrentPlayState== STATE_PREPARED
    }

    override fun isBuffering(): Boolean {
        return mCurrentPlayState== STATE_BUFFERING
    }

    override fun isBuffered(): Boolean {
        return mCurrentPlayState== STATE_BUFFERED
    }

    override fun isError(): Boolean {
        return mCurrentPlayState== STATE_ERROR
    }

    override fun isCompleted(): Boolean {
        return mCurrentPlayState== STATE_PLAYBACK_COMPLETED
    }

    override fun isTinyWindow(): Boolean {
        return mCurrentPlayMode== MODE_TINY_WINDOW
    }

    override fun isNormal(): Boolean {
        return mCurrentPlayMode== MODE_NORMAL
    }

    override fun getBufferPercentage(): Int {
        return if (mPlayer != null) bufferPercentage else 0
    }

    override fun isFullScreen(): Boolean {
        return mCurrentPlayMode == MODE_FULL_SCREEN
    }

    override fun enterFullScreen() {
        if (mPlayerController == null) return
        if (isFullScreen()) return
        val activity = PlayerWindowUtils.scanForActivity(context) ?: return
        PlayerWindowUtils.hideSystemBar(context)
        this.removeView(mContainer)
        val contentView = activity
                .findViewById<ViewGroup>(android.R.id.content)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        contentView.addView(mContainer, params)
        orientationEventListener?.enable()
        onPlayModeChanged(MODE_FULL_SCREEN)
    }

    override fun exitFullScreen() {
        if (mPlayerController == null) return
        if (!isFullScreen()) return
        val activity = PlayerWindowUtils.scanForActivity(context) ?: return
        if (!mConfig.autoRotate) orientationEventListener?.disable()
        PlayerWindowUtils.showSystemBar(context)
        val contentView = activity
                .findViewById<ViewGroup>(android.R.id.content)
        contentView.removeView(mContainer)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        this.addView(mContainer, params)
        onPlayModeChanged(MODE_NORMAL)
    }


    override fun enterTinyWindow() {
        if (mPlayerController == null) return
        if (mCurrentPlayMode == MODE_TINY_WINDOW) return

        val activity = PlayerWindowUtils.scanForActivity(context) ?: return
        orientationEventListener?.enable()

        val contentView = activity
                .findViewById<ViewGroup>(android.R.id.content)
        this.removeView(mContainer)

        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        val params = FrameLayout.LayoutParams(
                (PlayerUtils.getScreenWidth(context) * 0.6f).toInt(),
                (PlayerUtils.getScreenWidth(context).toFloat() * 0.6f * 9f / 16f).toInt())
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.rightMargin = PlayerUtils.dp2px(context, 8f)
        params.bottomMargin = PlayerUtils.dp2px(context, 8f)
        contentView.addView(mContainer, params)
        onPlayModeChanged(MODE_TINY_WINDOW)
    }

    override fun exitTinyWindow() {
        if (mCurrentPlayMode == MODE_TINY_WINDOW) {
            val activity = PlayerWindowUtils.scanForActivity(context) ?: return
            if (!mConfig.autoRotate) orientationEventListener?.disable()

            val contentView = activity
                    .findViewById<ViewGroup>(android.R.id.content)
            contentView.removeView(mContainer)
            val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            this.addView(mContainer, params)
            onPlayModeChanged(MODE_NORMAL)
        }
    }


    /**
     * 设置静音或者取消静音
     */
    override fun setMute() {
        isMute = if (isMute) {
            mPlayer?.setVolume(1f, 1f)
            false
        } else {
            mPlayer?.setVolume(0f, 0f)
            true
        }
    }

    override fun isMute(): Boolean {
        return isMute
    }

    override fun setLock(isLocked: Boolean) {
        this.isLockFullScreen = isLocked
    }

    override fun setScreenScale(screenScale: Int) {
        this.mCurrentScreenScale = screenScale
        mSurfaceView?.setScreenScale(screenScale)
        mTextureView?.setScreenScale(screenScale)
    }

    override fun retry() {
        PlayerLogUtils.d("-------> retry")
        mCurrentPosition = 0
        addDisplay()
        startPrepare(true)
    }

    override fun setSpeed(speed: Float) {
        if (isInPlaybackState()) {
            mPlayer?.setSpeed(speed)
        }
    }

    override fun getTcpSpeed(): Long {
        return mPlayer?.getTcpSpeed() ?: 0
    }

    override fun setMirrorRotation(enable: Boolean) {
        if (mTextureView != null) {
            val transform = Matrix()
            if (enable) {
                transform.setScale(-1f, 1f, (mTextureView!!.width / 2).toFloat(), 0f)
            } else {
                transform.setScale(1f, 1f, (mTextureView!!.width / 2).toFloat(), 0f)
            }
            mTextureView!!.setTransform(transform)
            mTextureView!!.invalidate()
        }

    }

    override fun doScreenShot(): Bitmap? {
        return if (mTextureView != null) {
            mTextureView?.bitmap
        } else null
    }


    /**
     * 设置视频地址
     */
    fun setUrl(url: String) {
        this.mCurrentUrl = url
    }

    /**
     * 设置包含请求头信息的视频地址
     *
     * @param url     视频地址
     * @param headers 请求头
     */
    private fun setUrl(url: String, headers: Map<String, String>) {
        mCurrentUrl = url
        mHeaders = headers
    }

    /**
     * 用于播放assets里面的视频文件
     */
    fun setAssetFileDescriptor(fd: AssetFileDescriptor) {
        this.mAssetFileDescriptor = fd
    }

    /**
     * 一开始播放就seek到预先设置好的位置
     */
    fun skipPositionWhenPlay(position: Int) {
        this.mCurrentPosition = position.toLong()
    }


    fun setConfig(config: PlayerConfig) {
        this.mConfig = config
    }

    /**
     * 获取当前播放器的状态
     */
    fun getCurrentPlayerState(): Int {
        return mCurrentPlayMode
    }

    /**
     * 获取当前的播放状态
     */
    fun getCurrentPlayState(): Int {
        return mCurrentPlayState
    }


    /**
     * 设置播放器监听，用于外部监听播放器的各种状态
     */
    fun setPlayerListener(listener: OnPlayerListener) {
        this.mPlayerListener = listener
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && isFullScreen()) {
            PlayerWindowUtils.hideSystemBar(context)
        }
    }


    /**
     * 设置控制器，设置的控制必须继承[BasePlayerController]
     * 设置控制器的同时，将播放器对象传入控制器
     * @param controller 控制器
     */
    fun setController(controller: BasePlayerController) {
        mContainer.removeView(mPlayerController)
        mPlayerController = controller
        controller.setPlayer(this)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        mContainer.addView(mPlayerController, params)
    }


    /**
     * 改变返回键逻辑，用于activity
     */
    fun onBackPressed(): Boolean {
        return mPlayerController?.onBackPressed() ?: false
    }

    /**
     * 缓存监听
     */
    private val cacheListener = CacheListener { _, _, percentsAvailable ->
        bufferPercentage = percentsAvailable
    }

    /**
     * 视频播放前的准备监听
     */
    private val onPreparedListener: IPlayer.OnPreparedListener = object : IPlayer.OnPreparedListener {
        override fun onPrepared() {
            onPlayStateChanged(STATE_PREPARED)
            if (mCurrentPosition > 0) {
                seekTo(mCurrentPosition)
            }
            mPlayerListener?.onPrepared()
        }

    }

    /**
     * 视频加载完成监听
     */
    private val onCompletionListener: IPlayer.OnCompletionListener = object : IPlayer.OnCompletionListener {
        override fun onCompletion() {
            onPlayStateChanged(STATE_PLAYBACK_COMPLETED)
            keepScreenOn = false
            mCurrentPosition = 0
            mPlayerListener?.onComplete()
        }

    }

    /**
     * 视频缓冲监听
     */
    private val onBufferingUpdateListener: IPlayer.OnBufferingUpdateListener = object : IPlayer.OnBufferingUpdateListener {
        override fun onBufferingUpdate(percent: Int) {
            if (!mConfig.isCache) bufferPercentage = percent
        }
    }


    /**
     * 视频大小改变监听监听
     */
    private val onVideoSizeChangedListener: IPlayer.OnVideoSizeChangedListener = object : IPlayer.OnVideoSizeChangedListener {
        override fun onVideoSizeChanged(width: Int, height: Int) {
            if (mConfig.usingSurfaceView) {
                mSurfaceView?.setScreenScale(mCurrentScreenScale)
                mSurfaceView?.setVideoSize(width, height)
            } else {
                mTextureView?.setScreenScale(mCurrentScreenScale)
                mTextureView?.setVideoSize(width, height)
            }
        }

    }


    /**
     * 视频播放异常监听
     */
    private val onErrorListener: IPlayer.OnErrorListener = object : IPlayer.OnErrorListener {
        override fun onError(): Boolean {
            onPlayStateChanged(STATE_ERROR)
            mPlayerListener?.onError()
            return false
        }
    }


    /**
     * 视频加载信息变化监听
     */
    private val onInfoListener: IPlayer.OnInfoListener = object : IPlayer.OnInfoListener {
        override fun onInfo(what: Int, extra: Int): Boolean {
            when (what) {
                IMediaPlayer.MEDIA_INFO_BUFFERING_START -> onPlayStateChanged(STATE_BUFFERING)
                IMediaPlayer.MEDIA_INFO_BUFFERING_END -> onPlayStateChanged(STATE_BUFFERED)
                IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START // 视频开始渲染
                -> {
                    onPlayStateChanged(STATE_PLAYING)
                    mPlayerListener?.onVideoStarted()
                    if (windowVisibility != View.VISIBLE) pause()
                }
                IjkMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED -> mTextureView?.rotation = extra.toFloat()
            }
            mPlayerListener?.onInfo(what, extra)
            return false
        }
    }

    /**
     * 加速度传感器监听
     * 用于自动旋转屏幕
     */
    private var orientationEventListener: OrientationEventListener? = object : OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            if (mPlayerController == null) return
            val activity = PlayerWindowUtils.scanForActivity(mPlayerController!!.context) ?: return
            when {
            // 屏幕顶部朝上
                orientation >= 340 ->
                    onOrientationPortrait(activity)
            // 屏幕左边朝上
                orientation in 260..280 ->
                    onOrientationLandscape(activity)
            // 屏幕右边朝上
                orientation in 70..90 ->
                    onOrientationReverseLandscape(activity)
            }
        }
    }


    /**
     * 竖屏
     */
    protected fun onOrientationPortrait(activity: Activity) {
        if (isLockFullScreen || !mConfig.autoRotate || currentOrientation == PORTRAIT)
            return
        if ((currentOrientation == LANDSCAPE || currentOrientation == REVERSE_LANDSCAPE) && !isFullScreen()) {
            currentOrientation = PORTRAIT
            return
        }
        currentOrientation = PORTRAIT
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        exitFullScreen()
    }

    /**
     * 横屏
     */
    protected fun onOrientationLandscape(activity: Activity) {
        if (currentOrientation == LANDSCAPE) return
        if (currentOrientation == PORTRAIT && isFullScreen()) {
            currentOrientation = LANDSCAPE
            return
        }
        currentOrientation = LANDSCAPE
        if (!isFullScreen()) {
            enterFullScreen()
        }
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 反向横屏
     */
    protected fun onOrientationReverseLandscape(activity: Activity) {
        if (currentOrientation == REVERSE_LANDSCAPE) return
        if (currentOrientation == PORTRAIT && isFullScreen()) {
            currentOrientation = REVERSE_LANDSCAPE
            return
        }
        currentOrientation = REVERSE_LANDSCAPE
        if (!isFullScreen()) {
            enterFullScreen()
        }
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
    }


    private fun getCacheServer(): HttpProxyCacheServer {
        return PlayerCacheUtils.getProxy(context.applicationContext)
    }

}