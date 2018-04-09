package com.leibo.baselib.image.progress

/**
 * @author Pinger
 * @since 3/30/18 11:12 AM
 */
interface ProgressListener {

    /**
     * 下载进度
     */
    fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean)

}