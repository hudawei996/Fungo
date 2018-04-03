package com.leibo.repertory.player.controller

import android.content.Context
import android.widget.ImageView
import com.leibo.baselib.player.controller.BaseVideoPlayerController

/**
 * @author Pinger
 * @since 18-4-2 下午2:10
 *
 */
class LiveVideoPlayerController (context:Context):BaseVideoPlayerController(context){


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
        return ImageView(context)
    }

    override fun updateProgress() {
    }

    override fun onPlayStateChanged(playState: Int) {
    }

    override fun onPlayModeChanged(playMode: Int) {
    }

    override fun reset() {
    }

    override fun showChangePosition(duration: Long, newPositionProgress: Int) {
    }

    override fun hideChangePosition(newPosition:Long) {
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