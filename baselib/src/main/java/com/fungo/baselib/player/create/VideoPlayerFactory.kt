package com.fungo.baselib.player.create

import android.content.Context

/**
 * @author Pinger
 * @since 2017/5/21 0021 下午 6:58
 * 工厂接口创建底层对象
 */
abstract class VideoPlayerFactory {

    /**
     * 创建抽象工厂
     */
    abstract fun create(context: Context): IVideoExecutor
}
