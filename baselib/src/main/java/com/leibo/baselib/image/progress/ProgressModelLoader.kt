package com.leibo.baselib.image.progress

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.signature.ObjectKey
import java.io.InputStream

/**
 * @author Pinger
 * @since 3/30/18 11:14 AM
 *
 * 进度加载器
 */
class ProgressModelLoader(private val listener: ProgressListener) : ModelLoader<String, InputStream> {

    override fun buildLoadData(model: String, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model), ProgressDataFetcher(model, listener))
    }

    override fun handles(model: String): Boolean {
        return false
    }
}