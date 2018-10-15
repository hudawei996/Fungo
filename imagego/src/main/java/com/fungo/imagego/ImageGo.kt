@file:JvmName("ImageGo")

package com.fungo.imagego

import android.content.Context
import android.view.View
import com.fungo.imagego.glide.transform.RoundType
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.listener.OnImageSaveListener
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.strategy.ImageGoEngine
import com.fungo.imagego.strategy.ImageStrategy
import java.io.File


// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－
// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－
// －－－－－－－－－－提供图片加载相关的API－－－－－－－－－

/**
 * 加载网络图片
 * @param any 资源
 * @param view　视图
 */
 fun loadImage(any: Any?,view: View?){
    getStrategy().loadImage(any,view)
}

/**
 * 加载网络图片
 * @param any 资源
 * @param view　视图
 * @param listener　监听
 */
fun loadImage(any: Any?,view: View?,listener: OnImageListener?){
    getStrategy().loadImage(any,view,listener)
}


/**
 * 加载Gif图片
 * @param any　资源
 * @param view　视图
 */
fun loadGif(any: Any?, view: View?){
    getStrategy().loadGif(any,view)
}


/**
 * 加载Gif
 * @param any　资源
 * @param view　视图
 * @param listener　监听
 */
fun loadGif(any: Any?, view: View?,listener: OnImageListener?){
    getStrategy().loadGif(any,view,listener)
}



/**
 * 加载图片，带进度条
 */
fun loadProgress(url: String?, view: View?, listener: OnProgressListener) {
    return getStrategy().loadProgress(url,view,listener)
}


/**
 * 加载图片资源，生成Bitmap对象
 * @param context 上下文
 * @param any 图片资源
 * @param listener　加载图片的回调
 */
fun loadBitmap(context: Context?, any: Any?, listener: OnImageListener){
    getStrategy().loadBitmap(context,any,listener)
}

/**
 * 加载圆形图片
 * @param url 图片链接
 * @param view　图片
 */
fun loadCircle(url:String?,view:View?){
    getStrategy().loadCircle(url,view)
}


/**
 * 加载圆形图片
 * @param url 图片链接
 * @param view　展示视图
 * @param borderColor　边框的颜色
 * @param borderWidth　边框的大小
 */
fun loadCircle(url:String?,view:View?,borderWidth:Int,borderColor:Int){
    getStrategy().loadCircle(url,view,borderWidth,borderColor)
}


/**
 * 加载圆形图片
 * @param url 图片链接
 * @param view　展示视图
 * @param borderColor　边框的颜色
 * @param borderWidth　边框的大小
 * @param listener　回调
 */
fun loadCircle(url:String?,view:View?,borderWidth:Int,borderColor:Int,listener: OnImageListener?){
    getStrategy().loadCircle(url,view,borderWidth,borderColor,listener)
}

/**
 * 加载圆角图片
 * @param url 资源
 * @param view 视图
 */
fun loadRound(url: String?,view: View?){
    getStrategy().loadRound(url,view)
}

/**
 * 加载圆角图片
 * @param url 资源
 * @param view 视图
 * @param roundRadius　圆角的角度
 */
fun loadRound(url: String?,view: View?,roundRadius:Int){
    getStrategy().loadRound(url,view,roundRadius)
}


/**
 * 加载圆角图片
 * @param url 图片链接
 * @param view 展示
 * @param roundRadius　圆角的角度
 * @param roundType　圆角图片的边向
 */
fun loadRound(url: String?, view: View?, roundRadius:Int, roundType: RoundType){
    getStrategy().loadRound(url,view,roundRadius,roundType)
}

/**
 * 加载圆角图片
 * @param url 图片链接
 * @param view 展示
 * @param roundRadius　圆角的角度
 * @param roundType　圆角图片的边向
 * @param listener 回调
 */
fun loadRound(url: String?, view: View?, roundRadius:Int, roundType: RoundType, listener: OnImageListener?){
    getStrategy().loadRound(url,view,roundRadius,roundType,listener)
}


/**
 * 加载高斯模糊图片
 * @param url　图片链接
 * @param view　展示
 */
fun loadBlur(url: String?,view: View?){
    getStrategy().loadBlur(url,view)
}

/**
 * 加载高斯模糊图片
 * @param url　图片链接
 * @param view　展示
 * @param blurRadius　高斯模糊的度数
 * @param listener 回调
 */
fun loadBlur(url: String?,view: View?,blurRadius:Int,listener: OnImageListener?){
    getStrategy().loadBlur(url,view,blurRadius,listener)
}

/**
 * 保存网络图片到本地
 * @param context　上下文
 * @param any　保存的图片资源
 */
fun saveImage(context: Context?, any: Any?){
    getStrategy().saveImage(context,any)
}

/**
 * 保存网络图片到本地
 * @param context　上下文
 * @param any　保存的图片资源
 * @param listener　图片保存的回调
 */
fun saveImage(context: Context?, any: Any?, listener: OnImageSaveListener?){
    getStrategy().saveImage(context,any,listener)
}

/**
 * 清除图片的磁盘缓存
 * 必须在子线程调用
 * @param context 上下文
 */
fun clearImageDiskCache(context: Context?){
    getStrategy().clearImageDiskCache(context)
}

/**
 * 清除图片的内存缓存
 * 必须在主线程调用
 * @param context 上下文
 */
fun clearImageMemoryCache(context: Context?){
    getStrategy().clearImageMemoryCache(context)
}

/**
 * 获取手机磁盘图片缓存大小
 * @param context　上下文
 * @return 缓存大小，格式已经处理好了 例如：100M
 */
fun getCacheSize(context: Context?): String{
    return getStrategy().getCacheSize(context)
}

/**
 * 恢复所有的图片加载任务，可以在页面或者列表可见时调用
 * @param context　上下文
 */
fun resumeRequests(context: Context?){
    getStrategy().resumeRequests(context)
}

/**
 * 暂停所有的图片加载任务，可以在页面或者列表不可见的时候调用
 * @param context　上下文
 */
fun pauseRequests(context: Context?){
    getStrategy().pauseRequests(context)
}

/**
 * 下载网络图片到本地
 * 本方法在子线程执行
 * @param context 上线文
 * @param any　图片的url
 */
fun downloadImage(context: Context, any: Any?): File {
    return getStrategy().downloadImage(context,any)
}


/**
 * 获取图片加载策略
 */
private fun getStrategy():ImageStrategy{
    return ImageGoEngine.getStrategy()
}



