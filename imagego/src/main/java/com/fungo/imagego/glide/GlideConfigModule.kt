package com.fungo.imagego.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.fungo.imagego.progress.ProgressEngine
import com.fungo.imagego.utils.ImageGoUtils
import java.io.InputStream


/**
 * @author Pinger
 * @since 3/30/18 12:56 PM
 *
 * Glide 配置
 */
@GlideModule
class GlideConfigModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(ProgressEngine.getOkHttpClient()))
    }

    /** 配置Glide常用属性 */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
        // 设置Glide缓存目录
        val cacheDir = ImageGoUtils.getImageCacheDir(context)

        val cache = DiskLruCacheWrapper.create(cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE.toLong())// 250 MB
        builder.setDiskCache { cache }
        // 设置memory和Bitmap池的大小
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize

        val customMemoryCacheSize = (1.2 * defaultMemoryCacheSize).toInt()
        val customBitmapPoolSize = (1.2 * defaultBitmapPoolSize).toInt()

        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))
    }
}