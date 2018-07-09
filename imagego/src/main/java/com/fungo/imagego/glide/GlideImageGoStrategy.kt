package com.fungo.imagego.glide

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.fungo.imagego.create.ImageGoStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.utils.ImageGoConstant
import com.fungo.imagego.utils.ImageGoUtils
import java.io.File
import java.io.FileOutputStream


/**
 * @author Pinger
 * @since 3/28/18 2:16 PM
 *
 * 使用Glide加载图片策略
 */
class GlideImageGoStrategy : ImageGoStrategy {

    /** 默认的配置,可以手动配置 */
    private val defaultConfig = GlideConfig
            .Builder()
            .setAsBitmap(true)
            .setPlaceHolderDrawable(ColorDrawable(Color.parseColor(ImageGoConstant.IMAGE_PLACE_HOLDER_COLOR)))
            .setDiskCacheStrategy(GlideConfig.DiskCache.AUTOMATIC)
            .setPriority(GlideConfig.LoadPriority.NORMAL)
            .isCrossFade(true)
            .build()


    override fun loadImage(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, null)
    }

    override fun loadImage(url: String?, placeholder: Int, imageView: ImageView?) {
        loadImage(url, imageView, defaultConfig.parseBuilder(defaultConfig)
                .setPlaceHolderResId(placeholder).build(), null)
    }


    override fun loadImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
        loadImage(url, imageView, defaultConfig, listener)
    }

    override fun loadGifImage(url: String?, imageView: ImageView?) {
        loadGifImage(url, imageView, null)
    }

    override fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?) {
        loadImage(url, imageView, defaultConfig.parseBuilder(defaultConfig)
                .setAsGif(true).setAsBitmap(false).build(), null)
    }

    override fun loadGifImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
        loadImage(url, imageView, defaultConfig.parseBuilder(defaultConfig)
                .setAsGif(true).setAsBitmap(false).build(), listener)
    }

    override fun loadImage(obj: Any?, imageView: ImageView?) {
        loadImage(obj, imageView, defaultConfig, null)
    }


    override fun loadImageNoFade(url: String?, imageView: ImageView?) {
        loadImage(url, imageView, defaultConfig.parseBuilder(defaultConfig)
                .isCrossFade(false).build(), null)
    }

    override fun saveImage(context: Context?, url: String?, listener: OnImageSaveListener?) {
        ImageGoUtils.runOnSubThread(Runnable {
            try {
                if (context != null && !TextUtils.isEmpty(url)) {
                    val suffix = if (ImageGoUtils.isGif(url)) {
                        "${System.currentTimeMillis()}.gif"
                    } else {
                        "${System.currentTimeMillis()}.jpg"
                    }

                    val destFile = File(ImageGoUtils.getImageSavePath(context) + suffix)
                    val imageFile = download(context, url!!)
                    val isCopySuccess = ImageGoUtils.copyFile(imageFile, destFile)

                    // 最后通知图库更新
                    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(destFile)))
                    ImageGoUtils.runOnUIThread(Runnable {
                        if (isCopySuccess) {
                            listener?.onSaveSuccess("图片已保存至 " + ImageGoUtils.getImageSavePath(context))
                        } else {
                            listener?.onSaveFail("保存失败")
                        }
                    })
                }
            } catch (e: Exception) {
                ImageGoUtils.runOnUIThread(Runnable {
                    listener?.onSaveFail("保存失败")
                })
            }
        })
    }

    override fun saveImage(context: Context?, bitmap: Bitmap?, listener: OnImageSaveListener?) {
        ImageGoUtils.runOnSubThread(Runnable {
            try {
                val destFile = File(ImageGoUtils.getImageSavePath(context) + System.currentTimeMillis() + ".jpg")
                val fos = FileOutputStream(destFile)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                // 最后通知图库更新
                context?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(destFile)))
                ImageGoUtils.runOnUIThread(Runnable {
                    listener?.onSaveSuccess("图片已保存至 " + ImageGoUtils.getImageSavePath(context))
                })
            } catch (e: Exception) {
                ImageGoUtils.runOnUIThread(Runnable {
                    listener?.onSaveFail("保存失败")
                })
            }
        })
    }


    override fun loadBitmapImage(context: Context?, url: String?, listener: OnImageListener?) {
        if (context == null || TextUtils.isEmpty(url)) {
            listener?.onFail("context or url is null...")
            return
        }
        ImageGoUtils.runOnSubThread(Runnable {
            val bitmap = Glide.with(context).asBitmap().load(url).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
            ImageGoUtils.runOnUIThread(Runnable {
                listener?.onSuccess(bitmap)
            })
        })
    }

    override fun clearImageDiskCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearDiskCache()
        }
    }

    /** 清除内存缓存，只能在主线程调用本方法 */
    override fun clearImageMemoryCache(context: Context?) {
        if (context != null) {
            Glide.get(context).clearMemory()
        }
    }

    override fun clearImageCache(context: Context?) {
        ImageGoUtils.showToast(context, "正在清除缓存...")
        clearImageMemoryCache(context)
        ImageGoUtils.runOnSubThread(Runnable {
            clearImageDiskCache(context)
            ImageGoUtils.showToast(context, "清除成功")
        })
    }

    override fun getCacheSize(context: Context?): String {
        return ImageGoUtils.getImageCacheSize(context)
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

    private fun loadImage(obj: Any?, imageView: ImageView?, config: GlideConfig?, listener: OnImageListener?) {
        if (obj == null) {
            listener?.onFail("GlideImageGoStrategy：image request url is null...")
            return
        }

        if (obj is String) {
            if (TextUtils.isEmpty(obj)) {
                listener?.onFail("GlideImageGoStrategy：image request url is null...")
                return
            }
        }

        if (imageView == null) {
            listener?.onFail("GlideImageGoStrategy：imageView is null...")
            return
        }

        val context = imageView.context
        if (context == null) {
            listener?.onFail("GlideImageGoStrategy：context is null...")
            return
        }
        val glideConfig: GlideConfig = config ?: defaultConfig
        try {
            when {
                glideConfig.isAsGif() -> {
                    val gifBuilder = Glide.with(context).asGif().load(obj)
                    val builder = buildGift(context, obj, glideConfig, gifBuilder, listener)
                    // 使用clone方法复用builder，不会请求网络
                    builder.clone().apply(buildOptions(obj, glideConfig)).into(imageView)
                }
                glideConfig.isAsBitmap() -> {
                    val bitmapBuilder = Glide.with(context).asBitmap().load(obj)
                    val builder = buildBitmap(context, obj, glideConfig, bitmapBuilder, listener)
                    builder.clone().apply(buildOptions(obj, glideConfig)).into(imageView)
                }
            }
        } catch (e: Exception) {
            listener?.onFail("GlideImageGoStrategy：load image exception: " + e.message)
            imageView.setImageResource(glideConfig.getErrorResId())
        }
    }


    /**
     * 设置bitmap属性
     */
    private fun buildBitmap(context: Context, obj: Any, glideConfig: GlideConfig, bitmapBuilder: RequestBuilder<Bitmap>, listener: OnImageListener?): RequestBuilder<Bitmap> {
        var builder = bitmapBuilder
        // 渐变展示
        if (glideConfig.isCrossFade()) {
            builder.transition(BitmapTransitionOptions.withCrossFade())
        }

        builder.listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                listener?.onFail(e?.message ?: "GlideImageGoStrategy：image load fail")
                return false
            }

            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                listener?.onSuccess(resource)
                return false
            }
        })

        // 缩略图大小
        if (glideConfig.getThumbnail() > 0f) {
            builder.thumbnail(glideConfig.getThumbnail())
        }


        // 缩略图请求
        if (!TextUtils.isEmpty(glideConfig.getThumbnailUrl())) {
            val thumbnailBuilder = Glide.with(context).asBitmap().load(obj).thumbnail(Glide.with(context).asBitmap().load(glideConfig.getThumbnailUrl()))
            builder = thumbnailBuilder
        }
        return builder
    }


    /**
     * 设置Gift属性
     */
    private fun buildGift(context: Context, obj: Any, glideConfig: GlideConfig, gifBuilder: RequestBuilder<GifDrawable>, listener: OnImageListener?): RequestBuilder<GifDrawable> {
        var builder = gifBuilder

        // 渐变展示
        if (glideConfig.isCrossFade()) {
            builder.transition(DrawableTransitionOptions.withCrossFade())
        }

        builder.listener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                listener?.onFail(e?.message ?: "GlideImageGoStrategy：Gif load fail")
                return false
            }

            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                listener?.onSuccess(resource?.firstFrame)
                return false
            }
        })

        // 缩略图大小
        if (glideConfig.getThumbnail() > 0f) {
            builder.thumbnail(glideConfig.getThumbnail())
        }

        // 缩略图请求
        if (!TextUtils.isEmpty(glideConfig.getThumbnailUrl())) {
            val thumbnailBuilder = Glide.with(context).asGif().load(obj).thumbnail(Glide.with(context).asGif().load(glideConfig.getThumbnailUrl()))
            builder = thumbnailBuilder
        }
        return builder
    }

    /**
     * 设置图片加载选项并且加载图片
     */
    private fun buildOptions(obj: Any, glideConfig: GlideConfig): RequestOptions {
        val options = RequestOptions()

        // 是否跳过内存缓存
        options.diskCacheStrategy(glideConfig.getDiskCacheStrategy().strategy)

        // 占位图
        when {
            glideConfig.getPlaceHolderResId() != 0 ->
                options.placeholder(glideConfig.getPlaceHolderResId())
            glideConfig.getErrorResId() != 0 ->
                options.error(glideConfig.getErrorResId())
                        .fallback(glideConfig.getErrorResId())
            glideConfig.getPlaceHolderDrawable() != null ->
                options.placeholder(glideConfig.getPlaceHolderDrawable())
                        .error(glideConfig.getPlaceHolderDrawable())
        }

        // 优先级和内存跳过
        options
                .priority(glideConfig.getPriority().strategy)
                .skipMemoryCache(glideConfig.isSkipMemoryCache())

        // Tag
        val tag = glideConfig.getTag()
        if (tag != null) {
            options.signature(ObjectKey(tag))
        } else {
            options.signature(ObjectKey(obj.toString()))
        }

        return options
    }

    /** 获取图片的缓存文件 */
    override fun download(context: Context, url: String): File {
        return Glide.with(context).download(url).submit().get()
    }
}