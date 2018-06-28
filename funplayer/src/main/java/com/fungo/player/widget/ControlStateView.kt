package com.fungo.player.widget

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fungo.player.R

/**
 * @author Pinger
 * @since 18-6-14 下午2:28
 * 控制器状态的View,包括视频加载，播放完成,播放异常，网络错误等状态控制
 */

class ControlStateView(context: Context) : LinearLayout(context) {


    private var mSateTextContainer: View
    private var mSateProgressContainer: View
    private var mSateImageContainer: View
    private var mSateText: TextView
    private var mSateTextBtn: TextView
    private var mSateProgressBtn: TextView
    private var mSateImageBtn: TextView
    private var mSateImage: ImageView

    init {
        visibility = View.GONE
        isClickable = true
        val rootView = LayoutInflater.from(context).inflate(R.layout.layout_player_control_state, this)
        mSateTextContainer = rootView.findViewById(R.id.state_text_container)
        mSateProgressContainer = rootView.findViewById(R.id.state_progress_container)
        mSateImageContainer = rootView.findViewById(R.id.state_image_container)

        mSateText = rootView.findViewById(R.id.state_text)
        mSateTextBtn = rootView.findViewById(R.id.state_text_btn)
        mSateProgressBtn = rootView.findViewById(R.id.state_progress_btn)
        mSateImage = rootView.findViewById(R.id.state_image)
        mSateImageBtn = rootView.findViewById(R.id.state_image_btn)
    }


    fun showLoading() {
        setBackgroundColor(Color.BLACK)
        setVisible(mSateProgressContainer)
        mSateProgressBtn.text = context.getString(R.string.player_prepared)
    }

    fun showBufferingUpdate() {
        setBackgroundColor(Color.TRANSPARENT)
        setVisible(mSateProgressContainer)
        mSateProgressBtn.text = context.getString(R.string.player_buffering_update)
    }

    fun showError(listener: OnClickListener) {
        setBackgroundColor(Color.BLACK)
        setVisible(mSateImageContainer)
        mSateImage.setImageResource(R.mipmap.ic_player_replay)
        mSateImageBtn.text = context.getString(R.string.player_net_error)
        mSateImage.setOnClickListener(listener)
    }

    fun showCompleted(listener: OnClickListener) {
        setBackgroundColor(Color.TRANSPARENT)
        setVisible(mSateImageContainer)
        mSateImage.setImageResource(R.mipmap.ic_player_replay)
        mSateImageBtn.text = context.getString(R.string.player_completed)
        mSateImage.setOnClickListener(listener)
    }


    fun showNetError(listener: OnClickListener) {
        setBackgroundColor(Color.BLACK)
        setVisible(mSateTextContainer)
        mSateText.text = context.getString(R.string.player_net_error)
        mSateTextBtn.text = context.getString(R.string.player_confirm)
        mSateTextBtn.setOnClickListener(listener)
    }


    fun showNetMobile(listener: OnClickListener) {
        setBackgroundColor(Color.BLACK)
        setVisible(mSateTextContainer)
        mSateText.text = context.getString(R.string.player_net_mobile)
        mSateTextBtn.text = context.getString(R.string.player_continue_play)
        mSateTextBtn.setOnClickListener(listener)
    }


    fun hideState() {
        setGone(this)
        setGone(mSateImageContainer)
        setGone(mSateProgressContainer)
        setGone(mSateTextContainer)
    }


    private fun setVisible(view: View?) {
        if (this.visibility != View.VISIBLE) {
            this.visibility = View.VISIBLE
        }

        if (view?.visibility != View.VISIBLE) {
            view?.visibility = View.VISIBLE
        }
    }

    private fun setGone(view: View?) {
        if (view?.visibility != View.GONE) {
            view?.visibility = View.GONE
        }
    }


    private var downX: Float = 0f
    private var downY: Float = 0f

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                // True if the child does not want the parent to intercept touch events.
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val absDeltaX = Math.abs(ev.x - downX)
                val absDeltaY = Math.abs(ev.y - downY)
                if (absDeltaX > ViewConfiguration.get(context).scaledTouchSlop || absDeltaY > ViewConfiguration.get(context).scaledTouchSlop) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}