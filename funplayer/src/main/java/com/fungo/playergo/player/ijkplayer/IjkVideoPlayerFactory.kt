package com.fungo.playergo.player.ijkplayer

import android.content.Context
import com.fungo.playergo.player.create.IVideoExecutor
import com.fungo.playergo.player.create.VideoPlayerFactory


/**
 * @author Pinger
 * @since 2017/12/21 0021 下午 2:48
 */

class IjkVideoPlayerFactory : VideoPlayerFactory() {

    override fun create(context: Context): IVideoExecutor {
        return IjkVideoPlayer()
    }
}
