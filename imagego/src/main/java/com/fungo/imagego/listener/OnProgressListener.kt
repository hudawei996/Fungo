package com.fungo.imagego.listener

/**
 * @author Pinger
 * @since 3/30/18 11:12 AM
 */
interface OnProgressListener {

    /**
     * 下载进度，自己计算百分比
     */
    fun onProgress(bytesRead: Long, contentLength: Long, isDone: Boolean)
}