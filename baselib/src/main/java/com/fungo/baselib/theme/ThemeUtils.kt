package com.fungo.baselib.theme

import android.content.Context
import com.fungo.baselib.R
import com.fungo.baselib.utils.SpUtils


/**
 * @author Pinger
 * @since 18-8-3 上午11:29
 * 处理app中的主题
 */

object ThemeUtils {

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
    fun setCurrentTheme(context: Context, theme: AppTheme) {
        when (theme) {
            AppTheme.Blue -> context.setTheme(R.style.BlueTheme)
            AppTheme.Red -> context.setTheme(R.style.RedTheme)
            AppTheme.Brown -> context.setTheme(R.style.BrownTheme)
            AppTheme.Green -> context.setTheme(R.style.GreenTheme)
            AppTheme.Purple -> context.setTheme(R.style.PurpleTheme)
            AppTheme.Teal -> context.setTheme(R.style.TealTheme)
            AppTheme.Pink -> context.setTheme(R.style.PinkTheme)
            AppTheme.DeepPurple -> context.setTheme(R.style.DeepPurpleTheme)
            AppTheme.Orange -> context.setTheme(R.style.OrangeTheme)
            AppTheme.Indigo -> context.setTheme(R.style.IndigoTheme)
            AppTheme.LightGreen -> context.setTheme(R.style.LightGreenTheme)
            AppTheme.Lime -> context.setTheme(R.style.LimeTheme)
            AppTheme.DeepOrange -> context.setTheme(R.style.DeepOrangeTheme)
            AppTheme.Cyan -> context.setTheme(R.style.CyanTheme)
            AppTheme.Black -> context.setTheme(R.style.BlackTheme)
            AppTheme.BlueGrey -> context.setTheme(R.style.BlueGreyTheme)
        }
        saveCurrentTheme(theme)
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

    /**
     * 保存当前的主题到本地
     */
    private fun saveCurrentTheme(currentTheme: AppTheme) {
        SpUtils.putString(KEY_THEME, currentTheme.name)
    }
}