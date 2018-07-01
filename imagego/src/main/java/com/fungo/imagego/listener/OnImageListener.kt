package com.fungo.imagego.listener

/**
 * @author Pinger
 * @since 3/28/18 2:22 PM
 */
interface OnImageListener {

    /** 图片加载成功 */
    fun onSuccess()

    /** 图片加载失败 */
    fun onFail(msg: String)
}