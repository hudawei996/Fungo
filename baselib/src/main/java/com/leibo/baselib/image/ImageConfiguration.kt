package org.fungo.baselib.image

import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * @author Pinger
 * @since 3/28/18 3:23 PM
 *
 * 图片加载库的相关配置
 */
class ImageConfiguration(builder: ImageConfiguration.Builder) {

    companion object {
        const val CENTER_CROP = 0      // 圆形图片
        const val CENTER_FITXY = 1     // 填充图片
    }

    private var isCrossFade = true            // 是否淡入淡出动画
    private var crossDuration = 2000        // 淡入淡出动画持续的时间

    private var placeHolderResId = 0        // 默认占位资源
    private var errorResId = 0              // 错误时显示的资源

    private var size: OverrideSize? = null   // 图片最终显示在ImageView上的宽高度像素
    private var cropType = CENTER_CROP       // 裁剪类型,默认为中部裁剪

    private var asGif: Boolean = false       // Gif模式加载
    private var asBitmap: Boolean = true     // 常规图片加载
    private var skipMemoryCache: Boolean = false                    // 内存缓存
    private var diskCacheStrategy: DiskCache = DiskCache.AUTOMATIC  // 硬盘缓存
    private var priority: LoadPriority = LoadPriority.HIGH
    private var thumbnail: Float = 0.8f
    private var thumbnailUrl: String? = null

    private var isCircleTransform: Boolean = false   // 圆形展示
    private var isRoundTransform: Boolean = false    // 圆角展示
    private var isGrayScaleTransform: Boolean = false// 灰色展示
    private var isBlurTransform: Boolean = false     // 高斯模糊


    init {
        this.placeHolderResId = builder.placeHolderResId
        this.errorResId = builder.errorResId
        this.isCrossFade = builder.isCrossFade
        this.crossDuration = builder.crossDuration
        this.size = builder.size
        this.cropType = builder.cropType
        this.asGif = builder.asGif
        this.asBitmap = builder.asBitmap
        this.skipMemoryCache = builder.skipMemoryCache
        this.diskCacheStrategy = builder.diskCacheStrategy
        this.thumbnail = builder.thumbnail
        this.thumbnailUrl = builder.thumbnailUrl
        this.priority = builder.priority
        this.thumbnailUrl = builder.thumbnailUrl
        this.isBlurTransform = builder.isBlurTransform
        this.isGrayScaleTransform = builder.isGrayScaleTransform
        this.isCircleTransform = builder.isCircleTransform
        this.isRoundTransform = builder.isRoundTransform
    }


    /**
     * 硬盘缓存策略
     */
    enum class DiskCache(strategy: DiskCacheStrategy) {
        NONE(DiskCacheStrategy.NONE),  // 无缓存
        AUTOMATIC(DiskCacheStrategy.AUTOMATIC),  // 自动选择
        RESOURCE(DiskCacheStrategy.RESOURCE),
        DATA(DiskCacheStrategy.DATA),
        ALL(DiskCacheStrategy.ALL);
    }

    /**
     * 加载优先级策略
     */
    enum class LoadPriority(strategy: Priority) {
        LOW(Priority.LOW),
        NORMAL(Priority.NORMAL),
        HIGH(Priority.HIGH),
        IMMEDIATE(Priority.IMMEDIATE);
    }


    fun parseBuilder(config: ImageConfiguration): Builder {
        val builder = Builder()
        builder.placeHolderResId = config.placeHolderResId
        builder.errorResId = config.errorResId
        builder.isCrossFade = config.isCrossFade
        builder.crossDuration = config.crossDuration
        builder.size = config.size
        builder.cropType = config.cropType
        builder.asGif = config.asGif
        builder.asBitmap = config.asBitmap
        builder.skipMemoryCache = config.skipMemoryCache
        builder.diskCacheStrategy = config.diskCacheStrategy
        builder.thumbnail = config.thumbnail
        builder.thumbnailUrl = config.thumbnailUrl
        builder.isBlurTransform = config.isBlurTransform
        builder.isGrayScaleTransform = config.isGrayScaleTransform
        builder.isCircleTransform = config.isCircleTransform
        builder.isRoundTransform = config.isRoundTransform
        return builder
    }

    fun getPlaceHolderResId(): Int {
        return placeHolderResId
    }

    fun getErrorResId(): Int {
        return errorResId
    }

    fun isCrossFade(): Boolean {
        return isCrossFade
    }

    fun getCrossFadeDuration(): Int {
        return crossDuration
    }

    fun getSize(): OverrideSize? {
        return size
    }

    fun getCropType(): Int {
        return cropType
    }

    fun isAsGif(): Boolean {
        return asGif
    }

    fun isAsBitmap(): Boolean {
        return asBitmap
    }

    fun isSkipMemoryCache(): Boolean {
        return skipMemoryCache
    }

    fun getDiskCacheStrategy(): DiskCache {
        return diskCacheStrategy
    }

    fun getPriority(): LoadPriority {
        return priority
    }

    fun getThumbnail(): Float {
        return thumbnail
    }

    fun getThumbnailUrl(): String? {
        return thumbnailUrl
    }

    fun isBlurTransform(): Boolean {
        return isBlurTransform
    }

    fun isGrayScaleTransform(): Boolean {
        return isGrayScaleTransform
    }

    fun isCircleTransform(): Boolean {
        return isCircleTransform
    }

    fun isRoundTransform(): Boolean {
        return isRoundTransform
    }


    /**
     * 图片最终显示在ImageView上的宽高像素
     */
    class OverrideSize(val width: Int, val height: Int)


    /**
     * Builder类
     */
    class Builder {
        var placeHolderResId = 0
        var errorResId = 0
        var isCrossFade = true
        var crossDuration = 1200
        var size: OverrideSize? = null
        var cropType = 0
        var asGif: Boolean = false
        var asBitmap: Boolean = true
        var skipMemoryCache: Boolean = false
        var diskCacheStrategy = DiskCache.AUTOMATIC
        var priority = LoadPriority.NORMAL
        var thumbnail: Float = 0.8f
        var thumbnailUrl: String? = null

        var isCircleTransform: Boolean = false   // 圆形展示
        var isRoundTransform: Boolean = false    // 圆角展示
        var isGrayScaleTransform: Boolean = false// 灰色展示
        var isBlurTransform: Boolean = false     // 高斯模糊

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

        fun setCrossFadeDuration(crossDuration: Int): Builder {
            this.crossDuration = crossDuration
            return this
        }

        fun setSize(size: OverrideSize): Builder {
            this.size = size
            return this
        }

        fun setCropType(cropType: Int): Builder {
            this.cropType = cropType
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

        fun setPrioriy(priority: LoadPriority): Builder {
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


        fun isCircleTransform(isCircleTransform: Boolean): Builder {
            this.isCircleTransform = isCircleTransform
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


        fun build(): ImageConfiguration {
            return ImageConfiguration(this)
        }
    }


}