package com.fungo.player.utils

import com.fungo.player.media.IPlayer

/**
 * @author Pinger
 * @since 18-6-13 下午8:53
 *
 */

class PlayerConfig {

    var isLooping: Boolean = false
    var autoRotate: Boolean = false
    var isCache: Boolean = false
    var addToPlayerManager: Boolean = false
    var usingSurfaceView: Boolean = false
    var enableMediaCodec: Boolean = false
    var savingProgress: Boolean = false
    var disableAudioFocus: Boolean = false
    var player: IPlayer? = null

    constructor()

    constructor(config: PlayerConfig) {
        this.isLooping = config.isLooping
        this.autoRotate = config.autoRotate
        this.isCache = config.isCache
        this.addToPlayerManager = config.addToPlayerManager
        this.usingSurfaceView = config.usingSurfaceView
        this.enableMediaCodec = config.enableMediaCodec
        this.savingProgress = config.savingProgress
        this.disableAudioFocus = config.disableAudioFocus
        this.player = config.player
    }


    class Builder {

        private val config: PlayerConfig = PlayerConfig()

        /**
         * 开启缓存
         */
        fun enableCache(): Builder {
            config.isCache = true
            return this
        }

        /**
         * 添加到[VideoViewManager],如需集成到RecyclerView或ListView请开启此选项
         */
        fun addToPlayerManager(): Builder {
            config.addToPlayerManager = true
            return this
        }

        /**
         * 启用SurfaceView
         */
        fun usingSurfaceView(): Builder {
            config.usingSurfaceView = true
            return this
        }

        /**
         * 设置自动旋转
         */
        fun autoRotate(): Builder {
            config.autoRotate = true
            return this
        }

        /**
         * 开启循环播放
         */
        fun setLooping(): Builder {
            config.isLooping = true
            return this
        }

        /**
         * 开启硬解码，只对IjkPlayer有效
         */
        fun enableMediaCodec(): Builder {
            config.enableMediaCodec = true
            return this
        }

        /**
         * 设置自定义播放核心
         */
        fun setMediaPlayer(player: IPlayer): Builder {
            config.player = player
            return this
        }

        /**
         * 保存播放进度
         */
        fun savingProgress(): Builder {
            config.savingProgress = true
            return this
        }

        /**
         * 关闭AudioFocus监听
         */
        fun disableAudioFocus(): Builder {
            config.disableAudioFocus = true
            return this
        }

        fun build(): PlayerConfig {
            return PlayerConfig(config)
        }
    }
}