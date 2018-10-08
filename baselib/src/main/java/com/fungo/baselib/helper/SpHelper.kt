package com.fungo.baselib.helper

import com.fungo.baselib.constant.SpConstant
import com.fungo.baselib.utils.SpUtils
import com.fungo.baselib.view.theme.AppTheme

/**
 * @author Pinger
 * @since 18-8-3 上午11:32
 * Sp操作的辅助类，用于管理每一个存储的值避免到处都是Sp的工具类
 */

object SpHelper {


    // 当前主题
    var currentTheme
        get() = SpUtils.getString(SpConstant.KEY_THEME, AppTheme.Blue.name)
        set(value) {
            SpUtils.putString(SpConstant.KEY_THEME, value)
        }

}