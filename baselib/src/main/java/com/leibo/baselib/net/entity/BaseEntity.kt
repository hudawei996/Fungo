package com.leibo.baselib.net.entity

import com.google.gson.JsonElement

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     服务器返回数据结构
 */
abstract class BaseEntity {

    var data: JsonElement? = null

    abstract fun isSuccess(): Boolean
    abstract fun getMsg(): String?
    abstract fun getCode(): Int
}
