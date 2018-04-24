package com.fungo.baselib.player

/**
 * @author Pinger
 * @since 2017/12/22 0022 上午 10:52
 * 播放器状态常量,一个底层播放器对应一套常量，可以在多个播放器中使用，如果使用多套底层，常量会被替换
 */

object PlayerConstant {

    var MEDIA_INFO_VIDEO_RENDERING_START: Int = 0
    var MEDIA_INFO_BUFFERING_START: Int = 0
    var MEDIA_INFO_BUFFERING_END: Int = 0
    var MEDIA_INFO_VIDEO_ROTATION_CHANGED: Int = 0
    var MEDIA_INFO_NOT_SEEK: Int = 0
}
