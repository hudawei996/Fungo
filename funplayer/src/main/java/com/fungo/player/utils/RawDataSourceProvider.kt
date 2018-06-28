package com.fungo.player.utils

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream


/***
 * 本地资源转换
 */
class RawDataSourceProvider(private var mDescriptor: AssetFileDescriptor) : IMediaDataSource {

    private var mMediaBytes: ByteArray? = null

    override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int {
        if (position + 1 >= mMediaBytes?.size ?: 0) {
            return -1
        }

        var length: Int
        if (position + size < mMediaBytes?.size ?: 0) {
            length = size
        } else {
            length = ((mMediaBytes?.size ?: 0) - position).toInt()
            if (length > buffer.size)
                length = buffer.size

            length--
        }
        System.arraycopy(mMediaBytes, position.toInt(), buffer, offset, length)
        return length
    }

    @Throws(IOException::class)
    override fun getSize(): Long {
        val length = mDescriptor.length
        if (mMediaBytes == null) {
            val inputStream = mDescriptor.createInputStream()
            mMediaBytes = readBytes(inputStream)
        }


        return length
    }

    @Throws(IOException::class)
    override fun close() {
        mDescriptor.close()
        mMediaBytes = null
    }

    @Throws(IOException::class)
    private fun readBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()

        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        var len = 0
        while (len != -1) {
            len = inputStream.read(buffer)
            byteBuffer.write(buffer, 0, len)
        }

        return byteBuffer.toByteArray()
    }

    companion object {
        fun create(context: Context, uri: Uri): RawDataSourceProvider? {
            try {
                val fileDescriptor = context.contentResolver.openAssetFileDescriptor(uri, "r")
                return RawDataSourceProvider(fileDescriptor)

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return null
        }
    }
}