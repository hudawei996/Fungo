package com.fungo.baselib.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author Pinger
 * @since 2018/4/30 16:07
 * 图片处理工具类
 */
object BitmapUtils {


    /** Bitmap保存为文件 */
    fun saveBitmapFile(bitmap: Bitmap, path: String): File? {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return File(path)
    }

    /** Bitmap 转 bytes */
    fun bitmap2Bytes(bitmap: Bitmap?): ByteArray? {
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        if (bitmap != null && !bitmap.isRecycled) {
            try {
                byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                if (byteArrayOutputStream.toByteArray() == null) {
                    LogUtils.e("BitmapUtils", "bitmap2Bytes byteArrayOutputStream toByteArray=null")
                }
                return byteArrayOutputStream.toByteArray()
            } catch (e: Exception) {
                LogUtils.e("BitmapUtils", e.toString())
            } finally {
                try {
                    byteArrayOutputStream?.close()
                } catch (var14: IOException) {
                }
            }
            return null
        } else {
            LogUtils.e("BitmapUtils", "bitmap2Bytes bitmap == null or bitmap.isRecycled()")
            return null
        }
    }

    /** 在保证质量的情况下尽可能压缩 不保证压缩到指定字节 */
    fun compressBitmap(datas: ByteArray?, byteCount: Int): ByteArray? {
        var isFinish = false
        if (datas != null && datas.size > byteCount) {
            val outputStream = ByteArrayOutputStream()
            val tmpBitmap = BitmapFactory.decodeByteArray(datas, 0, datas.size)
            var times = 1
            var percentage = 1.0

            while (!isFinish && times <= 10) {
                percentage = Math.pow(0.8, times.toDouble())
                val compress_datas = (100.0 * percentage).toInt()
                tmpBitmap.compress(Bitmap.CompressFormat.JPEG, compress_datas, outputStream)
                if (outputStream.size() < byteCount) {
                    isFinish = true
                } else {
                    outputStream.reset()
                    ++times
                }
            }
            val outputStreamByte = outputStream.toByteArray()
            if (!tmpBitmap.isRecycled) {
                tmpBitmap.recycle()
            }
            if (outputStreamByte.size > byteCount) {
                LogUtils.e("BitmapUtils", "compressBitmap cannot compress to " + byteCount + ", after compress size=" + outputStreamByte.size)
            }
            return outputStreamByte
        }
        return datas
    }

}