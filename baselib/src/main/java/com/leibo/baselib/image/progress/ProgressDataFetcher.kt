package com.leibo.baselib.image.progress

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStream


/**
 * @author Pinger
 * @since 3/30/18 11:13 AM
 */
class ProgressDataFetcher(private val url: String, private val listener: ProgressListener) : DataFetcher<InputStream> {

    private var progressCall: Call? = null
    private var stream: InputStream? = null
    private var isCancelled: Boolean = false


    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }


    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }

    override fun cancel() {
        isCancelled = true
    }

    override fun cleanup() {
        stream = try {
            stream?.close()
            null
        } catch (e: IOException) {
            null
        }
        progressCall?.cancel()
    }


    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.interceptors().add(ProgressInterceptor(listener))
        try {
            progressCall = client.newCall(request)
            val response = progressCall!!.execute()
            if (isCancelled || !response.isSuccessful) {
                return
            }
            stream = response.body()?.byteStream()
            callback.onDataReady(stream)
        } catch (e: IOException) {
            e.printStackTrace()
            callback.onLoadFailed(e)
        }
    }
}