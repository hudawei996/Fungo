package com.fungo.repertory.app

import com.fungo.baselib.app.BaseApplication
import com.fungo.imagego.ImageManager
import com.fungo.imagego.glide.GlideImageGoFactory
import com.fungo.playergo.VideoPlayerManager
import com.fungo.playergo.create.VideoPlayerFactory
import com.fungo.playergo.ijkplayer.IjkVideoPlayerFactory

/**
 * @author Pinger
 * @since 3/29/18 9:13 PM
 */
class SampleApplication : BaseApplication() {

    override fun initSDK() {
        ImageManager.instance.setImageGoFactory(GlideImageGoFactory())
        VideoPlayerManager.instance.setVideoPlayer(IjkVideoPlayerFactory())
    }

}