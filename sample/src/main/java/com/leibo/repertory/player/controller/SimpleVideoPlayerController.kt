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
import com.leibo.baseuilib.utils.ViewUtils
import com.leibo.repertory.R
import kotlinx.android.synthetic.main.video_player_simple_controller.view.*
import org.fungo.baselib.image.ImageManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Pinger
 * @since 2017/12/28 0028 下午 7:14
 */

class SimpleVideoPlayerController(context: Context) : BaseVideoPlayerController(context), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private var mUrl: String? = null
    private var mTitle: String? = null
    private var topBottomVisible: Boolean = false
    private var mDismissTopBottomCountDownTimer: CountDownTimer? = null

    private var hasRegisterBatteryReceiver: Boolean = false // 是否已经注册了电池广播
    private var mLastPosition: Long = 0  // 上一次的播放位置


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
        layoutError.setOnClickListener(this)
        ivTopShare.setOnClickListener(this)
        skBottomSeek.setOnSeekBarChangeListener(this)
        this.setOnClickListener(this)
    }

    override fun setTitle(title: String) {
        mTitle = title
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
        ImageManager.instance.loadImage(imageURl, ivPlaceImage)
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
                ViewUtils.setVisible(layoutLoading)
                ViewUtils.setGone(ivPlaceImage)
                tvLoadingText?.text = "正在准备..."
                ViewUtils.setGone(layoutError)
                ViewUtils.setGone(layoutTopController)
                ViewUtils.setGone(layoutBottomController)
                ViewUtils.setGone(ivCenterStartOrPause)
            }
            VideoPlayer.STATE_PREPARED -> startUpdateProgressTimer()
            VideoPlayer.STATE_PLAYING -> {
                ViewUtils.setGone(layoutLoading)
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_pause)
                ivCenterStartOrPause?.setImageResource(R.mipmap.ic_player_center_pause)
                startDismissTopBottomTimer()
            }
            VideoPlayer.STATE_PAUSED -> {
                ViewUtils.setGone(layoutLoading)
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_start)
                ivCenterStartOrPause?.setImageResource(R.mipmap.ic_player_center_start)
                cancelDismissTopBottomTimer()
            }
            VideoPlayer.STATE_BUFFERING_PLAYING -> {
                ViewUtils.setVisible(layoutLoading)
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_pause)
                tvLoadingText?.text = "正在缓冲${mVideoPlayer!!.getBufferPercentage()}%..."
                startDismissTopBottomTimer()
            }
            VideoPlayer.STATE_BUFFERING_PAUSED -> {
                ViewUtils.setVisible(layoutLoading)
                ivBottomStartOrPause?.setImageResource(R.mipmap.ic_player_bottom_start)
                tvLoadingText?.text = "正在缓冲${mVideoPlayer!!.getBufferPercentage()}%..."
                cancelDismissTopBottomTimer()
            }
            VideoPlayer.STATE_ERROR -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                ViewUtils.setGone(layoutLoading)
                ViewUtils.setVisible(layoutError)
            }
            VideoPlayer.STATE_COMPLETED -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                ViewUtils.setVisible(ivPlaceImage)
                ViewUtils.setVisible(ivCenterStartOrPause)
                ivCenterStartOrPause?.setImageResource(R.mipmap.ic_player_replay)
            }
        }
    }

    override fun onPlayModeChanged(playMode: Int) {
        when (playMode) {
            VideoPlayer.MODE_NORMAL -> {
                tvTitle?.text = ""
                ivFullOrShrinkScreen?.setImageResource(R.mipmap.ic_player_full)
                layoutTopBatteryTime?.visibility = View.GONE
                if (hasRegisterBatteryReceiver) {
                    mContext.unregisterReceiver(mBatterReceiver)
                    hasRegisterBatteryReceiver = false
                }
            }
            VideoPlayer.MODE_FULL_SCREEN -> {
                tvTitle?.text = mTitle
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
        ivCenterStartOrPause?.setImageResource(R.mipmap.ic_player_center_start)
        ivPlaceImage?.visibility = View.VISIBLE

        layoutBottomController?.visibility = View.GONE
        ivFullOrShrinkScreen?.setImageResource(R.mipmap.ic_player_full)

        layoutTopController?.visibility = View.VISIBLE

        layoutLoading?.visibility = View.GONE
        layoutError?.visibility = View.GONE
    }

    /**
     * 尽量不要在onClick中直接处理控件的隐藏、显示及各种UI逻辑。
     * UI相关的逻辑都尽量到[.onPlayStateChanged]和[.onPlayModeChanged]中处理.
     */
    override fun onClick(v: View) {
        if (v == ivBack) {
            when {
                mVideoPlayer!!.isFullScreen() -> mVideoPlayer!!.exitFullScreen()
                mVideoPlayer!!.isTinyWindow() -> mVideoPlayer!!.exitTinyWindow()
                else -> // TODO
                    Toast.makeText(mContext, "关闭当前页面", Toast.LENGTH_SHORT).show()
            }
        } else if (v == ivBottomStartOrPause || v == ivCenterStartOrPause) {
            if (mVideoPlayer!!.isPlaying() || mVideoPlayer!!.isBufferingPlaying()) {
                mVideoPlayer!!.pause()
                startLoadAD()
            } else if (mVideoPlayer!!.isPaused() || mVideoPlayer!!.isBufferingPaused() || mVideoPlayer!!.isCompleted()) {
                mVideoPlayer!!.restart()
            }
        } else if (v == ivFullOrShrinkScreen) {
            if (mVideoPlayer!!.isNormal() || mVideoPlayer!!.isTinyWindow()) {
                mVideoPlayer!!.enterFullScreen()
            } else if (mVideoPlayer!!.isFullScreen()) {
                mVideoPlayer!!.exitFullScreen()
            }
        } else if (v == layoutError) {
            ViewUtils.setGone(layoutError)
            mVideoPlayer!!.restart()
        } else if (v == ivTopShare) {
            startShareVideo()
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
     * 暂停视频请求广告数据，弹窗展示
     */
    private fun startLoadAD() {
        // TODO 广告
        Toast.makeText(mContext, "广告", Toast.LENGTH_SHORT).show()
    }

    /**
     * 分享视频
     */
    private fun startShareVideo() {
        // TODO 分享
        Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show()
    }

    /**
     * 设置top、bottom的显示和隐藏
     *
     * @param visible true显示，false隐藏.
     */
    private fun setTopBottomVisible(visible: Boolean) {
        layoutTopController?.visibility = if (visible) View.VISIBLE else View.GONE
        layoutBottomController?.visibility = if (visible) View.VISIBLE else View.GONE
        ivCenterStartOrPause.visibility = if (visible) View.VISIBLE else View.GONE
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
            mDismissTopBottomCountDownTimer = object : CountDownTimer(5000, 5000) {
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
        ViewUtils.setGone(ivCenterStartOrPause)
        ViewUtils.setVisible(layoutChangePosition)
        val newPosition = (duration * newPositionProgress / 100f).toLong()

        // 中间滑动的展示
        val dTime = (newPosition - mLastPosition) / 1000

        tvChangeDTime.text = if (dTime <= 0) {
            dTime.toString()
        } else {
            "+$dTime"
        }
        tvChangeCurrentTime?.text = VideoHandleUtils.formatTime(newPosition)
        tvChangeTotalTime?.text = VideoHandleUtils.formatTime(duration)
        pbChangePosition?.progress = newPositionProgress

        // 底下进度更新的位置
        skBottomSeek?.progress = newPositionProgress
        tvBottomPosition?.text = VideoHandleUtils.formatTime(newPosition)
    }

    override fun hideChangePosition(newPosition: Long) {
        ViewUtils.setGone(layoutChangePosition)
        mLastPosition = newPosition
    }

    override fun showChangeVolume(newVolumeProgress: Int) {
        ViewUtils.setGone(ivCenterStartOrPause)
        ViewUtils.setVisible(layoutChangeVolume)
        tvChangeProgressVolume?.text = "$newVolumeProgress%"
    }

    override fun hideChangeVolume() {
        ViewUtils.setGone(layoutChangeVolume)
    }

    override fun showChangeBrightness(newBrightnessProgress: Int) {
        ViewUtils.setGone(ivCenterStartOrPause)
        ViewUtils.setVisible(layoutChangeBrightness)
        tvChangeProgressBrightness?.text = "$newBrightnessProgress%"
    }

    override fun hideChangeBrightness() {
        ViewUtils.setGone(layoutChangeBrightness)
    }
}
