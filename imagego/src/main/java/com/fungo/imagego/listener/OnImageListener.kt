package com.fungo.imagego.listener

import android.graphics.Bitmap

/**
 * @author Pinger
 * @since 3/28/18 2:22 PM
 */
interface OnImageListener {

    /** 图片加载成功 */
    fun onSuccess(bitmap: Bitmap?)

    /** 图片加载失败 */
    fun onFail(msg: String)
}