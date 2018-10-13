package com.fungo.baselib.theme

import android.content.Context
import com.fungo.baselib.utils.SpUtils


/**
 * @author Pinger
 * @since 18-8-3 上午11:29
 * 处理app中的主题
 */

object ThemeHelper {

    private const val KEY_THEME = "KEY_THEME"

    /**
     * 获取当前的主题
     */
    fun getCurrentTheme(): AppTheme {
        return AppTheme.valueOf(SpUtils.getString(KEY_THEME, AppTheme.Blue.name))
    }


    /**
     * 设置当前的主题
     */
    fun setCurrentTheme(currentTheme: AppTheme) {
        SpUtils.putString(KEY_THEME, currentTheme.name)
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