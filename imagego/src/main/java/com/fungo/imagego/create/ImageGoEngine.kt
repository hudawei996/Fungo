package com.fungo.imagego.create

import android.graphics.Color
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @author Pinger
 * @since 3/28/18 3:23 PM
 *
 * 图片加载库的配置引擎，封装原始接口，进行转换
 */
class ImageGoEngine(private val builder: Builder) {

    /** 解析配置 */
    fun parseBuilder(config: ImageGoEngine): Builder {
        val builder = Builder()
        builder.placeHolderResId = config.getPlaceHolderResId()
        builder.errorResId = config.getErrorResId()
        builder.isCrossFade = config.isCrossFade()
        builder.size = config.getSize()
        builder.tag = config.getTag()
        builder.scaletype = config.getScaleType()
        builder.asGif = config.isAsGif()
        builder.asBitmap = config.isAsBitmap()
        builder.skipMemoryCache = config.isSkipMemoryCache()
        builder.diskCacheStrategy = config.getDiskCacheStrategy()
        builder.thumbnail = config.getThumbnail()
        builder.thumbnailUrl = config.getThumbnailUrl()
        builder.isCircleTransform = config.isCircleTransform()
        builder.isBlurTransform = config.isBlurTransform()
        builder.isGrayScaleTransform = config.isGrayScaleTransform()
        builder.isRoundTransform = config.isRoundTransform()
        builder.blurRadius = config.getBlurRadius()
        builder.roundRadius = config.getRoundRadius()
        builder.borderColor = config.getBorderColor()
        builder.borderWidth = config.getBorderWidth()
        return builder
    }

    fun getPlaceHolderResId(): Int {
        return builder.placeHolderResId
    }

    fun getErrorResId(): Int {
        return builder.errorResId
    }

    fun isCrossFade(): Boolean {
        return builder.isCrossFade
    }


    fun getSize(): OverrideSize? {
        return builder.size
    }

    fun getTag(): String? {
        return builder.tag
    }

    fun getScaleType(): ScaleType {
        return builder.scaletype
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

    fun isCircleTransform(): Boolean {
        return builder.isCircleTransform
    }

    fun isBlurTransform(): Boolean {
        return builder.isBlurTransform
    }

    fun isGrayScaleTransform(): Boolean {
        return builder.isGrayScaleTransform
    }

    fun isRoundTransform(): Boolean {
        return builder.isRoundTransform
    }

    fun getBlurRadius(): Float {
        return builder.blurRadius
    }

    fun getRoundRadius(): Float {
        return builder.roundRadius
    }

    fun getBorderColor(): Int {
        return builder.borderColor
    }

    fun getBorderWidth(): Float {
        return builder.borderWidth
    }


    /**
     * Builder类
     */
    class Builder {
        var placeHolderResId = 0
        var errorResId = 0
        var isCrossFade = true
        var size: OverrideSize? = null
        var scaletype = ScaleType.CENTER_CROP
        var asGif: Boolean = false
        var asBitmap: Boolean = false
        var skipMemoryCache: Boolean = false
        var diskCacheStrategy = DiskCache.AUTOMATIC
        var priority = LoadPriority.NORMAL
        var thumbnail: Float = 0f
        var thumbnailUrl: String? = null
        var tag: String? = null

        var isCircleTransform: Boolean = false   // 圆形图片
        var isRoundTransform: Boolean = false    // 圆角展示
        var isGrayScaleTransform: Boolean = false// 灰色展示
        var isBlurTransform: Boolean = false     // 高斯模糊

        var blurRadius: Float = 20f              // 高斯模糊程度
        var roundRadius: Float = 12f
        var borderWidth: Float = 0f
        var borderColor: Int = Color.TRANSPARENT


        fun setPlaceHolderResId(placeHolderResId: Int): Builder {
            this.placeHolderResId = placeHolderResId
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

        fun setSize(size: OverrideSize): Builder {
            this.size = size
            return this
        }

        fun setScaleType(scaleType: ScaleType): Builder {
            this.scaletype = scaleType
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

        fun isRoundTransform(isRoundTransform: Boolean): Builder {
            this.isRoundTransform = isRoundTransform
            return this
        }

        fun isGrayScaleTransform(isGrayScaleTransform: Boolean): Builder {
            this.isGrayScaleTransform = isGrayScaleTransform
            return this
        }

        fun isBlurTransform(isBlurTransform: Boolean): Builder {
            this.isBlurTransform = isBlurTransform
            return this
        }

        fun isCircleTransform(isCircleTransform: Boolean): Builder {
            this.isCircleTransform = isCircleTransform
            return this
        }

        fun setBlurRadius(blurRadius: Float): Builder {
            this.blurRadius = blurRadius
            return this
        }

        fun setRoundRadius(roundRadius: Float): Builder {
            this.roundRadius = roundRadius
            return this
        }

        fun setBorderColor(borderColor: Int): Builder {
            this.borderColor = borderColor
            return this
        }

        fun setBorderWidth(borderWidth: Float): Builder {
            this.borderWidth = borderWidth
            return this
        }

        fun build(): ImageGoEngine {
            return ImageGoEngine(this)
        }
    }

    /**
     * 图片最终显示在ImageView上的宽高像素
     */
    class OverrideSize(val width: Int, val height: Int)


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

    /**
     * 图片裁剪类型
     */
    enum class ScaleType {
        FIT_CENTER,    // 自适应控件, 不剪裁
        CENTER_CROP,   // 以填满整个控件为目标,等比缩放,超过控件时将被裁剪
        CENTER_INSIDE, // 以完整显示图片为目标, 不剪裁 ,当显示不下的时候将缩放
        CIRCLE_CROP    // 圆形裁剪
    }
}