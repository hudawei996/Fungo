package org.fungo.basenetlib.config

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     错误代码,这个需要根据各个项目服务器定义错误码而改变
 */
object ErrorCode {
    //服务器定义错误码，可修改
    const val SERVER_SUCCESS = 0//请求成功
    const val SERVER_ERROR = 500//服务器异常

    //本地自定义错误码，不需要修改
    const val JSON_DATA_ERROR = 901  //receive a jsonException when try to analysis a string to bean
    const val HTTP_SERIES_ERROR = 904  //ConnectException ||SocketTimeoutException||TimeoutException|| HttpException
    const val NETWORK_UN_CONNECTED = 907//无网络
}