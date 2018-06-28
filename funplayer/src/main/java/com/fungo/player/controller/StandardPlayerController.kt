package com.fungo.player.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import com.fungo.player.R
import com.fungo.player.utils.PlayerConstant
import com.fungo.player.utils.PlayerUtils
import kotlinx.android.synthetic.main.layout_player_control_standard.view.*

/**
 * @author Pinger
 * @since 18-6-21 下午8:28
 * 标准的一套控制器，方便直接使用
 */

class StandardPlayerController(context: Context) : BasePlayerController(context), View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    private var isShowPortTopContainer: Boolean = false
    private var isShowPortBottomProgressView: Boolean = false
    private var isShowCenterPlayView: Boolean = false


    override fun getLayoutId(): Int {
        return R.layout.layout_player_control_standard
    }

    override fun initView() {
        fpCenterPlayView.setOnClickListener(this)
        fpLockView.setOnClickListener(this)
        fpTopBackView.setOnClickListener(this)
        fpBottomPlayView.setOnClickListener(this)
        fpBottomFullView.setOnClickListener(this)
        fpBottomSeekBar.setOnSeekBarChangeListener(this)
    }


    override fun onClick(v: View?) {
        when (v) {
            fpTopBackView -> onBackPressed()
            fpLockView -> doLockUnlock(fpLockView)
            fpBottomPlayView, fpCenterPlayView -> doPauseStart()
            fpBottomFullView -> doStartStopFullScreen()
        }
    }


    /**
     * 获取图片对象
     * @return 占位图
     */
    override fun getImage(): ImageView {
        return fpPlaceBgView
    }


    /**
     * 设置标题
     * @param title 标题
     */
    override fun setTitle(title: String) {
        fpTopTitleView?.text = title
    }


    /**
     * 播放状态改变
     */
    override fun onPlayStateChanged(playState: Int) {
        when (playState) {
            PlayerConstant.STATE_IDLE -> {
                setTopBottomVisible(false)
                setLocked(false)
                setSelected(fpLockView, false)
                getPlayer().setLock(false)
                fpBottomProgressView.progress = 0
                fpBottomProgressView.secondaryProgress = 0
                fpBottomProgressView.visibility = View.GONE
                fpBottomSeekBar.progress = 0
                fpBottomSeekBar.secondaryProgress = 0
                setVisible(fpCenterPlayView)
                setVisible(fpPlaceBgView)
            }
            PlayerConstant.STATE_PREPARING -> {
                setGone(fpPlaceBgView)
                setGone(fpCenterPlayView)
                getStateView().showLoading()
            }
            PlayerConstant.STATE_PREPARED -> {
                startUpdateProgressTimer()
                if (!isLive()) setVisible(fpBottomProgressView)
                setGone(fpCenterPlayView)
            }
            PlayerConstant.STATE_BUFFERING -> {
                setGone(fpCenterPlayView)
                getStateView().showBufferingUpdate()
            }
            PlayerConstant.STATE_BUFFERED -> {
                setGone(fpCenterPlayView)
                getStateView().hideState()
            }

            PlayerConstant.STATE_PLAYING -> {
                getStateView().hideState()
                setTopBottomVisible(true)
                setSelected(fpBottomPlayView, true)
                setSelected(fpCenterPlayView, true)
                setGone(fpPlaceBgView)
                setGone(fpCenterPlayView)
            }

            PlayerConstant.STATE_PAUSED -> {
                setSelected(fpBottomPlayView, false)
                setSelected(fpCenterPlayView, false)
                setGone(fpCenterPlayView)
                cancelDismissTopBottomTimer()
            }

            PlayerConstant.STATE_PLAYBACK_COMPLETED -> {
                getStateView().showCompleted(OnClickListener {
                    getStateView().hideState()
                    getPlayer().retry()
                })
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)

                setGone(fpCenterPlayView)
                setVisible(fpPlaceBgView)
                fpBottomProgressView.progress = 0
                fpBottomProgressView.secondaryProgress = 0
                setLocked(false)
                getPlayer().setLock(false)
            }

            PlayerConstant.STATE_ERROR -> {
                getStateView().showError(OnClickListener {
                    getStateView().hideState()
                    getPlayer().retry()
                })
                setGone(fpCenterPlayView)
                setGone(fpPlaceBgView)
                setGone(fpBottomProgressView)
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
            }
        }
    }


    /**
     * 播放模式改动，横屏和　竖屏
     */
    override fun onPlayModeChanged(playMode: Int) {
        when (playMode) {
            PlayerConstant.MODE_FULL_SCREEN -> {
                if (isLocked()) return
                setGestureEnable(true)
                setSelected(fpBottomFullView, true)
                setVisible(fpLockView)
                setVisible(fpTopContainer)
                setVisible(fpBottomContainer)
                setTopBottomVisible(true)
            }
            PlayerConstant.MODE_NORMAL -> {
                if (isLocked()) return
                layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                setGestureEnable(false)
                setSelected(fpBottomFullView, false)
                setGone(fpLockView)
                setVisible(fpTopContainer)
                setVisible(fpBottomContainer)
                setTopBottomVisible(true)
            }
        }
    }


    /**
     * 设置顶部和顶部的悬浮展示，如果锁屏了则只需要设置锁屏按钮的展示
     */
    override fun setTopBottomVisible(visible: Boolean) {
        if (isLocked()) {
            if (visible) {
                fpLockView.animate().translationX(0f).setDuration(150).start()
                startDismissTopBottomTimer()
            } else {
                fpLockView.animate().translationX(-(fpLockView.left + fpLockView.width).toFloat()).setDuration(150).start()
                cancelDismissTopBottomTimer()
            }
            return
        }

        if (visible) {
            fpTopContainer.animate().translationY(0f).setDuration(150).start()
            fpBottomContainer.animate().translationY(0f).setDuration(150).start()
            fpLockView.animate().translationX(0f).setDuration(150).start()

            startDismissTopBottomTimer()
            setGone(fpBottomProgressView)
        } else {
            fpTopContainer.animate().translationY(-fpTopContainer.height.toFloat()).setDuration(150).start()
            fpBottomContainer.animate().translationY(fpBottomContainer.height.toFloat()).setDuration(150).start()
            fpLockView.animate().translationX(-(fpLockView.left + fpLockView.width).toFloat()).setDuration(150).start()

            cancelDismissTopBottomTimer()

            postDelayed({
                setVisible(fpBottomProgressView)
            }, 150)
        }
    }


    /**
     * 更新进度条进度，主要有底部的悬浮进度条和依附进度条
     */
    override fun updateProgress() {
        val position = getPlayer().getCurrentPosition()
        val duration = getPlayer().getDuration()
        val bufferPercentage = getPlayer().getBufferPercentage()
        val progress = (100f * position / duration).toInt()
        fpBottomSeekBar?.secondaryProgress = bufferPercentage
        fpBottomSeekBar?.progress = progress
        fpBottomProgressView?.secondaryProgress = bufferPercentage
        fpBottomProgressView?.progress = progress
        fpBottomCurrentTimeView?.text = PlayerUtils.formatTime(position)
        fpBottomTotalTimeView?.text = PlayerUtils.formatTime(duration)
        fpTopTimeView?.text = PlayerUtils.formatCurrentTime()
    }


    /**
     * 从窗体移除，注销电量广播
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        context.unregisterReceiver(mBatterReceiver)
    }

    /**
     * 从窗体依附，注册电量广播
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        context.registerReceiver(mBatterReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    /**
     * 实现SeekBar的OnSeekBarChangeListener方法，每次进度改变时都会回调
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    /**
     * 实现SeekBar的OnSeekBarChangeListener方法，手指按下时会回调
     */
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    /**
     * 实现SeekBar的OnSeekBarChangeListener方法，手指抬起时回调
     * 如果是暂停状态则开始播放，并且将播放进度设置到当前抬起的广告
     */
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (getPlayer().isPaused()) {
            getPlayer().start()
        }
        val duration = getPlayer().getDuration()
        val progress = (seekBar?.progress ?: 0).toLong()
        val position = (progress * duration / 100f).toLong()
        getPlayer().seekTo(position)
        startDismissTopBottomTimer()
    }

    /**
     * 电池状态即电量变化广播接收器
     */
    private val mBatterReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN)
            when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> // 充电中
                    fpTopBatteryView?.setImageResource(R.mipmap.ic_player_brighting)
                BatteryManager.BATTERY_STATUS_FULL -> // 充电完成
                    fpTopBatteryView?.setImageResource(R.mipmap.ic_player_battery_100)
                else -> {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
                    val percentage = (level.toFloat() / scale * 100).toInt()
                    when {
                        percentage <= 20 -> fpTopBatteryView?.setImageResource(R.mipmap.ic_player_battery_20)
                        percentage <= 40 -> fpTopBatteryView?.setImageResource(R.mipmap.ic_player_battery_40)
                        percentage <= 60 -> fpTopBatteryView?.setImageResource(R.mipmap.ic_player_battery_60)
                        percentage <= 80 -> fpTopBatteryView?.setImageResource(R.mipmap.ic_player_battery_80)
                        percentage <= 100 -> fpTopBatteryView?.setImageResource(R.mipmap.ic_player_battery_100)
                    }
                }
            }
        }
    }



    companion object {
        class Builder(context: Context){

            val controller = StandardPlayerController(context)

            fun setShowPortTopContainer(isShow:Boolean){
                controller.isShowPortTopContainer = isShow
            }

            fun setShowPortBottomProgressView(isShow: Boolean){
                controller.isShowPortBottomProgressView = isShow
            }

            fun setShowCenterPlayView(isShow: Boolean){
                controller.isShowCenterPlayView = isShow
            }


            fun build():BasePlayerController{
                return controller
            }
        }
    }



}