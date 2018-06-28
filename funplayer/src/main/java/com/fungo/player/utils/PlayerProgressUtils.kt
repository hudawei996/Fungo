package com.fungo.player.utils

import android.text.TextUtils
import java.util.*

/**
 * @author Pinger
 * @since 18-6-13 下午4:10
 * 播放器相关工具类
 */

object PlayerProgressUtils {

    private val mProgressMap = LinkedHashMap<Int, Long>()

    /**
     * 保存当前的播放进度
     */
    fun saveProgress(url: String?, progress: Long) {
        if (TextUtils.isEmpty(url)) return
        mProgressMap[url!!.hashCode()] = progress
    }

    /***
     * 获取当前的播放进度
     */
    fun getSavedProgress(url: String?): Long {
        return if (TextUtils.isEmpty(url)) 0 else {
            if (mProgressMap.containsKey(url?.hashCode())) {
                mProgressMap[url?.hashCode()] ?: 0
            } else 0
        }
    }

    /**
     * 清除有所的保存进度
     */
    fun clearAllSavedProgress() {
        mProgressMap.clear()
    }

    /**
     * 清除单条播放进度
     */
    fun clearSavedProgressByUrl(url: String) {
        mProgressMap.remove(url.hashCode())
    }


}