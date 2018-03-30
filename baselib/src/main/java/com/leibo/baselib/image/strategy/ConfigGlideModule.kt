package com.leibo.baselib.image.strategy

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule
import com.leibo.baselib.image.progress.ProgressModelLoaderFactory
import java.io.InputStream

/**
 * @author Pinger
 * @since 3/30/18 12:56 PM
 *
 * Glide 配置
 */
@GlideModule
class ConfigGlideModule : LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(String::class.java, InputStream::class.java, ProgressModelLoaderFactory())
    }
}