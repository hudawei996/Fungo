package com.leibo.baselib.net.api

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.leibo.baselib.net.constant.ErrorCode
import com.leibo.baselib.net.constant.ErrorDesc
import com.leibo.baselib.net.entity.BaseEntity
import com.leibo.baselib.net.entity.RequestError
import com.leibo.baselib.net.utils.NetLogger
import com.leibo.baselib.net.utils.ParamsUtils
import com.leibo.baselib.utils.BaseUtils
import com.leibo.baselib.utils.GsonUtils
import com.leibo.baselib.utils.NetWorkUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern


/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     网络请求
 */
class FungoRequest(private val fungoApi: FungoApi) {

    private companion object {
        const val TYPE_POST = 0
        const val TYPE_GET = 1
    }

    /**
     * POST请求  不具体处理返回值，只是转化成BaseEntity实例
     *
     * @param sourceUrl 接口号
     * @param params    请求中data对应的参数，一个json格式数据
     *
     */
    fun postRequest(sourceUrl: String, params: Map<String, Any?>?): Observable<BaseEntity> {
        val encryptionParams = ParamsUtils.getPostBody(sourceUrl, params)
        return if (sourceUrl.contains("http:") || sourceUrl.contains("https:")) {
            fungoApi.postRequestWithFullUrl(sourceUrl, encryptionParams)
        } else {
            fungoApi.postRequest(sourceUrl, encryptionParams)
        }
    }

    /**
     * POST请求   处理返回值成<T>
     * @param sourceUrl     接口号
     * @param params        请求中data对应的参数，一个json格式数据
     * @param clazz         请求成功对象data里面的实体对象
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              返回成功后的数据
    </T> */
    fun <T> postRequest(sourceUrl: String, params: Map<String, Any?>?, clazz: Class<T>): Observable<T> {
        return request(TYPE_POST, sourceUrl, params, clazz)
    }

    /**
     * GET请求  不具体处理返回值，只是转化成BaseEntity实例
     *
     * @param sourceUrl 接口号+参数
     *
     */
    fun getRequest(sourceUrl: String, params: Map<String, Any?>?): Observable<BaseEntity> {
        val url = ParamsUtils.appendUrlParams(sourceUrl, params)
        return if (url.contains("http:") || url.contains("https:")) {
            fungoApi.getRequestWithFullUrl(url)
        } else {
            fungoApi.getRequest(url)
        }
    }

    /**
     * GET请求  处理返回值成<T>
     * @param sourceUrl     接口号+参数
     * @param clazz         请求成功对象data里面的实体对象
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              返回成功后的数据
    </T> */
    fun <T> getRequest(sourceUrl: String, clazz: Class<T>): Observable<T> {
        return request(TYPE_GET, sourceUrl, null, clazz)
    }

    private fun <T> request(requestType: Int, sourceUrl: String, params: Map<String, Any?>?, clazz: Class<T>): Observable<T> {
        // 没网络主动抛出，程序决定是否处理
        return if (isNetworkDisconnected()) {
            Observable.create { e ->
                if (!e.isDisposed) {
                    e.onError(RequestError(ErrorCode.NETWORK_UN_CONNECTED, ErrorDesc.NET_UN_CONNECTED))//当前无网络
                    e.onComplete()
                }
            }
        } else Observable.just(sourceUrl)
                .flatMap { url ->
                    if (TYPE_POST == requestType) {
                        postRequest(url, params)
                    } else {
                        getRequest(url, params)
                    }
                }
                .flatMap(Function<BaseEntity, ObservableSource<T>> { baseEntity ->
                    if (baseEntity.isSuccess()) {
                        Observable.create { subscriber ->
                            if (baseEntity.data == null) {
                                val commonData = Any()
                                subscriber.onNext(commonData as T)
                            } else {
                                try {
                                    val data = baseEntity.data
                                    NetLogger.i("Fungo Request OK---> sourceUrl ：" + sourceUrl + "\n success response : ---> " + data!!.toString())
                                    /*if (cacheTime != CacheTime.NOT_CACHE) {
                                                ACache.get(BaseApplication.getApplication()).put(new CacheHelper().getCacheKey(sourceUrl, params), data.toString(), cacheTime);
                                            }*/
                                    val bean = GsonUtils.fromJson(data.toString(), clazz)
                                    if (!subscriber.isDisposed) {
                                        subscriber.onNext(bean)
                                    }
                                } catch (e: Exception) {
                                    NetLogger.e("logout" + " :" + e.message)
                                    //compare to server,the mDataBean's type of client is different
                                    if (!subscriber.isDisposed) {
                                        subscriber.onError(RequestError(ErrorCode.JSON_DATA_ERROR, baseEntity.toString()))
                                    }
                                }
                            }
                        }
                    } else {
                        NetLogger.e("logout" + " new RequestError: baseEntity.errno = " + baseEntity.getCode() + ",baseEntity.desc = " + baseEntity.getMsg())
                        Observable.error(RequestError(baseEntity.getCode(), baseEntity.getMsg()
                                ?: "", RequestError.TYPE_SERVER))
                    }
                })
                .onErrorResumeNext(Function<Throwable, ObservableSource<T>> { error ->
                    val e = convertError(error)
                    if (e is RequestError) {
                        //在这里做全局的错误处理
                        NetLogger.i("Fungo Request Error--->  sourceUrl ：" + sourceUrl + "\n Error Code : ---> " + e.state + "\n Error message : ---> " + e.toString())
                        e.printStackTrace()
                    } else {
                        NetLogger.i("Fungo Request Error--->  sourceUrl ：" + sourceUrl + "\n Error message : ---> " + e.toString())
                    }
                    Observable.error(e)
                })

    }

    private fun convertError(error: Throwable): Throwable {
        var e = error
        if (e is RequestError) {

            if (e.state == ErrorCode.SERVER_ERROR)
                e = RequestError(ErrorCode.SERVER_ERROR, ErrorDesc.SERVER_ERR, RequestError.TYPE_SERVER)

        } else if (e is HttpException) {

            e = if (isServerErr(e.code())) {
                RequestError(ErrorCode.SERVER_ERROR, ErrorDesc.SERVER_ERR, RequestError.TYPE_SERVER)//服务器挂了
            } else {
                RequestError(ErrorCode.HTTP_SERIES_ERROR, ErrorDesc.NET_TIME_OUT, RequestError.TYPE_SERVER)
            }

        } else if (e is SocketTimeoutException || e is TimeoutException) {   //transform this error to local error,since this way is easier to handle

            e = RequestError(ErrorCode.HTTP_SERIES_ERROR, ErrorDesc.NET_TIME_OUT)//服务连接超时

        } else if (e is ConnectException) {

            e = RequestError(ErrorCode.HTTP_SERIES_ERROR, ErrorDesc.NET_NOT_GOOD)//当前网络较差

        }

        return e
    }

    private fun isServerErr(code: Int): Boolean {
        val p = Pattern.compile("^5\\d{2}$")
        val m = p.matcher(code.toString())
        return m.matches()
    }

    private fun isNetworkDisconnected(): Boolean {
        return NetWorkUtils.isDisconnectedByState(BaseUtils.getApp())
    }
}


