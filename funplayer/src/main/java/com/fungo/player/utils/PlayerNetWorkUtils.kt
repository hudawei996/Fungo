package com.fungo.player.utils

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import com.fungo.player.utils.PlayerConstant.NETWORK_MOBILE
import com.fungo.player.utils.PlayerConstant.NETWORK_WIFI
import com.fungo.player.utils.PlayerConstant.NO_NETWORK


/**
 * @author Pinger
 * @since 18-6-13 下午4:54
 *
 */

object PlayerNetWorkUtils {


    /**
     * 判断当前网络类型  0为没有网络连接  3为WiFi 4为2G5为3G6为4G
     */
    private fun getNetworkType(context: Context): Int {
        //改为context.getApplicationContext()，防止在Android 6.0上发生内存泄漏
        val connectMgr = context.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectMgr.activeNetworkInfo
                ?: return NO_NETWORK // 没有任何网络
        if (!networkInfo.isConnected) {
            return NO_NETWORK  // 网络断开或关闭
        }
        if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
            // wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接
            return NETWORK_WIFI
        } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            // 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭
            when (networkInfo.subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN,
                    // 2G网络
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP,
                    // 3G网络
                TelephonyManager.NETWORK_TYPE_LTE ->
                    // 4G网络
                    return NETWORK_MOBILE
            }
        }
        return NO_NETWORK
    }


    fun isWifiNetWork(context: Context): Boolean {
        return getNetworkType(context) == NETWORK_WIFI
    }

    fun isMobileNetWork(context: Context): Boolean {
        return getNetworkType(context) == NETWORK_MOBILE
    }

    fun isNetWorkAvailable(context: Context): Boolean {
        return getNetworkType(context) != NO_NETWORK
    }
}