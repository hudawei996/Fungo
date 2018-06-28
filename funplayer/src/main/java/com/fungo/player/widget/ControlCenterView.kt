package com.fungo.player.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.fungo.player.R
import com.fungo.player.utils.PlayerUtils

/**
 * @author Pinger
 * @since 18-6-13 下午4:59
 * 中心控制器，控制音量，亮度，进度等中间位置的操作属性
 */

class ControlCenterView(context: Context) : LinearLayout(context) {

    private var mCenterIcon: ImageView
    private var mCenterPercent: TextView
    private var mCenterProgress: ProgressBar
    private var mCenterTime: TextView

    init {
        gravity = Gravity.CENTER
        val view = LayoutInflater.from(getContext()).inflate(R.layout.layout_player_control_center, this)
        mCenterIcon = view.findViewById(R.id.control_center_icon)
        mCenterPercent = view.findViewById(R.id.control_center_percent)
        mCenterProgress = view.findViewById(R.id.control_center_progress)
        mCenterTime = view.findViewById(R.id.control_center_time)
        visibility = View.GONE
    }


    /**
     * 设置View的可见和隐藏
     */
   private fun setVisible(view:View,isVisible: Boolean) {
        if (isVisible) {
            if (view.visibility != View.VISIBLE) {
                view.visibility = View.VISIBLE
            }
        } else {
            if (view.visibility != View.GONE) {
                view.visibility = View.GONE
            }
        }
    }


    /**
     * 设置自身隐藏
     */
    fun setCenterGone(){
        setVisible(this,false)
    }


    /**
     * 设置不可见时，加上渐变动画
     */
    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE) {
            this.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
        }
    }


    /**
     * 显示音量变化
     */
    fun showVolumeChange(percent:Int){
        setVisible(this,true)
        setVisible(mCenterTime,false)
        setVisible(mCenterIcon,true)
        mCenterIcon.setImageResource(R.mipmap.ic_player_volume)
        mCenterPercent.text = "$percent%"
        mCenterProgress.progress = percent
    }


    /**
     * 显示亮度变化
     */
    fun showBrightnessChange(percent:Int){
        setVisible(this,true)
        setVisible(mCenterTime,false)
        setVisible(mCenterIcon,true)
        mCenterIcon.setImageResource(R.mipmap.ic_player_brightness)
        mCenterPercent.text = "$percent%"
        mCenterProgress.progress = percent
    }


    fun showPositionChange(isForward:Boolean,position: Long, currentPosition: Long, duration: Long) {
        setVisible(this,true)
        setVisible(mCenterTime,true)
        setVisible(mCenterIcon,false)

        mCenterTime.text = if (isForward) {
            "+${(position-currentPosition )/ 1000}秒"
        }else{
            "-${(currentPosition-position)/ 1000}秒"
        }
        mCenterPercent.text = "${PlayerUtils.formatTime(position)} / ${PlayerUtils.formatTime(duration)}"
        mCenterProgress.progress  =  (position*100/duration).toInt()

    }
}