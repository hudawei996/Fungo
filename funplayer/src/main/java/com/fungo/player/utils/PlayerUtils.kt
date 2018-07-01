package com.fungo.player.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.support.annotation.StringRes
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Pinger
 * @since 18-6-25 下午4:33
 *
 */

object PlayerUtils {


    /**
     * 显示Toast
     */
    fun showToast(context: Context,content:String){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show()
    }


    fun showToast(context: Context, @StringRes resId:Int){
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show()
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
     * 格式化当前时间，转换为小时和分的格式
     *
     * @return ##:##
     */
    fun formatCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.CHINA).format(Date())
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
}