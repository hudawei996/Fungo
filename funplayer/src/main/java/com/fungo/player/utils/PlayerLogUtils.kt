package com.fungo.player.utils

import android.util.Log

/**
 * @author Pinger
 * @since 18-6-13 下午3:56
 * 播放器日志打印
 */

object PlayerLogUtils {

    private const val TAG = "FunPlayer"
    private var isDebug = true

    fun d(msg: String) {
        if (isDebug) {
            println("-----> $msg")
        }
    }

    fun e(msg: String) {
        if (isDebug) {
            Log.e(TAG, "-----> $msg")
        }
    }

    fun i(msg: String) {
        if (isDebug) {
            Log.i(TAG, "-----> $msg")
        }
    }

    fun setDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

}