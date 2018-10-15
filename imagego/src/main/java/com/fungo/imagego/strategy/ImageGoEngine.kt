package com.fungo.imagego.strategy

import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.progress.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author Pinger
 * @since 18-7-19 下午3:16
 * 图片加载的配置引擎
 *
 * 管理进度条的监听集合，提供全局图片加载的okhttp，配置图片加载策略等等
 */

object ImageGoEngine {

    /**
     * 加载进度条的集合，在列表中使用进度条时会出现多个进度监听
     */
    private val mProgressListeners = Collections.synchronizedList<WeakReference<OnProgressListener>>(ArrayList<WeakReference<OnProgressListener>>())


    /**
     * 获取经过配置的OkHttp
     */
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor(ProgressInterceptor())
                .build()
    }


    /**
     * 图片加载进度拦截器，将进度监听传入，回调回来
     */
    private class ProgressInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            return response.newBuilder()
                    .body(ProgressResponseBody(response.body(), mProgressListener))
                    .build()
        }
    }


    /**
     *　监听接口的内部对象，包装一层，外部好做处理
     */
    private val mProgressListener = object : OnProgressListener {

        override fun onProgress(bytesRead: Long, contentLength: Long, isFinish: Boolean) {
            if (mProgressListeners == null || mProgressListeners.size == 0) return

            for (i in 0 until mProgressListeners.size) {
                val listener = mProgressListeners[i]
                val progressListener = listener.get()
                progressListener?.onProgress(bytesRead, contentLength, isFinish)
            }
            // TODO 加载结束了暂时移除掉进度的监听，需要单独处理，自动移除和手动移除，单条和所有移除
            if (isFinish) {
                mProgressListeners.clear()
            }
        }
    }


    /**
     * 添加进度监听
     */
    fun addProgressListener(progressListener: OnProgressListener?) {
        if (progressListener == null) return

        if (findProgressListener(progressListener) == null) {
            mProgressListeners.add(WeakReference(progressListener))
        }
    }

    /**
     * 移除进度监听
     */
    fun removeProgressListener(progressListener: OnProgressListener?) {
        if (progressListener == null) return

        val listener = findProgressListener(progressListener)
        if (listener != null) {
            mProgressListeners.remove(listener)
        }
    }

    /**
     * 查找是否有个这个监听，避免重复添加一样的监听
     */
    private fun findProgressListener(listener: OnProgressListener?): WeakReference<OnProgressListener>? {
        if (listener == null) return null
        if (mProgressListeners == null || mProgressListeners.size == 0) return null
        for (i in 0 until mProgressListeners.size) {
            val progressListener = mProgressListeners[i]
            if (progressListener.get() == listener) {
                return progressListener
            }
        }
        return null
    }


    /**
     * 初始化加载策略，在Application中设置
     * 默认使用Glide加载策略
     */
    private var mStrategy: ImageStrategy = GlideImageStrategy()

    /**
     * 是否是开发模式，正式环境设置为false
     */
    private var isDebug = true

    /**
     * 是否自动加载Gif图
     */
    private var isAutoGif = false


    fun isDebug(): Boolean {
        return isDebug
    }

    fun isAutoGif(): Boolean {
        return isAutoGif
    }

    fun getStrategy(): ImageStrategy {
        return mStrategy
    }

    fun setDebug(isDebug: Boolean): ImageGoEngine {
        this.isDebug = isDebug
        return this
    }

    fun setAutoGif(autoGif: Boolean): ImageGoEngine {
        this.isAutoGif = autoGif
        return this
    }

    fun setImageStrategy(strategy: ImageStrategy): ImageGoEngine {
        this.mStrategy = strategy
        return this
    }

}