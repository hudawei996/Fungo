package org.fungo.baselib.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.leibo.baselib.R
import com.leibo.baselib.image.transfmer.BlurTransformation
import com.leibo.baselib.image.transfmer.CircleTransformation
import com.leibo.baselib.image.transfmer.GrayScaleTransformation
import com.leibo.baselib.image.transfmer.RoundTransformation
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
            .setScaleType(ImageConfiguration.ScaleType.CENTER_CROP)
            .setAsBitmap(true)
            .setPlaceHolderResId(R.drawable.ic_placeholder)
            .setErrorResId(R.drawable.ic_placeholder)
            .setDiskCacheStrategy(ImageConfiguration.DiskCache.AUTOMATIC)
            .setPriority(ImageConfiguration.LoadPriority.NORMAL)
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
                .setAsGif(true).setAsBitmap(false).build(), null)
    }

    override fun loadGifImage(url: String?, imageView: ImageView?, listener: ImageListener?) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .setAsGif(true).setAsBitmap(false).build(), listener)
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

    override fun loadRoundImage(url: String?, imageView: ImageView?, roundRadius: Float) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .isRoundTransform(true).setRoundRadius(roundRadius).build(), null)
    }

    override fun loadBlurImage(url: String?, imageView: ImageView?, blurRadius: Float) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .isBlurTransform(true).setBlurRadius(blurRadius).build(), null)
    }

    override fun loadGrayImage(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .isGrayScaleTransform(true).build(), null)
    }

    override fun loadCircleImage(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .setScaleType(ImageConfiguration.ScaleType.CIRCLE_CROP).build(), null)
    }

    override fun loadCircleImage(url: String?, imageView: ImageView?, borderWidth: Float, borderColor: Int) {
        loadImage(url, imageView, defaultConfiguration.parseBuilder(defaultConfiguration)
                .isCircleTransform(true).setBorderWidth(borderWidth).setBorderColor(borderColor).build(), null)
    }


    override fun clearImageDiskCache(context: Context?) {
        if (context != null) {
            // TODO 子线程
            Glide.get(context).clearDiskCache()
        }
    }

    override fun clearImageMemoryCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearMemory()
        }
    }

    override fun clearImageCache(context: Context?) {
        clearImageMemoryCache(context)
        clearImageDiskCache(context)
    }

    override fun getCacheSize(context: Context?): String {
        return ""
    }

    override fun resumeRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).resumeRequests()
        }
    }

    override fun pauseRequests(context: Context?) {
        if (context != null) {
            Glide.with(context).pauseRequests()
        }
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

        val context = imageView.context
        if (context == null) {
            listener?.onFail("GlideImageLoaderStrategy：context is null...")
            return
        }

        val glideConfig: ImageConfiguration = config ?: defaultConfiguration

        try {
            val options = RequestOptions()
            val request = Glide.with(context)
            var builder: RequestBuilder<*>? = null

            // 图片类型
            when {
                glideConfig.isAsGif() -> builder = request.asGif().load(obj)       // gif加载
                glideConfig.isAsBitmap() -> builder = request.asBitmap().load(obj) // bitmap加载
                glideConfig.isCrossFade() -> builder = request.asBitmap().load(obj).transition(withCrossFade())
            }


            if (builder == null) {
                listener?.onFail("GlideImageLoaderStrategy：builder is null...")
                return
            }

            // 缩放类型
            when (glideConfig.getScaleType()) {
                ImageConfiguration.ScaleType.FIT_CENTER -> options.fitCenter()
                ImageConfiguration.ScaleType.CENTER_CROP -> options.centerCrop()
                ImageConfiguration.ScaleType.CENTER_INSIDE -> options.centerInside()
                ImageConfiguration.ScaleType.CIRCLE_CROP -> options.circleCrop()
            }

            // TODO 检查存储权限
            // 是否跳过内存缓存
            options.diskCacheStrategy(glideConfig.getDiskCacheStrategy().strategy)

            // transform
            when {
                glideConfig.isCircleTransform() -> options.transform(CircleTransformation(context, glideConfig.getBorderWidth(), glideConfig.getBorderColor()))
                glideConfig.isBlurTransform() -> options.transform(BlurTransformation(context, glideConfig.getBlurRadius()))
                glideConfig.isRoundTransform() -> options.transform(RoundTransformation(context, glideConfig.getRoundRadius()))
                glideConfig.isGrayScaleTransform() -> options.transform(GrayScaleTransformation(context))
            }

            options
                    .priority(glideConfig.getPriority().strategy)      // 优先级
                    .skipMemoryCache(glideConfig.isSkipMemoryCache())  // 是否跳过内存缓存
                    .placeholder(glideConfig.getPlaceHolderResId())    // 占位符
                    .error(glideConfig.getErrorResId())                // 错误占位符
                    .fallback(glideConfig.getErrorResId())             // 传入null时占位

            // 图片大小
            val size = glideConfig.getSize()
            if (size != null) {
                options.override(size.width, size.height)
            }

            // Tag
            val tag = glideConfig.getTag()
            if (tag != null) {
                options.signature(ObjectKey(tag))
            } else {
                options.signature(ObjectKey(obj.toString()))
            }

            // 缩略图大小
            if (glideConfig.getThumbnail() > 0f) {
                builder.thumbnail(glideConfig.getThumbnail())
            }

            // 缩略图请求
            if (!TextUtils.isEmpty(glideConfig.getThumbnailUrl())) {
                val thumbnailRequest = Glide.with(context).load(glideConfig.getThumbnailUrl())
                builder = Glide.with(context).load(obj)
                builder.thumbnail(thumbnailRequest)
            }
            builder.apply(options).into(imageView)
        } catch (e: Exception) {
            listener?.onFail("GlideImageLoaderStrategy：load image exception: " + e.message)
            imageView.setImageResource(glideConfig.getErrorResId())
        }
    }
}