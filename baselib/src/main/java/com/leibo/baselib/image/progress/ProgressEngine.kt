package com.leibo.baselib.image.progress

import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.util.*


/**
 * @author Pinger
 * @since 2018/4/9 21:49
 */
object ProgressEngine {

    private val mListeners = Collections.synchronizedList<WeakReference<ProgressListener>>(ArrayList<WeakReference<ProgressListener>>())
    private var mOkHttpClient: OkHttpClient? = null

    fun getOkHttpClient(): OkHttpClient {
        if (mOkHttpClient == null) {
            mOkHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor { chain ->
                        val request = chain.request()
                        val response = chain.proceed(request)
                        response.newBuilder()
                                .body(ProgressResponseBody(response.body(), mProgressListener))
                                .build()
                    }
                    .build()
        }
        return mOkHttpClient!!
    }

    private val mProgressListener = object : ProgressListener {
        override fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean) {
            if (mListeners == null || mListeners.size == 0) return

            for (i in 0 until mListeners.size) {
                val listener = mListeners[i]
                val progressListener = listener.get()
                if (progressListener == null) {
                    mListeners.removeAt(i)
                } else {
                    progressListener.onProgress(bytesRead, contentLength, isDone)
                }
            }
        }
    }

    fun addProgressListener(progressListener: ProgressListener?) {
        if (progressListener == null) return

        if (findProgressListener(progressListener) == null) {
            mListeners.add(WeakReference(progressListener))
        }
    }

    fun removeProgressListener(progressListener: ProgressListener?) {
        if (progressListener == null) return

        val listener = findProgressListener(progressListener)
        if (listener != null) {
            mListeners.remove(listener)
        }
    }

    private fun findProgressListener(listener: ProgressListener?): WeakReference<ProgressListener>? {
        if (listener == null) return null
        if (mListeners == null || mListeners.size == 0) return null
        for (i in 0 until mListeners.size) {
            val progressListener = mListeners[i]
            if (progressListener.get() === listener) {
                return progressListener
            }
        }
        return null
    }

}