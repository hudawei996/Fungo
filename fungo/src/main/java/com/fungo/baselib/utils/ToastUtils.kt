package com.fungo.baselib.utils

import android.support.annotation.StringRes
import android.widget.Toast
import com.fungo.baselib.manager.ThreadManager

/**
 * @author Pinger
 * @since 18-4-9 下午8:00
 *
 */
object ToastUtils {

    /**
     * 展示一个吐司，短时间的吐司
     */
    fun showToast(msg: String) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_SHORT).show()
        })
    }


    /**
     * 展示一个吐司
     */
    fun showToast(@StringRes strId: Int) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), strId, Toast.LENGTH_SHORT).show()
        })
    }


    /**
     * 长时间展示的吐司
     */
    fun showLong(msg: String) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_LONG).show()
        })
    }

    /**
     * 长时间展示的吐司
     */
    fun showLong(@StringRes strId: Int) {
        ThreadManager.runOnUIThread(Runnable {
            Toast.makeText(AppUtils.getContext(), strId, Toast.LENGTH_LONG).show()
        })
    }

}