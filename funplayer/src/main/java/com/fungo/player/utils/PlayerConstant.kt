package com.fungo.player.utils

/**
 * @author Pinger
 * @since 18-6-13 下午3:57
 *　播放器相关常量
 */

object PlayerConstant {

    var IS_PLAY_ON_MOBILE_NETWORK = false  // 是否在移动网络下播放视频
    var IS_PLAY_AUTO_WIFI_NETWORK = true   // 是否在移动网络下播放视频


    /***
     * 视频的缩放状态
     */
    const val SCREEN_SCALE_DEFAULT = 0
    const val SCREEN_SCALE_16_9 = 1
    const val SCREEN_SCALE_4_3 = 2
    const val SCREEN_SCALE_MATCH_PARENT = 3
    const val SCREEN_SCALE_ORIGINAL = 4
    const val SCREEN_SCALE_CENTER_CROP = 5


    /**
     * 播放器的播放的各种状态
     */
    const val STATE_ERROR = -1
    const val STATE_IDLE = 0
    const val STATE_PREPARING = 1
    const val STATE_PREPARED = 2
    const val STATE_PLAYING = 3
    const val STATE_PAUSED = 4
    const val STATE_PLAYBACK_COMPLETED = 5
    const val STATE_BUFFERING = 6
    const val STATE_BUFFERED = 7

    /**
     * 播放模式
     */
    const val MODE_NORMAL = 10        // 普通播放器
    const val MODE_FULL_SCREEN = 11   // 全屏播放器
    const val MODE_TINY_WINDOW = 12   // 悬浮播放

    /**
     * 当前播放器屏幕的朝向
     */
    const val PORTRAIT = 1
    const val LANDSCAPE = 2
    const val REVERSE_LANDSCAPE = 3

    /**
     * 当前的网络状态
     */
    const val NO_NETWORK = 0
    const val NETWORK_WIFI = 3
    const val NETWORK_MOBILE = 4

}