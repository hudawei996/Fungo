package org.fungo.baselib.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.fungo.imagego.listener.ImageListener
import com.fungo.imagego.listener.ImageSaveListener
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 2:17 PM
 *
 * 图片加载策略基类，定义接口
 */
interface BaseImageStrategy {

    /** 加载图片，使用默认的配置 */
    fun loadImage(url: String?, imageView: ImageView?)

    /** 加载图片，自定义展位图 */
    fun loadImage(url: String?, placeholder: Int, imageView: ImageView?)

    /** 加载图片，使用监听 */
    fun loadImage(url: String?, imageView: ImageView?, listener: ImageListener?)

    /** 加载Gif图片 */
    fun loadGifImage(url: String?, imageView: ImageView?)

    /** 加载Gif图片，自定义占位图 */
    fun loadGifImage(url: String?, placeholder: Int, imageView: ImageView?)

    /** 加载Gif图片，自使用监听 */
    fun loadGifImage(url: String?, imageView: ImageView?, listener: ImageListener?)

    /** 保存图片 */
    fun saveImage(context: Context?, url: String?, listener: ImageSaveListener?)

    /** 加载圆角图片 */
    fun loadRoundImage(url: String?, imageView: ImageView?, roundRadius: Float)

    /** 加载模糊图片 */
    fun loadBlurImage(url: String?, imageView: ImageView?, blurRadius: Float)

    /** 加载灰色图片 */
    fun loadGrayImage(url: String?, imageView: ImageView?)

    /** 加载圆形图片 */
    fun loadCircleImage(url: String?, imageView: ImageView?)

    /** 加载图片，生成Bitmap */
    fun loadBitmapImage(context: Context?, url: String?): Bitmap?

    /** 加载圆形图片带边框 */
    fun loadCircleImage(url: String?, imageView: ImageView?, borderWidth: Float, borderColor: Int)

    // 通过其他的资源，加载图片
    fun loadImage(file: File?, imageView: ImageView?)       // 加载本地文件

    fun loadImage(bitmap: Bitmap?, imageView: ImageView?)   // 加载Bitmap
    fun loadImage(uri: Uri?, imageView: ImageView?)         // 路由
    fun loadImage(resId: Int?, imageView: ImageView?)       // 加载本地资源
    fun loadImage(drawable: Drawable?, imageView: ImageView?) // 加载drawable
    fun loadImage(obj: Any?, imageView: ImageView?)             // 加载任意资源


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