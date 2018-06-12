package com.fungo.imagego.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @author Pinger
 * @since 3/28/18 3:23 PM
 *
 * 图片加载库的配置引擎，封装原始接口，进行转换
 */
class GlideConfig(private val builder: Builder) {

    /** 解析配置 */
    fun parseBuilder(config: GlideConfig): Builder {
        val builder = Builder()
        builder.placeHolderResId = config.getPlaceHolderResId()
        builder.placeHolderDrawable = config.getPlaceHolderDrawable()
        builder.errorResId = config.getErrorResId()
        builder.isCrossFade = config.isCrossFade()
        builder.tag = config.getTag()
        builder.asGif = config.isAsGif()
        builder.asBitmap = config.isAsBitmap()
        builder.skipMemoryCache = config.isSkipMemoryCache()
        builder.diskCacheStrategy = config.getDiskCacheStrategy()
        builder.thumbnail = config.getThumbnail()
        builder.thumbnailUrl = config.getThumbnailUrl()
        return builder
    }

    fun getPlaceHolderResId(): Int {
        return builder.placeHolderResId
    }

    fun getPlaceHolderDrawable(): Drawable? {
        return builder.placeHolderDrawable
    }

    fun getErrorResId(): Int {
        return builder.errorResId
    }

    fun isCrossFade(): Boolean {
        return builder.isCrossFade
    }

    fun getTag(): String? {
        return builder.tag
    }


    fun isAsGif(): Boolean {
        return builder.asGif
    }

    fun isAsBitmap(): Boolean {
        return builder.asBitmap
    }

    fun isSkipMemoryCache(): Boolean {
        return builder.skipMemoryCache
    }

    fun getDiskCacheStrategy(): DiskCache {
        return builder.diskCacheStrategy
    }

    fun getPriority(): LoadPriority {
        return builder.priority
    }

    fun getThumbnail(): Float {
        return builder.thumbnail
    }

    fun getThumbnailUrl(): String? {
        return builder.thumbnailUrl
    }

    /**
     * Builder类
     */
    class Builder {
        var placeHolderResId = 0
        var placeHolderDrawable: Drawable? = null
        var errorResId = 0
        var isCrossFade = true
        var asGif: Boolean = false
        var asBitmap: Boolean = false
        var skipMemoryCache: Boolean = false
        var diskCacheStrategy = DiskCache.AUTOMATIC
        var priority = LoadPriority.NORMAL
        var thumbnail: Float = 0f
        var thumbnailUrl: String? = null
        var tag: String? = null

        fun setPlaceHolderResId(placeHolderResId: Int): Builder {
            this.placeHolderResId = placeHolderResId
            return this
        }

        fun setPlaceHolderDrawable(placeHolderDrawable: Drawable): Builder {
            this.placeHolderDrawable = placeHolderDrawable
            return this
        }

        fun setErrorResId(errorResId: Int): Builder {
            this.errorResId = errorResId
            return this
        }

        fun isCrossFade(isCrossFade: Boolean): Builder {
            this.isCrossFade = isCrossFade
            return this
        }

        fun setAsGif(asGif: Boolean): Builder {
            this.asGif = asGif
            return this
        }

        fun setAsBitmap(asBitmap: Boolean): Builder {
            this.asBitmap = asBitmap
            return this
        }

        fun isSkipMemoryCache(skipMemoryCache: Boolean): Builder {
            this.skipMemoryCache = skipMemoryCache
            return this
        }

        fun setDiskCacheStrategy(diskCacheStrategy: DiskCache): Builder {
            this.diskCacheStrategy = diskCacheStrategy
            return this
        }

        fun setTag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        fun setPriority(priority: LoadPriority): Builder {
            this.priority = priority
            return this
        }

        fun setThumbnail(thumbnail: Float): Builder {
            this.thumbnail = thumbnail
            return this
        }

        fun setThumbnailUrl(thumbnailUrl: String): Builder {
            this.thumbnailUrl = thumbnailUrl
            return this
        }

        fun build(): GlideConfig {
            return GlideConfig(this)
        }
    }


    /**
     * 硬盘缓存策略
     */
    enum class DiskCache(val strategy: DiskCacheStrategy) {
        NONE(DiskCacheStrategy.NONE),  // 无缓存
        AUTOMATIC(DiskCacheStrategy.AUTOMATIC),  // 自动选择
        RESOURCE(DiskCacheStrategy.RESOURCE),
        DATA(DiskCacheStrategy.DATA),
        ALL(DiskCacheStrategy.ALL)
    }

    /**
     * 加载优先级策略
     */
    enum class LoadPriority(val strategy: Priority) {
        LOW(Priority.LOW),
        NORMAL(Priority.NORMAL),
        HIGH(Priority.HIGH),
        IMMEDIATE(Priority.IMMEDIATE)
    }
}