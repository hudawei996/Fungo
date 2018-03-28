package org.fungo.basenetlib.cache

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     缓存
 */
class CacheEntity<T> {
    var status: Int = 0  //0为失败，1为成功
    var data: T? = null
}
