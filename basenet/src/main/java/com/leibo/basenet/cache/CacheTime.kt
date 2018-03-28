package org.fungo.basenetlib.cache

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     缓存时间
 */
object CacheTime {
    val NOT_CACHE = 0
    val DEFAULT = 4 * 60 * 60
    val A_MINUTE = 60
    val AN_HOURE = 60 * 60
    val SHORT = 30 * 60 * 60//30分钟
    val MID = 8 * 60 * 60//4个小时
    val LONG = 3 * 24 * 60 * 60//3天
    val LOG_TIME = 5 * 24 * 60 * 60//5天
    val A_DAY = 24 * 60 * 60
}