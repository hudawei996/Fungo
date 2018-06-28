package com.fungo.player.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ContextThemeWrapper
import android.util.TypedValue
import android.view.*

/**
 * @author Pinger
 * @since 18-6-13 下午4:51
 *
 */

object PlayerWindowUtils {

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Double {
        var statusBarHeight = 0
        //获取status_bar_height资源的ID
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight.toDouble()
    }

    /**
     * 获取NavigationBar的高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        if (!hasNavigationBar(context)) {
            return 0
        }
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android")
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 是否存在NavigationBar
     */
    fun hasNavigationBar(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val display = getWindowManager(context).defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            realSize.x != size.x || realSize.y != size.y
        } else {
            val menu = ViewConfiguration.get(context).hasPermanentMenuKey()
            val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            !(menu || back)
        }
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(context: Context, isIncludeNav: Boolean): Int {
        return if (isIncludeNav) {
            context.resources.displayMetrics.heightPixels + getNavigationBarHeight(context)
        } else {
            context.resources.displayMetrics.heightPixels
        }
    }

    /**
     * 隐藏ActionBar，StatusBar，NavigationBar
     */
    @SuppressLint("RestrictedApi")
    fun hideSystemBar(context: Context) {
        val appCompatActivity = getAppCompActivity(context)
        if (appCompatActivity != null) {
            val ab = appCompatActivity.supportActionBar
            if (ab != null && ab.isShowing) {
                ab.setShowHideAnimationEnabled(false)
                ab.hide()
            }
        }
        hideNavigationBar(context)
    }

    /**
     * 显示ActionBar，StatusBar，NavigationBar
     */
    @SuppressLint("RestrictedApi")
    fun showSystemBar(context: Context) {
        showNavigationBar(context)
        val appCompatActivity = getAppCompActivity(context)
        if (appCompatActivity != null) {
            val ab = appCompatActivity.supportActionBar
            if (ab != null && !ab.isShowing) {
                ab.setShowHideAnimationEnabled(false)
                ab.show()
            }
        }
    }

    /**
     * 获取Activity
     */
    fun scanForActivity(context: Context?): Activity? {
        return if (context == null) null else context as? Activity
                ?: if (context is ContextWrapper) scanForActivity(context.baseContext) else null
    }

    private fun hideNavigationBar(context: Context) {
        val decorView = scanForActivity(context)!!.window.decorView
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = flags
    }

    private fun showNavigationBar(context: Context) {
        val decorView = scanForActivity(context)!!.window.decorView
        var systemUiVisibility = decorView.systemUiVisibility
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        systemUiVisibility = systemUiVisibility and flags.inv()
        decorView.systemUiVisibility = systemUiVisibility
    }


    /**
     * Get AppCompatActivity from context
     */
    fun getAppCompActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity(context.baseContext)
        }
        return null
    }

    /**
     * dp转为px
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()
    }

    /**
     * sp转为px
     */
    fun sp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpValue, context.resources.displayMetrics).toInt()
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     */
    fun getWindowManager(context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    /**
     * 边缘检测
     */
    fun isEdge(context: Context, e: MotionEvent): Boolean {
        val edgeSize = dp2px(context, 50f)
        return (e.rawX < edgeSize
                || e.rawX > getScreenWidth(context) - edgeSize
                || e.rawY < edgeSize
                || e.rawY > getScreenHeight(context, true) - edgeSize)
    }

}