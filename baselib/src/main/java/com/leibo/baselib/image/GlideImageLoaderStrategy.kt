package org.fungo.baselib.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.leibo.baselib.R
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 2:16 PM
 *
 * 使用Glide加载图片策略
 */
class GlideImageLoaderStrategy : BaseImageStrategy {

    /**
     * 默认的配置,可以手动配置
     */
    private val defaultConfiguration = ImageConfiguration.Builder()
            .setCropType(ImageConfiguration.CENTER_CROP)
            .setAsBitmap(true)
            .setPlaceHolderResId(R.drawable.ic_placeholder)
            .setErrorResId(R.drawable.ic_placeholder)
            .setDiskCacheStrategy(ImageConfiguration.DiskCache.AUTOMATIC)
            .setPrioriy(ImageConfiguration.LoadPriority.NORMAL)
            .build()


    override fun loadImage(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, null)
    }

    override fun loadImage(url: String?, placeholder: Int, imageView: ImageView?) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration).build(), null)
    }

    override fun loadImage(url: String?, imageView: ImageView?, listener: ImageListener?) {
        loadImage(url, imageView, null, listener)
    }

    override fun loadGifImage(url: String?, imageView: ImageView?) {
        loadGifImage(url, imageView, null)
    }

    override fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .setAsGif(true).build(), null)
    }

    override fun loadGifImage(url: String?, imageView: ImageView?, listener: ImageListener?) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .setAsGif(true).build(), listener)
    }

    override fun loadImage(file: File?, imageView: ImageView?) {
        loadImage(file, imageView, null, null)
    }

    override fun loadImage(bitmap: Bitmap?, imageView: ImageView?) {
        loadImage(bitmap, imageView, null, null)
    }

    override fun loadImage(uri: Uri?, imageView: ImageView?) {
        loadImage(uri, imageView, null, null)
    }

    override fun loadImage(resId: Int?, imageView: ImageView?) {
        loadImage(resId, imageView, null, null)
    }

    override fun loadImage(drawable: Drawable?, imageView: ImageView?) {
        loadImage(drawable, imageView, null, null)
    }

    override fun loadImage(obj: Any?, imageView: ImageView?) {
        loadImage(obj, imageView, null, null)
    }

    override fun saveImage(context: Context?, url: String?, listener: ImageListener?) {
    }

    override fun saveImage(context: Context?, url: String?, savePath: String, saveFileName: String,
                           listener: ImageListener?) {
    }

    override fun clearImageDiskCache(context: Context?) {
    }

    override fun clearImageMemoryCache(context: Context?) {
    }

    override fun getCacheSize(context: Context?): String {
        return ""
    }


    private fun loadImage(obj: Any?, imageView: ImageView?, config: ImageConfiguration?, listener: ImageListener?) {
        if (obj == null) {
            listener?.onFail("GlideImageLoaderStrategy：image request url is null...")
            return
        }

        if (obj is String) {
            if (TextUtils.isEmpty(obj)) {
                listener?.onFail("GlideImageLoaderStrategy：image request url is null...")
                return
            }
        }

        if (imageView == null) {
            listener?.onFail("GlideImageLoaderStrategy：imageView is null...")
            return
        }

        val glideConfig: ImageConfiguration = config ?: defaultConfiguration


    }


}