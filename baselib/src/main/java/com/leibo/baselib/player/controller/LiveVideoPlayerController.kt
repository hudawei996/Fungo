package com.leibo.baselib.player.controller


import android.content.Context
import android.widget.ImageView

/**
 * @author Pinger
 * @since 2017/12/21 0021 下午 1:08
 * 直播的播放控制器，没有操作按钮，空白页面
 */

class LiveVideoPlayerController(context: Context) : BaseVideoPlayerController(context) {

    override fun initView() {

    }

    override fun setTitle(title: String) {

    }

    override fun setUrl(url: String) {

    }

    override fun setImage(resId: Int) {

    }

    override fun setImage(imageURl: String) {

    }

    override fun imageView(): ImageView {
        return ImageView(mContext)
    }

    override fun setLength(length: Long) {

    }

    override fun onPlayStateChanged(playState: Int) {

    }

    override fun onPlayModeChanged(playMode: Int) {

    }

    override fun reset() {

    }

    override fun updateProgress() {

    }

    override fun showChangePosition(duration: Long, newPositionProgress: Int) {

    }

    override fun hideChangePosition() {

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
