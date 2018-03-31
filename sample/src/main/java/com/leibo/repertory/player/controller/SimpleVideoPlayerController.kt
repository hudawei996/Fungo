package com.leibo.repertory.player.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.CountDownTimer
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.leibo.baselib.player.IVideoPlayer
import com.leibo.baselib.player.VideoHandleUtils
import com.leibo.baselib.player.VideoPlayer
import com.leibo.baselib.player.controller.BaseVideoPlayerController
import com.leibo.repertory.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Pinger
 * @since 2017/12/28 0028 下午 7:14
 */

class SimpleVideoPlayerController(context: Context) : BaseVideoPlayerController(context), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private var mImage: ImageView? = null
    private var mCenterStart: ImageView? = null
    private var mUrl: String? = null

    private var mTop: LinearLayout? = null
    private var mBack: ImageView? = null
    private var mTitle: TextView? = null
    private var mBatteryTime: LinearLayout? = null
    private var mBattery: ImageView? = null
    private var mTime: TextView? = null

    private var mBottom: LinearLayout? = null
    private var mRestartPause: ImageView? = null
    private var mPosition: TextView? = null
    private var mDuration: TextView? = null
    private var mSeek: SeekBar? = null
    private var mFullScreen: ImageView? = null

    private var mLength: TextView? = null

    private var mLoading: LinearLayout? = null
    private var mLoadText: TextView? = null

    private var mChangePosition: LinearLayout? = null
    private var mChangePositionCurrent: TextView? = null
    private var mChangePositionProgress: ProgressBar? = null

    private var mChangeBrightness: LinearLayout? = null
    private var mChangeBrightnessProgress: ProgressBar? = null

    private var mChangeVolume: LinearLayout? = null
    private var mChangeVolumeProgress: ProgressBar? = null

    private var mError: LinearLayout? = null
    private var mRetry: TextView? = null

    private var mCompleted: LinearLayout? = null
    private var mReplay: TextView? = null
    private var mShare: TextView? = null

    private var topBottomVisible: Boolean = false
    private var mDismissTopBottomCountDownTimer: CountDownTimer? = null

    private var hasRegisterBatteryReceiver: Boolean = false // 是否已经注册了电池广播

    /**
     * 电池状态即电量变化广播接收器
     */
    private val mBatterReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN)
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                // 充电中
                mBattery!!.setImageResource(R.mipmap.ic_player_battery)
            } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
                // 充电完成
                mBattery!!.setImageResource(R.mipmap.ic_player_battery)
            } else {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
                val percentage = (level.toFloat() / scale * 100).toInt()
                when {
//                    percentage <= 10 -> mBattery!!.setImageResource(R.drawable.battery_10)
//                    percentage <= 20 -> mBattery!!.setImageResource(R.drawable.battery_20)
//                    percentage <= 50 -> mBattery!!.setImageResource(R.drawable.battery_50)
//                    percentage <= 80 -> mBattery!!.setImageResource(R.drawable.battery_80)
                    percentage <= 100 -> mBattery!!.setImageResource(R.mipmap.ic_player_battery)
                }
            }
        }
    }

    override fun initView() {
        LayoutInflater.from(mContext).inflate(R.layout.video_player_simple_controller, this, true)

        mCenterStart = findViewById(R.id.center_start)
        mImage = findViewById(R.id.image)

        mTop = findViewById<View>(R.id.top) as LinearLayout
        mBack = findViewById<View>(R.id.back) as ImageView
        mTitle = findViewById<View>(R.id.title) as TextView
        mBatteryTime = findViewById<View>(R.id.battery_time) as LinearLayout
        mBattery = findViewById<View>(R.id.battery) as ImageView
        mTime = findViewById<View>(R.id.time) as TextView

        mBottom = findViewById<View>(R.id.bottom) as LinearLayout
        mRestartPause = findViewById<View>(R.id.restart_or_pause) as ImageView
        mPosition = findViewById<View>(R.id.position) as TextView
        mDuration = findViewById<View>(R.id.duration) as TextView
        mSeek = findViewById<View>(R.id.seek) as SeekBar
        mFullScreen = findViewById<View>(R.id.full_screen) as ImageView
        mLength = findViewById<View>(R.id.length) as TextView

        mLoading = findViewById<View>(R.id.loading) as LinearLayout
        mLoadText = findViewById<View>(R.id.load_text) as TextView

        mChangePosition = findViewById<View>(R.id.change_position) as LinearLayout
        mChangePositionCurrent = findViewById<View>(R.id.change_position_current) as TextView
        mChangePositionProgress = findViewById<View>(R.id.change_position_progress) as ProgressBar

        mChangeBrightness = findViewById<View>(R.id.change_brightness) as LinearLayout
        mChangeBrightnessProgress = findViewById<View>(R.id.change_brightness_progress) as ProgressBar

        mChangeVolume = findViewById<View>(R.id.change_volume) as LinearLayout
        mChangeVolumeProgress = findViewById<View>(R.id.change_volume_progress) as ProgressBar

        mError = findViewById<View>(R.id.error) as LinearLayout
        mRetry = findViewById<View>(R.id.retry) as TextView

        mCompleted = findViewById<View>(R.id.completed) as LinearLayout
        mReplay = findViewById<View>(R.id.replay) as TextView
        mShare = findViewById<View>(R.id.share) as TextView

        mCenterStart!!.setOnClickListener(this)
        mBack!!.setOnClickListener(this)
        mRestartPause!!.setOnClickListener(this)
        mFullScreen!!.setOnClickListener(this)
        mRetry!!.setOnClickListener(this)
        mReplay!!.setOnClickListener(this)
        mShare!!.setOnClickListener(this)
        mSeek!!.setOnSeekBarChangeListener(this)
        this.setOnClickListener(this)
    }

    override fun setTitle(title: String) {
        mTitle!!.text = title
    }

    override fun setUrl(url: String) {
        mUrl = url
    }

    override fun imageView(): ImageView {
        return mImage!!
    }

    override fun setImage(@DrawableRes resId: Int) {
        mImage!!.setImageResource(resId)
    }

    override fun setImage(imageURl: String) {
        // TODO 加载网络图片
    }

    override fun setLength(length: Long) {
        mLength!!.text = VideoHandleUtils.formatTime(length)
    }

    override fun setVideoPlayer(videoPlayer: IVideoPlayer) {
        super.setVideoPlayer(videoPlayer)
        // 给播放器配置视频链接地址
        if (!TextUtils.isEmpty(mUrl)) {
            mVideoPlayer!!.setUp(mUrl!!)
        }
    }

    override fun onPlayStateChanged(playState: Int) {
        when (playState) {
            VideoPlayer.STATE_IDLE -> {
            }
            VideoPlayer.STATE_PREPARING -> {
                mImage!!.visibility = View.GONE
                mLoading!!.visibility = View.VISIBLE
                mLoadText!!.text = "正在准备..."
                mError!!.visibility = View.GONE
                mCompleted!!.visibility = View.GONE
                mTop!!.visibility = View.GONE
                mBottom!!.visibility = View.GONE
                mCenterStart!!.visibility = View.GONE
                mLength!!.visibility = View.GONE
            }
            VideoPlayer.STATE_PREPARED -> startUpdateProgressTimer()
            VideoPlayer.STATE_PLAYING -> {
                mLoading!!.visibility = View.GONE
                mRestartPause!!.setImageResource(R.mipmap.ic_player_pause)
                startDismissTopBottomTimer()
            }
            VideoPlayer.STATE_PAUSED -> {
                mLoading!!.visibility = View.GONE
                mRestartPause!!.setImageResource(R.mipmap.ic_player_start)
                cancelDismissTopBottomTimer()
            }
            VideoPlayer.STATE_BUFFERING_PLAYING -> {
                mLoading!!.visibility = View.VISIBLE
                mRestartPause!!.setImageResource(R.mipmap.ic_player_pause)
                mLoadText!!.text = "正在缓冲..."
                startDismissTopBottomTimer()
            }
            VideoPlayer.STATE_BUFFERING_PAUSED -> {
                mLoading!!.visibility = View.VISIBLE
                mRestartPause!!.setImageResource(R.mipmap.ic_player_start)
                mLoadText!!.text = "正在缓冲..."
                cancelDismissTopBottomTimer()
            }
            VideoPlayer.STATE_ERROR -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                mTop!!.visibility = View.VISIBLE
                mError!!.visibility = View.VISIBLE
            }
            VideoPlayer.STATE_COMPLETED -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                mImage!!.visibility = View.VISIBLE
                mCompleted!!.visibility = View.VISIBLE
            }
        }
    }

    override fun onPlayModeChanged(playMode: Int) {
        when (playMode) {
            VideoPlayer.MODE_NORMAL -> {
                mBack!!.visibility = View.GONE
                mFullScreen!!.setImageResource(R.mipmap.ic_player_enlarge)
                mFullScreen!!.visibility = View.VISIBLE
                mBatteryTime!!.visibility = View.GONE
                if (hasRegisterBatteryReceiver) {
                    mContext.unregisterReceiver(mBatterReceiver)
                    hasRegisterBatteryReceiver = false
                }
            }
            VideoPlayer.MODE_FULL_SCREEN -> {
                mBack!!.visibility = View.VISIBLE
                mFullScreen!!.visibility = View.GONE
                mFullScreen!!.setImageResource(R.mipmap.ic_player_shrink)
                mBatteryTime!!.visibility = View.VISIBLE
                if (!hasRegisterBatteryReceiver) {
                    mContext.registerReceiver(mBatterReceiver,
                            IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                    hasRegisterBatteryReceiver = true
                }
            }
            VideoPlayer.MODE_TINY_WINDOW -> mBack!!.visibility = View.VISIBLE
        }
    }

    override fun reset() {
        topBottomVisible = false
        cancelUpdateProgressTimer()
        cancelDismissTopBottomTimer()
        mSeek!!.progress = 0
        mSeek!!.secondaryProgress = 0

        mCenterStart!!.visibility = View.VISIBLE
        mImage!!.visibility = View.VISIBLE

        mBottom!!.visibility = View.GONE
        mFullScreen!!.setImageResource(R.mipmap.ic_player_enlarge)

        mLength!!.visibility = View.VISIBLE

        mTop!!.visibility = View.VISIBLE
        mBack!!.visibility = View.GONE

        mLoading!!.visibility = View.GONE
        mError!!.visibility = View.GONE
        mCompleted!!.visibility = View.GONE
    }

    /**
     * 尽量不要在onClick中直接处理控件的隐藏、显示及各种UI逻辑。
     * UI相关的逻辑都尽量到[.onPlayStateChanged]和[.onPlayModeChanged]中处理.
     */
    override fun onClick(v: View) {
        if (v === mCenterStart) {
            if (mVideoPlayer!!.isIdle()) {
                mVideoPlayer!!.start()
            }
        } else if (v === mBack) {
            if (mVideoPlayer!!.isFullScreen()) {
                mVideoPlayer!!.exitFullScreen()
            } else if (mVideoPlayer!!.isTinyWindow()) {
                mVideoPlayer!!.exitTinyWindow()
            }
        } else if (v === mRestartPause) {
            if (mVideoPlayer!!.isPlaying() || mVideoPlayer!!.isBufferingPlaying()) {
                mVideoPlayer!!.pause()
            } else if (mVideoPlayer!!.isPaused() || mVideoPlayer!!.isBufferingPaused()) {
                mVideoPlayer!!.restart()
            }
        } else if (v === mFullScreen) {
            if (mVideoPlayer!!.isNormal() || mVideoPlayer!!.isTinyWindow()) {
                mVideoPlayer!!.enterFullScreen()
            } else if (mVideoPlayer!!.isFullScreen()) {
                mVideoPlayer!!.exitFullScreen()
            }
        } else if (v === mRetry) {
            mVideoPlayer!!.restart()
        } else if (v === mReplay) {
            mRetry!!.performClick()
        } else if (v === mShare) {
            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show()
        } else if (v === this) {
            if (mVideoPlayer!!.isPlaying()
                    || mVideoPlayer!!.isPaused()
                    || mVideoPlayer!!.isBufferingPlaying()
                    || mVideoPlayer!!.isBufferingPaused()) {
                setTopBottomVisible(!topBottomVisible)
            }
        }
    }

    /**
     * 设置top、bottom的显示和隐藏
     *
     * @param visible true显示，false隐藏.
     */
    private fun setTopBottomVisible(visible: Boolean) {
        mTop!!.visibility = if (visible) View.VISIBLE else View.GONE
        mBottom!!.visibility = if (visible) View.VISIBLE else View.GONE
        topBottomVisible = visible
        if (visible) {
            if (!mVideoPlayer!!.isPaused() && !mVideoPlayer!!.isBufferingPaused()) {
                startDismissTopBottomTimer()
            }
        } else {
            cancelDismissTopBottomTimer()
        }
    }

    /**
     * 开启top、bottom自动消失的timer
     */
    private fun startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer()
        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = object : CountDownTimer(8000, 8000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    setTopBottomVisible(false)
                }
            }
        }
        mDismissTopBottomCountDownTimer!!.start()
    }

    /**
     * 取消top、bottom自动消失的timer
     */
    private fun cancelDismissTopBottomTimer() {
        if (mDismissTopBottomCountDownTimer != null) {
            mDismissTopBottomCountDownTimer!!.cancel()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (mVideoPlayer!!.isBufferingPaused() || mVideoPlayer!!.isPaused()) {
            mVideoPlayer!!.restart()
        }
        val position = (mVideoPlayer!!.getDuration() * seekBar.progress / 100f).toLong()
        mVideoPlayer!!.seekTo(position)
        startDismissTopBottomTimer()
    }

    override fun updateProgress() {
        val position = mVideoPlayer!!.getCurrentPosition()
        val duration = mVideoPlayer!!.getDuration()
        val bufferPercentage = mVideoPlayer!!.getBufferPercentage()
        mSeek!!.secondaryProgress = bufferPercentage
        val progress = (100f * position / duration).toInt()
        mSeek!!.progress = progress
        mPosition!!.text = VideoHandleUtils.formatTime(position)
        mDuration!!.text = VideoHandleUtils.formatTime(duration)
        // 更新时间
        mTime!!.text = SimpleDateFormat("HH:mm", Locale.CHINA).format(Date())
    }

    override fun showChangePosition(duration: Long, newPositionProgress: Int) {
        mChangePosition!!.visibility = View.VISIBLE
        val newPosition = (duration * newPositionProgress / 100f).toLong()
        mChangePositionCurrent!!.text = VideoHandleUtils.formatTime(newPosition)
        mChangePositionProgress!!.progress = newPositionProgress
        mSeek!!.progress = newPositionProgress
        mPosition!!.text = VideoHandleUtils.formatTime(newPosition)
    }

    override fun hideChangePosition() {
        mChangePosition!!.visibility = View.GONE
    }

    override fun showChangeVolume(newVolumeProgress: Int) {
        mChangeVolume!!.visibility = View.VISIBLE
        mChangeVolumeProgress!!.progress = newVolumeProgress
    }

    override fun hideChangeVolume() {
        mChangeVolume!!.visibility = View.GONE
    }

    override fun showChangeBrightness(newBrightnessProgress: Int) {
        mChangeBrightness!!.visibility = View.VISIBLE
        mChangeBrightnessProgress!!.progress = newBrightnessProgress
    }

    override fun hideChangeBrightness() {
        mChangeBrightness!!.visibility = View.GONE
    }
}
