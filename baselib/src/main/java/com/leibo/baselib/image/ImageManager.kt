package org.fungo.baselib.image

import android.text.TextUtils
import android.widget.ImageView
import com.leibo.baselib.image.strategy.ImageModel
import com.leibo.baseuilib.utils.ViewUtils

/**
 * @author Pinger
 * @since 3/28/18 10:50 AM
 */
class ImageManager {

    private var mLoadModel = ImageModel.MODEL_GLIDE

    companion object {  //  伴生对象获取单利
        private var mInstance: ImageManager? = null
        private var mImageStrategy: BaseImageStrategy? = null

        val instance: ImageManager
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = ImageManager()
                }
                checkStrategy()
                return mInstance!!
            }

        private fun checkStrategy() {
            if (mImageStrategy == null) {
                // 根据ImageModel生产策略模式
                mImageStrategy = GlideImageLoaderStrategy()
            }
        }
    }


    fun loadImage(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, null)
    }

    fun loadImage(url: String?, imageView: ImageView?, listener: ImageListener?) {
        if (!TextUtils.isEmpty(url)) {
            if (url!!.endsWith(".gif") || url.endsWith(".GIF")) {
                mImageStrategy!!.loadGifImage(url, imageView, listener)
            } else {
                mImageStrategy!!.loadImage(url, imageView, listener)
            }
        }
    }


    fun loadRoundImage(url: String?, imageView: ImageView?, roundRadius: Int) {
        mImageStrategy!!.loadRoundImage(url, imageView, ViewUtils.dp2px(imageView?.context, roundRadius))
    }

    fun loadGrayImage(url: String?, imageView: ImageView?) {
        mImageStrategy!!.loadGrayImage(url, imageView)
    }

    fun loadBlurImage(url: String?, imageView: ImageView?, blurRadius: Float) {
        mImageStrategy!!.loadBlurImage(url, imageView, blurRadius)
    }

    fun loadCircleImage(url: String?, imageView: ImageView?) {
        loadCircleImage(url, imageView, 0, 0)
    }

    fun loadCircleImage(url: String?, imageView: ImageView?, borderWidth: Int, borderColor: Int) {
        mImageStrategy!!.loadCircleImage(url, imageView, ViewUtils.dp2px(imageView?.context, borderWidth), borderColor)
    }

    /**
     * 设置图片加载模式
     */
    fun setLoadModel(model: Int) {
        this.mLoadModel = model
    }
}