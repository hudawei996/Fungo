package com.fungo.imagego

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import com.fungo.imagego.create.ImageGoFactory
import com.fungo.imagego.create.ImageGoStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.progress.ProgressEngine
import com.fungo.imagego.utils.ImageGoUtils

/**
 * @author Pinger
 * @since 3/28/18 10:50 AM
 */
class ImageManager {

    companion object {
        private var mInstance: ImageManager? = null

        val instance: ImageManager
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = ImageManager()
                }
                return mInstance!!
            }
    }


    private lateinit var mImageStrategy: ImageGoStrategy

    /** 初始化加载策略 */
    fun setImageGoFactory(factory: ImageGoFactory) {
        mImageStrategy = factory.create()
    }


    /** 普通图片 */
    fun loadImage(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, null)
    }

    /** 加载图片，默认根据图片后缀加载GIF图片 */
    fun loadImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
        if (!TextUtils.isEmpty(url)) {
            if (url!!.endsWith(".gif") || url.endsWith(".GIF")) {
                mImageStrategy.loadGifImage(url, imageView, listener)
            } else {
                mImageStrategy.loadImage(url, imageView, listener)
            }
        }
    }

    /** 加载图片，带进度条 */
    fun loadImageWithProgress(url: String?, imageView: ImageView?, listener: OnProgressListener) {
        ProgressEngine.addProgressListener(listener)
        loadImage(url, imageView, null)
    }


    /** GIF图片 */
    fun loadGifImage(url: String?, imageView: ImageView?) {
        mImageStrategy.loadGifImage(url, imageView)
    }

    /** GIF图片，带进度条 */
    fun loadGifImageWithProgress(url: String?, imageView: ImageView?, listener: OnProgressListener) {
        ProgressEngine.addProgressListener(listener)
        loadGifImage(url, imageView)
    }

    /** 圆角图片 */
    fun loadRoundImage(url: String?, imageView: ImageView?, roundRadius: Float) {
        mImageStrategy.loadRoundImage(url, imageView, ImageGoUtils.dp2px(imageView?.context, roundRadius).toFloat())
    }

    /** 灰色图片 */
    fun loadGrayImage(url: String?, imageView: ImageView?) {
        mImageStrategy.loadGrayImage(url, imageView)
    }

    /** 模糊图片 */
    fun loadBlurImage(url: String?, imageView: ImageView?, blurRadius: Float) {
        mImageStrategy.loadBlurImage(url, imageView, blurRadius)
    }

    /** 圆形图片 */
    fun loadCircleImage(url: String?, imageView: ImageView?) {
        loadCircleImage(url, imageView, 0f, 0)
    }

    /** 加载圆形图片，指定图片大小 */
    fun loadCircleImage(url:String,imageView: ImageView?,width:Int,height:Int){
        mImageStrategy.loadCircleImage(url,imageView,width,height)
    }

    /** 圆形图片，带边框 */
    fun loadCircleImage(url: String?, imageView: ImageView?, borderWidth: Float, borderColor: Int) {
        mImageStrategy.loadCircleImage(url, imageView, ImageGoUtils.dp2px(imageView?.context, borderWidth), borderColor)
    }

    /** 保存图片到本地 */
    fun saveImage(context: Context?, url: String?, listener: OnImageSaveListener?) {
        mImageStrategy.saveImage(context, url, listener)
    }

    /** 清除图片缓存 */
    fun clearImageCache(context: Context?) {
        mImageStrategy.clearImageCache(context)
    }

    /** 图片缓存大小 */
    fun getImageCacheSize(context: Context?): String {
        return mImageStrategy.getCacheSize(context)
    }
}