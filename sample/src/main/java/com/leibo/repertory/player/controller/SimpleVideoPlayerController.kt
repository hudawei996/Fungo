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
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import com.leibo.baselib.player.IVideoPlayer
import com.leibo.baselib.player.VideoHandleUtils
import com.leibo.baselib.player.VideoPlayer
import com.leibo.baselib.player.controller.BaseVideoPlayerController
import com.leibo.repertory.R
import kotlinx.android.synthetic.main.video_player_simple_controller.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Pinger
 * @since 2017/12/28 0028 下午 7:14
 */

class SimpleVideoPlayerController(context: Context) : BaseVideoPlayerController(context), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private var mUrl: String? = null
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
            when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> // 充电中
                    ivTopBattery.setImageResource(R.mipmap.ic_player_brighting)
                BatteryManager.BATTERY_STATUS_FULL -> // 充电完成
                    ivTopBattery.setImageResource(R.mipmap.ic_player_battery_100)
                else -> {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
                    val percentage = (level.toFloat() / scale * 100).toInt()
                    when {
                        percentage <= 20 -> ivTopBattery?.setImageResource(R.mipmap.ic_player_battery_20)
                        percentage <= 40 -> ivTopBattery?.setImageResource(R.mipmap.ic_player_battery_40)
                        percentage <= 60 -> ivTopBattery?.setImageResource(R.mipmap.ic_player_battery_60)
                        percentage <= 80 -> ivTopBattery?.setImageResource(R.mipmap.ic_player_battery_80)
                        percentage <= 100 -> ivTopBattery.setImageResource(R.mipmap.ic_player_battery_100)
                    }
                }
            }
        }
    }

    override fun initView() {
        LayoutInflater.from(mContext).inflate(R.layout.video_player_simple_controller, this, true)
        ivCenterStartOrPause.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        ivBottomStartOrPause.setOnClickListener(this)
        ivFullOrShrinkScreen.setOnClickListener(this)
        tvErrorRetry.setOnClickListener(this)
        tvReplay.setOnClickListener(this)
        ivShare.setOnClickListener(this)
        skBottomSeek.setOnSeekBarChangeListener(this)
        this.setOnClickListener(this)
    }

    override fun setTitle(title: String) {
        tvTitle?.text = title
    }

    override fun setUrl(url: String) {
        mUrl = url
    }

    override fun imageView(): ImageView {
        return ivPlaceImage
    }

    override fun setImage(@DrawableRes resId: Int) {
        ivPlaceImage?.setImageResource(resId)
    }

    override fun setImage(imageURl: String) {
        // TODO 加载网络图片
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
                ivPlaceImage?.visibility = View.GONE
                layoutLoading?.visibility = View.VISIBLE
                tvLoadingText?.text = "正在准备..."
                layoutError?.visibility = View.GONE
                layoutPlayerCompleted?.visibility = View.GONE
                layoutTopController?.visibility = View.GONE
                layoutBottomController!!.visibility = View.GONE
                ivCenterStartOrPause!!.visibility = View.GONE
            }
            VideoPlayer.STATE_PREPARED -> startUpdateProgressTimer()
            VideoPlayer.STATE_PLAYING -> {
                layoutLoading?.visibility = View.GONE
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_pause)
                startDismissTopBottomTimer()
            }
            VideoPlayer.STATE_PAUSED -> {
                layoutLoading?.visibility = View.GONE
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_start)
                cancelDismissTopBottomTimer()
            }
            VideoPlayer.STATE_BUFFERING_PLAYING -> {
                layoutLoading?.visibility = View.VISIBLE
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_pause)
                tvLoadingText?.text = "正在缓冲..."
                startDismissTopBottomTimer()
            }
            VideoPlayer.STATE_BUFFERING_PAUSED -> {
                layoutLoading?.visibility = View.VISIBLE
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_start)
                tvLoadingText?.text = "正在缓冲..."
                cancelDismissTopBottomTimer()
            }
            VideoPlayer.STATE_ERROR -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                layoutTopController!!.visibility = View.VISIBLE
                layoutError!!.visibility = View.VISIBLE
            }
            VideoPlayer.STATE_COMPLETED -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                ivPlaceImage?.visibility = View.VISIBLE
                layoutPlayerCompleted?.visibility = View.VISIBLE
            }
        }
    }

    override fun onPlayModeChanged(playMode: Int) {
        when (playMode) {
            VideoPlayer.MODE_NORMAL -> {
                ivBack?.visibility = View.GONE
                ivFullOrShrinkScreen?.setImageResource(R.mipmap.ic_player_full)
                ivFullOrShrinkScreen?.visibility = View.VISIBLE
                layoutTopBatteryTime?.visibility = View.GONE
                if (hasRegisterBatteryReceiver) {
                    mContext.unregisterReceiver(mBatterReceiver)
                    hasRegisterBatteryReceiver = false
                }
            }
            VideoPlayer.MODE_FULL_SCREEN -> {
                ivBack?.visibility = View.VISIBLE
                ivFullOrShrinkScreen?.visibility = View.GONE
                ivFullOrShrinkScreen?.setImageResource(R.mipmap.ic_player_shrink)
                layoutTopBatteryTime?.visibility = View.VISIBLE
                if (!hasRegisterBatteryReceiver) {
                    mContext.registerReceiver(mBatterReceiver,
                            IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                    hasRegisterBatteryReceiver = true
                }
            }
            VideoPlayer.MODE_TINY_WINDOW -> ivBack?.visibility = View.VISIBLE
        }
    }

    override fun reset() {
        topBottomVisible = false
        cancelUpdateProgressTimer()
        cancelDismissTopBottomTimer()
        skBottomSeek?.progress = 0
        skBottomSeek?.secondaryProgress = 0

        ivCenterStartOrPause?.visibility = View.VISIBLE
        ivPlaceImage?.visibility = View.VISIBLE

        layoutBottomController?.visibility = View.GONE
        ivFullOrShrinkScreen?.setImageResource(R.mipmap.ic_player_full)

        layoutTopController?.visibility = View.VISIBLE
        ivBack?.visibility = View.GONE

        layoutLoading?.visibility = View.GONE
        layoutError?.visibility = View.GONE
        layoutPlayerCompleted?.visibility = View.GONE
    }

    /**
     * 尽量不要在onClick中直接处理控件的隐藏、显示及各种UI逻辑。
     * UI相关的逻辑都尽量到[.onPlayStateChanged]和[.onPlayModeChanged]中处理.
     */
    override fun onClick(v: View) {
        if (v === ivCenterStartOrPause) {
            if (mVideoPlayer!!.isIdle()) {
                mVideoPlayer!!.start()
            }
        } else if (v === ivBack) {
            if (mVideoPlayer!!.isFullScreen()) {
                mVideoPlayer!!.exitFullScreen()
            } else if (mVideoPlayer!!.isTinyWindow()) {
                mVideoPlayer!!.exitTinyWindow()
            }
        } else if (v === ivBottomStartOrPause) {
            if (mVideoPlayer!!.isPlaying() || mVideoPlayer!!.isBufferingPlaying()) {
                mVideoPlayer!!.pause()
            } else if (mVideoPlayer!!.isPaused() || mVideoPlayer!!.isBufferingPaused()) {
                mVideoPlayer!!.restart()
            }
        } else if (v === ivFullOrShrinkScreen) {
            if (mVideoPlayer!!.isNormal() || mVideoPlayer!!.isTinyWindow()) {
                mVideoPlayer!!.enterFullScreen()
            } else if (mVideoPlayer!!.isFullScreen()) {
                mVideoPlayer!!.exitFullScreen()
            }
        } else if (v == tvErrorRetry) {
            mVideoPlayer!!.restart()
        } else if (v == tvReplay) {
            tvErrorRetry!!.performClick()
        } else if (v == ivShare) {
            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show()
        } else if (v == this) {
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
        layoutTopController?.visibility = if (visible) View.VISIBLE else View.GONE
        layoutBottomController?.visibility = if (visible) View.VISIBLE else View.GONE
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
        skBottomSeek?.secondaryProgress = bufferPercentage
        val progress = (100f * position / duration).toInt()
        skBottomSeek?.progress = progress
        tvBottomPosition?.text = VideoHandleUtils.formatTime(position)
        tvBottomDuration?.text = VideoHandleUtils.formatTime(duration)
        // 更新时间
        tvTopTime?.text = SimpleDateFormat("HH:mm", Locale.CHINA).format(Date())
    }

    override fun showChangePosition(duration: Long, newPositionProgress: Int) {
        layoutChangePosition?.visibility = View.VISIBLE
        val newPosition = (duration * newPositionProgress / 100f).toLong()
        tvChangeCurrentPosition?.text = VideoHandleUtils.formatTime(newPosition)
        pbChangeProgressPosition?.progress = newPositionProgress
        skBottomSeek?.progress = newPositionProgress
        tvBottomPosition?.text = VideoHandleUtils.formatTime(newPosition)
    }

    override fun hideChangePosition() {
        layoutChangePosition?.visibility = View.GONE
    }

    override fun showChangeVolume(newVolumeProgress: Int) {
        layoutChangeVolume?.visibility = View.VISIBLE
        pbChangeProgressVolume?.progress = newVolumeProgress
    }

    override fun hideChangeVolume() {
        layoutChangeVolume?.visibility = View.GONE
    }

    override fun showChangeBrightness(newBrightnessProgress: Int) {
        layoutChangeBrightness?.visibility = View.VISIBLE
        pbChangeProgressBrightness?.progress = newBrightnessProgress
    }

    override fun hideChangeBrightness() {
        layoutChangeBrightness?.visibility = View.GONE
    }
}
