package org.fungo.basenetlib.config

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     自定义错误,不需要修改
 */

class RequestError : Throwable {
    companion object {
        const val TYPE_LOCAL = 0//本地错误
        const val TYPE_SERVER = 1//服务器确定的错误代码
    }

    var state: Int = 0
        private set
    var type: Int = 0//0--->本地错误，1---->服务器确定的错误代码
        private set

    constructor(state: Int, message: String) : super(message) {
        this.state = state
    }

    constructor(state: Int, message: String, type: Int) : super(message) {
        this.state = state
        this.type = type
    }

    override fun toString(): String {
        return "RequestError{" +
                "State=" + state +
                "message = " + message +
                '}'
    }
}

