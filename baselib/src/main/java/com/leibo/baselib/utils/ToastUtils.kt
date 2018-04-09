package com.leibo.baselib.utils

import android.support.annotation.StringRes
import android.widget.Toast
import com.leibo.baselib.manager.ThreadManager

/**
 * @author Pinger
 * @since 18-4-9 下午8:00
 *
 */
object ToastUtils {


    fun showToast(msg: String) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(BaseUtils.getApp(), msg, Toast.LENGTH_SHORT).show()
        })
    }

    fun showToast(@StringRes strId: Int) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(BaseUtils.getApp(), strId, Toast.LENGTH_SHORT).show()
        })
    }


    fun showLong(msg: String) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(BaseUtils.getApp(), msg, Toast.LENGTH_LONG).show()
        })
    }

    fun showLong(@StringRes strId: Int) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(BaseUtils.getApp(), strId, Toast.LENGTH_LONG).show()
        })
    }

}