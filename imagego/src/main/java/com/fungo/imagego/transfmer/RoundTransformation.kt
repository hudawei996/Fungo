package com.fungo.imagego.transfmer

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * @author Pinger
 * @since 3/29/18 4:06 PM
 */
class RoundTransformation(val context: Context, private val roundRadius: Float) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return roundCrop(pool, toTransform)
    }

    private fun roundCrop(pool: BitmapPool, toTransform: Bitmap): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint)
        return bitmap
    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

}