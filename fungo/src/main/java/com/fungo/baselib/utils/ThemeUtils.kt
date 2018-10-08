package com.fungo.baselib.utils

import android.content.Context
import com.fungo.baselib.helper.SpHelper
import com.fungo.baselib.view.theme.AppTheme


/**
 * @author Pinger
 * @since 18-8-3 上午11:29
 * 处理app中的主题
 */

object ThemeUtils {


    /**
     * 获取当前的主题
     */
    fun getCurrentTheme(): AppTheme {
        return AppTheme.valueOf(SpHelper.currentTheme)
    }


    /**
     * 设置当前的主题
     */
    fun setCurrentTheme(currentTheme: AppTheme) {
        SpHelper.currentTheme = currentTheme.name
    }


    /**
     * 获取主题的颜色
     */
    fun getThemeColor(context: Context, attrRes: Int): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attrRes))
        val color = typedArray.getColor(0, 0xffffff)
        typedArray.recycle()
        return color
    }
}