package com.leibo.baselib.image.progress

import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import java.io.InputStream

/**
 * @author Pinger
 * @since 3/30/18 12:59 PM
 */
class ProgressModelLoaderFactory : ModelLoaderFactory<String, InputStream> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
        return ProgressModelLoader(listener)
    }

    override fun teardown() {
    }

    val listener = object : ProgressListener {
        override fun progress(bytesRead: Long, contentLength: Long, isDone: Boolean) {
        }

    }
}