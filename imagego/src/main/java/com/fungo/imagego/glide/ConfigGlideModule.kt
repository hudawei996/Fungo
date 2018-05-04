package com.fungo.imagego.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.fungo.imagego.progress.ProgressEngine
import java.io.InputStream


/**
 * @author Pinger
 * @since 3/30/18 12:56 PM
 *
 * Glide 配置
 */
@GlideModule
class ConfigGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(ProgressEngine.getOkHttpClient()))
    }
}