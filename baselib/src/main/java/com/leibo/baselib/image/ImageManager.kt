package org.fungo.baselib.image

import android.widget.ImageView
import com.leibo.baselib.image.ImageModel

/**
 * @author Pinger
 * @since 3/28/18 10:50 AM
 */
class ImageManager {

    private var mImageModel = ImageModel.MODEL_GLIDE

    private var mInstance: ImageManager? = null
    private var mImageStrategy: BaseImageStrategy? = null
    val instance: ImageManager
        get() {
            if (mInstance == null) {
                synchronized(ImageManager::class.java) {
                    if (mInstance == null) {
                        mInstance = ImageManager()
                    }
                }
            }
            return mInstance!!
        }


    /**
     * 设置图片加载模式
     */
    fun setImageModel(model: Int) {
        this.mImageModel = model
    }


    fun loadImage(url: String, imageView: ImageView) {
        loadImage(url, imageView, null)
    }

    fun loadImage(url: String, imageView: ImageView, listener: ImageListener?) {
        checkStrategy()
        mImageStrategy!!.loadImage(url,imageView,listener)


    }


    /**
     * 检查策略模式
     */
    private fun checkStrategy() {
        if (mImageStrategy == null) {
            // TODO 根据ImageModel生产策略模式
            mImageStrategy = GlideImageLoaderStrategy()
        }
    }

}