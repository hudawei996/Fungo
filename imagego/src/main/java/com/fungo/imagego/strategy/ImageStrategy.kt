package com.fungo.imagego.strategy


import android.content.Context
import android.view.View
import com.fungo.imagego.glide.transform.RoundType
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.listener.OnProgressListener
import java.io.File

/**
 * @author Pinger
 * @since 3/28/18 2:17 PM
 *
 * 图片加载策略基类，定义接口
 * 只定义最全的接口，不全的可以在Manager中补上
 */

interface ImageStrategy {


    /**
     * 加载网络图片，支持png，jpeg,jpg等格式
     * @param any 图片资源，可以是Url,File,Bitmap,URI,ResID,Drawable
     * @param view 图片展示
     * @param listener 图片加载监听
     */
    fun loadImage(any: Any?, view: View?, listener: OnImageListener?,builder:ImageOptions.Builder)


    /**
     * 获取图片加载的配置
     */
    fun getBuilder():ImageOptions.Builder


    /**
     * 加载网络图片
     * @param any 资源
     * @param view　视图
     */
    fun loadImage(any: Any?,view: View?){
        loadImage(any,view,null,getBuilder())
    }

    /**
     * 加载网络图片，支持png，jpeg,jpg等格式
     * @param any 图片资源，可以是Url,File,Bitmap,URI,ResID,Drawable
     * @param view 图片展示
     */
    fun loadImage(any: Any?, view: View?, builder:ImageOptions.Builder){
        loadImage(any,view,null,builder)
    }


    /**
     * 加载网络图片
     * @param any 资源
     * @param view　视图
     * @param listener　监听
     */
    fun loadImage(any: Any?,view: View?,listener: OnImageListener?){
        loadImage(any,view,listener,getBuilder())
    }


    /**
     * 加载Gif图片
     * @param any　资源
     * @param view　视图
     */
    fun loadGif(any: Any?, view: View?){
        loadGif(any,view,null)
    }


    /**
     * 加载Gif
     * @param any　资源
     * @param view　视图
     * @param listener　监听
     */
    fun loadGif(any: Any?, view: View?,listener: OnImageListener?){
        loadGif(any,view,null,getBuilder())
    }


    /**
     * 加载Gif
     * @param any　资源
     * @param view　视图
     */
    fun loadGif(any: Any?, view: View?,builder:ImageOptions.Builder){
        loadGif(any,view,null,builder)
    }


    /**
     * 加载Gif网络图片
     * @param any Gif图片的网络链接
     * @param view　展示的View
     * @param listener　加载监听
     */
    fun loadGif(any: Any?, view: View?, listener: OnImageListener?,builder:ImageOptions.Builder)


    /**
     * 加载图片，带有进度条
     */
    fun loadProgress(url: String?, view: View?, listener: OnProgressListener)


    /**
     * 加载图片资源，生成Bitmap对象
     * @param context 上下文
     * @param any 图片资源
     * @param listener　加载图片的回调
     */
    fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener)

    /**
     * 加载圆形图片
     * @param url 图片链接
     * @param view　图片
     */
    fun loadCircle(url:String?,view:View?){
        loadCircle(url,view,0,0,null)
    }


    /**
     * 加载圆形图片
     * @param url 图片链接
     * @param view　图片
     * @param borderColor　边框的颜色
     * @param borderWidth　边框的大小
     */
    fun loadCircle(url:String?,view:View?,borderWidth:Int,borderColor:Int){
        loadCircle(url,view,borderWidth,borderColor,null)
    }


    /**
     * 加载圆形图片
     * 使用本方法要求View有固定的宽高
     * @param url 图片链接
     * @param view　展示视图
     * @param borderColor　边框的颜色
     * @param borderWidth　边框的大小
     * @param listener　回调
     */
    fun loadCircle(url:String?,view:View?,borderWidth:Int,borderColor:Int,listener: OnImageListener?){
        loadImage(url, view,listener, getBuilder()
                .setAsGif(false)
                .setCrossFade(false)
                .setCircleCrop(true)
                .setCircleBorderColor(borderColor)
                .setCircleBorderWidth(borderWidth))
    }


    /**
     * 加载圆角图片
     * @param url 资源
     * @param view 视图
     */
    fun loadRound(url: String?,view: View?){
        loadRound(url,view,0,RoundType.ALL,null)
    }

    /**
     * 加载圆角图片
     * @param url 资源
     * @param view 视图
     */
    fun loadRound(url: String?,view: View?,roundRadius:Int,roundType: RoundType){
        loadRound(url,view,roundRadius,roundType,null)
    }

    /**
     * 加载圆角图片
     * @param url 资源
     * @param view 视图
     */
    fun loadRound(url: String?,view: View?,roundRadius:Int){
        loadRound(url,view,roundRadius,RoundType.ALL,null)
    }


    /**
     * 加载圆角图片
     * @param url 图片链接
     * @param view 展示
     * @param roundRadius　圆角的角度
     * @param roundType　圆角图片的边向
     * @param listener 回调
     */
    fun loadRound(url: String?,view: View?,roundRadius:Int,roundType: RoundType,listener: OnImageListener?){
        loadImage(url, view,listener, getBuilder()
                .setAsGif(false)
                .setCrossFade(false)
                .setRoundedCorners(true)
                .setRoundRadius(roundRadius)
                .setRoundType(roundType))
    }


    /**
     * 加载高斯模糊图片
     * @param url　图片链接
     * @param view　展示
     */
    fun loadBlur(url: String?,view: View?){
        loadBlur(url,view,0,null)
    }


    /**
     * 加载高斯模糊图片
     * @param url　图片链接
     * @param blurRadius　高斯模糊的度数
     * @param view　展示
     */
    fun loadBlur(url: String?,view: View?,blurRadius:Int){
        loadBlur(url,view,blurRadius,null)
    }


    /**
     * 加载高斯模糊图片
     * @param url　图片链接
     * @param view　展示
     * @param blurRadius　高斯模糊的度数
     * @param listener 回调
     */
    fun loadBlur(url: String?,view: View?,blurRadius:Int,listener: OnImageListener?){
        loadImage(url, view, listener, getBuilder()
                .setAsGif(false)
                .setCrossFade(false)
                .setBlur(true)
                .setBlurRadius(blurRadius))
    }


    /**
     * 保存网络图片到本地
     * @param context　上下文
     * @param any　保存的图片资源
     */
    fun saveImage(context: Context?, any: Any?){
        saveImage(context,any,null)
    }


    /**
     * 保存网络图片到本地
     * @param context　上下文
     * @param any　保存的图片资源
     * @param listener　图片保存的回调
     */
    fun saveImage(context: Context?, any: Any?, listener: OnImageSaveListener?)

    /**
     * 清除图片的磁盘缓存
     * 必须在子线程调用
     * @param context 上下文
     */
    fun clearImageDiskCache(context: Context?)

    /**
     * 清除图片的内存缓存
     * 必须在主线程调用
     * @param context 上下文
     */
    fun clearImageMemoryCache(context: Context?)

    /**
     * 获取手机磁盘图片缓存大小
     * @param context　上下文
     * @return 缓存大小，格式已经处理好了 例如：100M
     */
    fun getCacheSize(context: Context?): String

    /**
     * 恢复所有的图片加载任务，可以在页面或者列表可见时调用
     * @param context　上下文
     */
    fun resumeRequests(context: Context?)

    /**
     * 暂停所有的图片加载任务，可以在页面或者列表不可见的时候调用
     * @param context　上下文
     */
    fun pauseRequests(context: Context?)

    /**
     * 下载网络图片到本地
     * 本方法在子线程执行
     * @param context 上线文
     * @param any　图片的url
     */
    fun downloadImage(context: Context, any: Any?): File
}
