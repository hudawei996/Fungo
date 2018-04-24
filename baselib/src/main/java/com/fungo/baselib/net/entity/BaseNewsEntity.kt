package com.fungo.baselib.net.entity

import com.fungo.baselib.net.constant.ErrorCode

/**
 * @author Pinger
 * @since 2018/4/12 18:36
 */
class BaseNewsEntity(private val error_code: Int, val message: String) : BaseEntity() {

    override fun isSuccess(): Boolean {
        return error_code == ErrorCode.SERVER_SUCCESS
    }

    override fun getMsg(): String? {
        return message
    }

    override fun getCode(): Int {
        return error_code
    }
}