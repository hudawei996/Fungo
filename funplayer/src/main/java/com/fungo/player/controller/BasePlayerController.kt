package com.fungo.player.controller

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.fungo.player.FunPlayer
import com.fungo.player.R
import com.fungo.player.utils.PlayerConstant
import com.fungo.player.utils.PlayerConstant.NETWORK_MOBILE
import com.fungo.player.utils.PlayerUtils
import com.fungo.player.utils.PlayerWindowUtils
import com.fungo.player.widget.ControlCenterView
import com.fungo.player.widget.ControlStateView
import java.util.*

/**
 * @author Pinger
 * @since 18-6-13 下午8:36
 * 播放器控制器基类，抽取常用方法并且重写播放器
 */

abstract class BasePlayerController : FrameLayout {

    private var mPosition: Long = 0
    private var mBrightness: Float = 0f
    private var mStreamVolume: Int = 0
    private var mNeedSeek: Boolean =false
    private var mGestureEnabled: Boolean = false
    private var mTopBottomVisible: Boolean = false
    private var isLive: Boolean = false
    private var isLocked: Boolean = false

    private lateinit var mStateView: ControlStateView
    private lateinit var mCenterView: ControlCenterView

    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mAudioManager: AudioManager

    private lateinit var mPlayer: IPlayerController

    private var mUpdateProgressTimer: Timer? = null
    private var mUpdateProgressTimerTask: TimerTask? = null
    private var mDismissTopBottomCountDownTimer: CountDownTimer? = null


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        LayoutInflater.from(context).inflate(getLayoutId(), this)
        mStateView = ControlStateView(context)
        addView(mStateView)
        mCenterView = ControlCenterView(context)
        addView(mCenterView)
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mGestureDetector = GestureDetector(context, ControlGestureListener())
        this.setOnTouchListener { _, event -> mGestureDetector.onTouchEvent(event) }
        isClickable = true
        isFocusable = true
        initView()
    }


    /**
     * 设置播放器
     * 在设置控制器时会调用此方法将播放器对象传入控制器，使用控制球必须先设置控制器，不然会出现Player非空异常
     * @param player 播放器对象
     */
    fun setPlayer(player: FunPlayer) {
        this.mPlayer = player
    }


    /**
     * 展示网络异常状态
     */
    fun showNetWorkState(netState: Int) {
        if (netState == NETWORK_MOBILE) {
            mStateView.showNetMobile(OnClickListener {
                mStateView.hideState()
                PlayerConstant.IS_PLAY_ON_MOBILE_NETWORK = true
                mPlayer.start()
            })
        } else {
            mStateView.showNetError(OnClickListener {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show()
            })
        }
    }


    /**
     * 暂停和播放
     * 如果当前是暂停就会执行播放，如果是播放就是执行暂停
     * 如果播放器在缓冲数据，则会直接返回
     */
    protected fun doPauseStart() {
        if (mPlayer.isBuffering()) return
        if (mPlayer.isPlaying()) {
            mPlayer.pause()
        } else {
            mPlayer.start()
        }
    }


    /**
     * 横竖屏切换
     * 如果当前是横屏则会切换到竖屏，如果是竖屏则会切换到横屏
     * 使用横竖屏一定要在Activity的清单文件中配置ConfigChange属性
     */
    protected fun doStartStopFullScreen() {
        if (mPlayer.isFullScreen()) {
            PlayerWindowUtils.scanForActivity(context)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            mPlayer.exitFullScreen()
        } else {
            PlayerWindowUtils.scanForActivity(context)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            mPlayer.enterFullScreen()
        }
    }

    /**
     * 上锁和解锁
     *　如果已经锁上了，则进行解锁，如果解锁了就上锁
     */
    protected fun doLockUnlock(lockView:View) {
        if (isLocked()) {
            setSelected(lockView,false)
            setTopBottomVisible(true)
            setGestureEnable(true)
            setLocked(false)
            PlayerUtils.showToast(context,R.string.player_unlock)
        } else {
            setSelected(lockView,true)
            setTopBottomVisible(false)
            setGestureEnable(false)
            setLocked(true)
            PlayerUtils.showToast(context,R.string.player_lock)
        }
        mPlayer.setLock(isLocked())
    }


    /***
     * 控制器的手势监听
     */
    protected inner class ControlGestureListener : GestureDetector.SimpleOnGestureListener() {

        private var mFirstTouch: Boolean = false
        private var mChangePosition: Boolean = false
        private var mChangeBrightness: Boolean = false
        private var mChangeVolume: Boolean = false

        override fun onDown(e: MotionEvent): Boolean {
            if (!isGestureEnable() || PlayerWindowUtils.isEdge(context, e)) return super.onDown(e)
            mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            mBrightness = PlayerWindowUtils.scanForActivity(context)?.window?.attributes?.screenBrightness ?: 0f
            mFirstTouch = true
            mChangePosition = false
            mChangeBrightness = false
            mChangeVolume = false
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            mTopBottomVisible = !mTopBottomVisible
            setTopBottomVisible(mTopBottomVisible)
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (!isGestureEnable() || PlayerWindowUtils.isEdge(context, e1)) return super.onScroll(e1, e2, distanceX, distanceY)
            val deltaX = e1.x - e2.x
            val deltaY = e1.y - e2.y
            if (mFirstTouch) {
                mChangePosition = Math.abs(distanceX) >= Math.abs(distanceY)
                if (!mChangePosition) {
                    if (e2.x > PlayerWindowUtils.getScreenHeight(context, false) / 2) {
                        mChangeBrightness = true
                    } else {
                        mChangeVolume = true
                    }
                }
                mFirstTouch = false
            }
            when {
                mChangePosition -> slideToChangePosition(deltaX)
                mChangeBrightness -> slideToChangeBrightness(deltaY)
                mChangeVolume -> slideToChangeVolume(deltaY)
            }
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (!isLocked()) doPauseStart()
            return true
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val detectedUp = event.action == MotionEvent.ACTION_UP
        if (!mGestureDetector.onTouchEvent(event) && detectedUp) {
            mCenterView.setCenterGone()
            if (mNeedSeek) {
                startUpdateProgressTimer()
                mPlayer.seekTo(mPosition)
                mNeedSeek = false
            }
        }
        return super.onTouchEvent(event)
    }


    /**
     * 滑动改变播放进度
     * @param deltaX 如果向左滑动，deltaX为正数,如果向右滑动，deltaX为负数
     * 先判断当前是不是直播视频，如果不是就去设置进度
     */
    private fun slideToChangePosition(deltaX: Float) {
        if (isLive()) {
            mNeedSeek = false
        }else{
            mNeedSeek = true
            setTopBottomVisible(false)
            val dx = -deltaX
            val isForward = deltaX<0
            val width = measuredWidth
            val duration = mPlayer.getDuration()
            val currentPosition = mPlayer.getCurrentPosition()
            var position = (dx / width * duration + currentPosition).toLong()
            if (position > duration) position = duration
            if (position < 0) position = 0
            mPosition = position
            mCenterView.showPositionChange(isForward,position,currentPosition,duration)
        }
    }


    /**
     * 滑动改变亮度
     * @param deltaY Y轴上滑动的距离
     */
    private fun slideToChangeBrightness(deltaY: Float) {
        setTopBottomVisible(false)
        val window = PlayerWindowUtils.scanForActivity(context)?.window
        val attributes = window?.attributes
        val height = measuredHeight
        if (mBrightness == -1.0f) mBrightness = 0.5f
        var brightness = deltaY * 2 / height * 1.0f + mBrightness
        if (brightness < 0) {
            brightness = 0f
        }
        if (brightness > 1.0f) brightness = 1.0f
        val percent = (brightness * 100).toInt()
        mCenterView.showBrightnessChange(percent)
        attributes?.screenBrightness = brightness
        window?.attributes = attributes
    }


    /**
     * 滑动改变音量
     * @param deltaY Y轴上滑动的距离
     */
    private fun slideToChangeVolume(deltaY: Float) {
        setTopBottomVisible(false)
        val streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val height = measuredHeight
        val deltaV = deltaY * 2 / height * streamMaxVolume
        var index = mStreamVolume + deltaV
        if (index > streamMaxVolume) index = streamMaxVolume.toFloat()
        if (index < 0) index = 0f
        val percent = (index / streamMaxVolume * 100).toInt()
        mCenterView.showVolumeChange(percent)
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index.toInt(), 0)
    }


    /**
     * 播放返回的处理，是否锁屏，是否全屏等等
     * 返回时需要把进度更新和悬浮栏更新移除，不然会内存泄露
     * @return 是否可以返回
     */
    fun onBackPressed(): Boolean {
        if (isLocked) {
            setTopBottomVisible(true)
            PlayerUtils.showToast(context,context.getString(R.string.player_unlock_first))
            return true
        }
        if (mPlayer.isFullScreen()) {
            PlayerWindowUtils.scanForActivity(context)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            mPlayer.exitFullScreen()
            return true
        }
        cancelUpdateProgressTimer()
        cancelDismissTopBottomTimer()
        return false
    }

    /**
     * 开启更新进度的计时器
     * 当播放开始时，开启播放进度计时，不断更新进度
     */
    protected open fun startUpdateProgressTimer() {
        cancelUpdateProgressTimer()
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = Timer()
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = object : TimerTask() {
                override fun run() {
                    this@BasePlayerController.post { updateProgress() }
                }
            }
        }
        mUpdateProgressTimer!!.schedule(mUpdateProgressTimerTask, 0, 1000)
    }

    /**
     * 取消更新进度的计时器
     * 当不在正常播放时，需要取消播放进度更新
     */
    protected open fun cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer!!.cancel()
            mUpdateProgressTimer = null
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask!!.cancel()
            mUpdateProgressTimerTask = null
        }
    }


    /**
     * 开启top、bottom自动消失的timer
     * 一般在播放器的播放状态改变时调用此方法
     * 调用[setTopBottomVisible]这个方法，并且设置的值为true是，会调用本方法
     */
    protected open fun startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer()
        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = object : CountDownTimer(5000, 5000) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    setTopBottomVisible(false)
                }
            }
        }
        mDismissTopBottomCountDownTimer?.start()
    }

    /**
     * 取消top、bottom自动消失的timer
     * 一般在播放器暂停，悬浮栏不需要消失时调用此方法
     */
    protected fun cancelDismissTopBottomTimer() {
        mDismissTopBottomCountDownTimer?.cancel()
    }


    // -------------控制器的API--------------

    /**
     * 获取当前控制器操作的播放器，可以控制播放的状态等
     * @return 播放器引用
     */
    open fun getPlayer():IPlayerController{
        return mPlayer
    }


    /**
     * 获取状态填充的View，包括加载中，加载完成，异常异常等多种状态
     * @return 返回填充的View
     */
    open fun getStateView():ControlStateView{
        return mStateView
    }


    /**
     * 设置是否可以手势滑动
     * @param enable true为可以，false为不可以
     */
    open fun setGestureEnable(enable:Boolean){
        this.mGestureEnabled = enable
    }


    /**
     * 是否可以手势操作，音量，亮度和进度控制
     * 直播时该值为[false]
     * @return 是否可以手势
     */
    open fun isGestureEnable():Boolean{
        return this.mGestureEnabled
    }


    /**
     * 判断是否锁定页面
     * @return 是否锁定
     */
    open fun isLocked():Boolean{
        return this.isLocked
    }


    /**
     * 设置是否锁定控制器，锁定后操作栏不会出现
     * @param isLocked 是否锁定
     */
    open fun setLocked(isLocked:Boolean){
        this.isLocked = isLocked
    }


    /**
     * 当前播放是否是直播
     * @return
     */
    open fun isLive():Boolean{
        return this.isLive
    }


    /**
     * 设置View可见
     * @param view 设置的View
     */
    open fun setVisible(view:View?){
        if (view!=null) {
            if (view.visibility!=View.VISIBLE) {
                view.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 设置View隐藏
     * @param view 设置的View
     */
    open  fun setGone(view:View?){
        if (view!=null) {
            if (view.visibility!=View.GONE) {
                view.visibility = View.GONE
            }
        }
    }

    /**
     * 设置View的选中状态，设置不同的图片
     * @param view 设置的View
     * @param selected 是否选中
     */
    open  fun setSelected(view:View?, selected:Boolean){
        if (view!=null) {
            view.isSelected = selected
        }
    }


    // -------------控制器子类实现--------------
    /**
     * 获取填充的View的id
     * @return 布局id
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 设置顶部和顶部的容器是否展示
     * @param visible 容器是否可见
     */
    open fun setTopBottomVisible(visible: Boolean) {

    }

    /**
     * 播放状态改变，控制View的展示和隐藏
     * @param playState 播放状态
     */
    open fun onPlayStateChanged(playState: Int) {

    }

    /**
     * 播放模式改变，全屏，悬浮窗播放等等
     * @param playMode 播放模式
     */
    open fun onPlayModeChanged(playMode: Int) {
    }


    /**
     * 更新进度条进度
     */
    open fun updateProgress() {

    }


    /**
     * 获取背景占位图
     * @return 背景图View
     */
   open fun getImage(): ImageView {
        return ImageView(context)
    }


    /**
     * 设置控制器的标题
     * @param title 标题
     */
    open fun setTitle(title:String ){
    }

}