package com.leibo.repertory.player.controller


import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.leibo.baselib.player.VideoPlayer
import com.leibo.baselib.player.controller.BaseVideoPlayerController
import com.leibo.baseuilib.utils.ViewUtils
import com.leibo.repertory.R
import kotlinx.android.synthetic.main.video_player_live_controller.view.*
import com.leibo.baselib.image.ImageManager

/**
 * @author Pinger
 * @since 2017/12/21 0021 下午 1:08
 * 直播的播放控制器，没有操作按钮，空白页面
 */

class LiveVideoPlayerController(context: Context) : BaseVideoPlayerController(context) {

    private var mUrl: String? = null

    override fun initView() {
        LayoutInflater.from(context).inflate(R.layout.video_player_live_controller, this, true)
    }

    override fun setTitle(title: String) {

    }

    override fun setUrl(url: String) {
        mUrl = url
    }

    override fun setImage(resId: Int) {
        ivPlaceImage.setImageResource(resId)
    }

    override fun setImage(imageURl: String) {
        ImageManager.instance.loadBlurImage(imageURl, ivPlaceImage, 12f)
    }

    override fun imageView(): ImageView {
        return ivPlaceImage
    }

    override fun onPlayStateChanged(playState: Int) {
        when (playState) {
            VideoPlayer.STATE_IDLE -> {
            }
            VideoPlayer.STATE_PREPARING -> {
                ViewUtils.setVisible(layoutLoading)
                ViewUtils.setGone(ivPlaceImage)
                tvLoadingText?.text = "正在准备..."
            }
            VideoPlayer.STATE_PREPARED -> startUpdateProgressTimer()
            VideoPlayer.STATE_PLAYING -> {
                ViewUtils.setGone(layoutLoading)
            }
            VideoPlayer.STATE_PAUSED -> {
                ViewUtils.setGone(layoutLoading)
            }
            VideoPlayer.STATE_BUFFERING_PLAYING -> {
                ViewUtils.setVisible(layoutLoading)
                tvLoadingText?.text = "正在缓冲${mVideoPlayer!!.getBufferPercentage()}%..."
            }
            VideoPlayer.STATE_BUFFERING_PAUSED -> {
                ViewUtils.setVisible(layoutLoading)
                tvLoadingText?.text = "正在缓冲${mVideoPlayer!!.getBufferPercentage()}%..."
            }
            VideoPlayer.STATE_ERROR -> {
                cancelUpdateProgressTimer()
                ViewUtils.setGone(layoutLoading)
            }
            VideoPlayer.STATE_COMPLETED -> {
                cancelUpdateProgressTimer()
                ViewUtils.setVisible(ivPlaceImage)
            }
        }
    }

    override fun onPlayModeChanged(playMode: Int) {

    }

    override fun reset() {

    }

    override fun updateProgress() {

    }

    override fun showChangePosition(duration: Long, newPositionProgress: Int) {

    }

    override fun hideChangePosition(newPosition: Long) {

    }

    override fun showChangeVolume(newVolumeProgress: Int) {

    }

    override fun hideChangeVolume() {

    }

    override fun showChangeBrightness(newBrightnessProgress: Int) {

    }

    override fun hideChangeBrightness() {

    }
}