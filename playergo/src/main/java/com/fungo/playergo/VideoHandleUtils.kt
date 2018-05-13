package com.fungo.playergo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ContextThemeWrapper
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import java.util.*


object VideoHandleUtils {
    /**
     * Get activity from context object
     *
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    fun scanForActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }

    /**
     * Get AppCompatActivity from context
     *
     * @param context
     * @return AppCompatActivity if it's not null
     */
    private fun getAppCompActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity(context.baseContext)
        }
        return null
    }

    @SuppressLint("RestrictedApi")
    fun showActionBar(context: Context) {
        val ab = getAppCompActivity(context)!!.supportActionBar
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.show()
        }
        scanForActivity(context)!!
                .window
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    @SuppressLint("RestrictedApi")
    fun hideActionBar(context: Context) {
        val ab = getAppCompActivity(context)!!.supportActionBar
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.hide()
        }
        scanForActivity(context)!!
                .window
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return width of the screen.
     */
    @SuppressLint("ObsoleteSdkInt")
    fun getScreenWidth(context: Context): Int {
        val width: Int
        val dm = DisplayMetrics()
        val windowMgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowMgr.defaultDisplay.getRealMetrics(dm)
            width = dm.widthPixels
        } else {
            val point = Point()
            windowMgr.defaultDisplay.getSize(point)
            return point.x
        }
        return width
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return heiht of the screen.
     */
    @SuppressLint("ObsoleteSdkInt")
    fun getScreenHeight(context: Context): Int {
        val height: Int
        val dm = DisplayMetrics()
        val windowMgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowMgr.defaultDisplay.getRealMetrics(dm)
            height = dm.heightPixels
        } else {
            val point = Point()
            windowMgr.defaultDisplay.getSize(point)
            return point.y
        }
        return height
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal   dp value
     * @return px value
     */
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.resources.displayMetrics).toInt()
    }

    /**
     * 将毫秒数格式化为"##:##"的时间
     *
     * @param milliseconds 毫秒数
     * @return ##:##
     */
    fun formatTime(milliseconds: Long): String {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00"
        }
        val totalSeconds = milliseconds / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val stringBuilder = StringBuilder()
        val mFormatter = Formatter(stringBuilder, Locale.getDefault())
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    /**
     * 保存播放位置，以便下次播放时接着上次的位置继续播放.
     *
     * @param context
     * @param url     视频链接url
     */
    fun savePlayPosition(context: Context, url: String, position: Long) {
        context.getSharedPreferences("VIDEO_PLAYER_PLAY_POSITION",
                Context.MODE_PRIVATE)
                .edit()
                .putLong(url, position)
                .apply()
    }

    /**
     * 取出上次保存的播放位置
     *
     * @param context
     * @param url     视频链接url
     * @return 上次保存的播放位置
     */
    fun getSavedPlayPosition(context: Context, url: String): Long {
        return context.getSharedPreferences("VIDEO_PLAYER_PLAY_POSITION",
                Context.MODE_PRIVATE)
                .getLong(url, 0)
    }
}
