package com.fungo.imagego.picasso

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.fungo.imagego.create.ImageGoStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import java.io.File

/**
 * @author Pinger
 * @since 2018/5/13 下午4:13
 *
 */
class PicassoImageGoStrategy : ImageGoStrategy {
    override fun loadBitmapImage(context: Context?, url: String?, listener: OnImageListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadImageNoFade(url: String?, imageView: ImageView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun loadImage(url: String?, imageView: ImageView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadImage(url: String?, placeholder: Int, imageView: ImageView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadGifImage(url: String?, imageView: ImageView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadGifImage(url: String?, imageView: ImageView?, listener: OnImageListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveImage(context: Context?, url: String?, listener: OnImageSaveListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadImage(obj: Any?, imageView: ImageView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearImageDiskCache(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearImageMemoryCache(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearImageCache(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCacheSize(context: Context?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resumeRequests(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pauseRequests(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun download(context: Context, url: String): File {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}