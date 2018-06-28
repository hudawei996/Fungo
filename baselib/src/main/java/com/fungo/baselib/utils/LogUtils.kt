package com.fungo.baselib.utils

/**
 * @author Pinger
 * @since 2018/4/27 21:53
 */
object LogUtils {

    fun e(msg: String) {
        println(msg)
    }

    fun e(tag: String, msg: String) {
        println("$tag：$msg")
    }
}