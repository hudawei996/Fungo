package com.fungo.imagego.transfmer

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * @author Pinger
 * @since 3/29/18 5:05 PM
 */
class CircleTransformation(val context: Context, private val borderWidth: Float
                           , borderColor: Int) : BitmapTransformation() {

    private var mBorderPaint = Paint()

    init {
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = borderWidth
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return circleCrop(pool, toTransform)
    }


    private fun circleCrop(pool: BitmapPool, source: Bitmap): Bitmap {
        val size = (Math.min(source.width, source.height) - borderWidth / 2).toInt()
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        val borderRadius = r - borderWidth / 2
        canvas.drawCircle(r, r, borderRadius, mBorderPaint)
        return result!!
    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }


}