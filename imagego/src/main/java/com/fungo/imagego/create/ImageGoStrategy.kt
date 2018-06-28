package com.fungo.imagego.create


import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 2:17 PM
 *
 * 图片加载策略基类，定义接口
 */

interface ImageGoStrategy {

    /** 加载图片，使用默认的配置 */
    fun loadImage(url: String?, imageView: ImageView?)

    /** 加载图片，自定义展位图 */
    fun loadImage(url: String?, placeholder: Int, imageView: ImageView?)

    /** 加载图片，使用监听 */
    fun loadImage(url: String?, imageView: ImageView?, listener: OnImageListener?)

    /** 加载Gif图片 */
    fun loadGifImage(url: String?, imageView: ImageView?)

    /** 加载Gif图片，自定义占位图 */
    fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?)

    /** 加载Gif图片，自使用监听 */
    fun loadGifImage(url: String?, imageView: ImageView?, listener: OnImageListener?)

    /** 保存图片 */
    fun saveImage(context: Context?, url: String?, listener: OnImageSaveListener?)

    /** 加载图片，生成Bitmap */
    fun loadBitmapImage(context: Context?, url: String?): Bitmap?

    /**
     * 通过其他资源加载图片
     * 可以是File,Bitmap,URI,ResID,Drawable
     */
    fun loadImage(obj: Any?, imageView: ImageView?)

    /** 清除手机磁盘图片缓存 */
    fun clearImageDiskCache(context: Context?)

    /** 清除app图片内存缓存 */
    fun clearImageMemoryCache(context: Context?)

    /** 清除图片缓存 */
    fun clearImageCache(context: Context?)

    /** 获取手机磁盘图片缓存大小 */
    fun getCacheSize(context: Context?): String

    /** 恢复所有任务 */
    fun resumeRequests(context: Context?)

    /** 暂停所有加载任务 */
    fun pauseRequests(context: Context?)

    /** 下载图片 */
    fun download(context: Context, url: String): File

}
