package com.leibo.baselib.player.ijkplayer

import android.content.Context
import com.leibo.baselib.player.create.IVideoExecutor
import com.leibo.baselib.player.create.VideoPlayerFactory


/**
 * @author Pinger
 * @since 2017/12/21 0021 下午 2:48
 */

class IjkVideoPlayerFactory : VideoPlayerFactory() {

    override fun create(context: Context): IVideoExecutor {
        return IjkVideoPlayer()
    }
}
