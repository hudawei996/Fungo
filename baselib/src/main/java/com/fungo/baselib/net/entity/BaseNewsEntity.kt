package com.fungo.baselib.net.entity

import com.fungo.baselib.net.constant.ErrorCode
import com.google.gson.JsonElement

/**
 * @author Pinger
 * @since 2018/4/12 18:36
 */
class BaseNewsEntity(private val data:JsonElement,private val code: Int, val msg: String) : BaseEntity {

    override fun getData(): JsonElement? {
        return data
    }

    override fun isSuccess(): Boolean {
        return code == ErrorCode.SERVER_SUCCESS
    }

    override fun getMessage(): String? {
        return msg
    }

    override fun getCode(): Int {
        return code
    }
}