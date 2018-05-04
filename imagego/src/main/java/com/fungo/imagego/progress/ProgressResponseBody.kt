package com.fungo.imagego.progress

import com.fungo.imagego.listener.OnProgressListener
import com.fungo.imagego.utils.ImageGoUtils
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


/**
 * @author Pinger
 * @since 3/30/18 11:29 AM
 */
class ProgressResponseBody(private val responseBody: ResponseBody?, val progressListener: OnProgressListener) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        try {
            return responseBody?.contentLength() ?: 0
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return 0
    }

    override fun source(): BufferedSource? {
        if (responseBody == null) {
            return null
        }
        if (bufferedSource == null) {
            try {
                bufferedSource = Okio.buffer(source(responseBody.source()))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead: Long = 0
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != (-1).toLong()) bytesRead else 0
                ImageGoUtils.runOnUIThread(Runnable {
                    progressListener.onProgress(totalBytesRead, contentLength(), bytesRead == (-1).toLong())
                })
                return bytesRead
            }
        }
    }

}