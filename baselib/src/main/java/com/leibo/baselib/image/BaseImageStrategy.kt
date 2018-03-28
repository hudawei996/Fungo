package org.fungo.baselib.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 2:17 PM
 *
 * 图片加载策略基类，定义接口
 */
interface BaseImageStrategy {

    /**
     * 加载图片，使用默认的配置
     */
    fun loadImage(url: String?, imageView: ImageView?)

    /**
     * 加载图片，自定义展位图
     */
    fun loadImage(url: String?, placeholder: Int, imageView: ImageView?)

    /**
     * 加载图片，使用监听
     */
    fun loadImage(url: String?, imageView: ImageView?, listener: ImageListener?)

    /**
     * 加载Gif图片
     */
    fun loadGifImage(url: String?, imageView: ImageView?)

    /**
     * 加载Gif图片，自定义占位图
     */
    fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?)

    /**
     * 加载Gif图片，自使用监听
     */
    fun loadGifImage(url: String?, imageView: ImageView?, listener: ImageListener?)

    /**
     * 保存图片
     */
    fun saveImage(context: Context?, url: String?, listener: ImageListener?)

    /**
     * 保存图片，自定义路径
     */
    fun saveImage(context: Context?, url: String?, savePath: String, saveFileName: String, listener: ImageListener?)

    /**
     * 清除手机磁盘图片缓存
     */
    fun clearImageDiskCache(context: Context?)

    /**
     * 清除app图片内存缓存
     */
    fun clearImageMemoryCache(context: Context?)

    /**
     * 获取手机磁盘图片缓存大小
     */
    fun getCacheSize(context: Context?): String


    // 通过其他的资源，加载图片
    fun loadImage(file: File?, imageView: ImageView?)       // 加载本地文件
    fun loadImage(bitmap: Bitmap?, imageView: ImageView?)   // 加载Bitmap
    fun loadImage(uri: Uri?, imageView: ImageView?)         // 路由
    fun loadImage(resId: Int?, imageView: ImageView?)       // 加载本地资源
    fun loadImage(drawable: Drawable?, imageView: ImageView?) // 加载drawable
    fun loadImage(obj: Any?, imageView: ImageView?)             // 加载任意资源
}