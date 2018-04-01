package com.leibo.baselib.net.retrofit

import com.leibo.baselib.net.api.FungoApi
import com.leibo.baselib.net.base.BaseEntity
import com.leibo.baselib.net.base.RequestError
import com.leibo.baselib.net.constant.ErrorDesc
import com.leibo.baselib.net.constant.ErrorCode
import com.leibo.baselib.net.utils.GsonUtils
import com.leibo.baselib.net.utils.Logger
import com.leibo.baselib.net.utils.ParamsUtils
import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
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
@Suppress("UNCHECKED_CAST")
class FungoRequest(private val context: Context, private val fungoApi: FungoApi) {

    private companion object {
        val TYPE_POST = 0
        val TYPE_GET = 1
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
     * POST请求   处理返回值成<T>，无缓存
     *
     * @param sourceUrl     接口号
     * @param params        请求中data对应的参数，一个json格式数据
     * @param clazz         请求成功对象data里面的实体对象
     * @param needRetry     请求重试（暂无实现）
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              返回成功后的数据
    </T> */
    fun <T> postRequest(sourceUrl: String, params: Map<String, Any?>?, clazz: Class<T>, needRetry: Boolean): Observable<T> {
        return request(TYPE_POST, sourceUrl, params, clazz, /*CacheTime.NOT_CACHE*/0)
    }

    /**
     * POST请求   处理返回值成<T>，有缓存（暂无实现）
     *
     * @param sourceUrl     接口号
     * @param params        请求中data对应的参数，一个json格式数据
     * @param clazz         请求成功对象data里面的实体对象
     * @param needRetry     请求重试（暂无实现）
     * @param cacheTime     缓存时间
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              返回成功后的数据
    </T> */
    fun <T> postRequest(sourceUrl: String, params: Map<String, Any?>?, clazz: Class<T>, needRetry: Boolean, cacheTime: Int): Observable<T> {
        return request(TYPE_POST, sourceUrl, params, clazz, cacheTime)
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
     * GET请求  处理返回值成<T>，无缓存
     *
     * @param sourceUrl     接口号+参数
     * @param clazz         请求成功对象data里面的实体对象
     * @param needRetry     请求重试（暂无实现）
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              返回成功后的数据
    </T> */
    fun <T> getRequest(sourceUrl: String, clazz: Class<T>, needRetry: Boolean): Observable<T> {
        return request(TYPE_GET, sourceUrl, null, clazz, /*CacheTime.NOT_CACHE*/0)
    }

    /**
     * GET请求  处理返回值成<T>，有缓存（暂无实现）
     *
     * @param sourceUrl     接口号+参数
     * @param clazz         请求成功对象data里面的实体对象
     * @param needRetry     请求重试（暂无实现）
     * @param cacheTime     缓存时间
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              返回成功后的数据
    </T> */
    fun <T> getRequest(sourceUrl: String, clazz: Class<T>, needRetry: Boolean, cacheTime: Int): Observable<T> {
        return request(TYPE_GET, sourceUrl, null, clazz, cacheTime)
    }

    private fun <T> request(requestType: Int, sourceUrl: String, params: Map<String, Any?>?, clazz: Class<T>, cacheTime: Int): Observable<T> {
        //没网络主动抛出，程序决定是否处理
        return if (!isNetworkConnected()) {
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
                    if (ErrorCode.SERVER_SUCCESS == baseEntity.errno) {
                        Observable.create { subscriber ->
                            if (baseEntity.data == null) {
                                val commonData = Any()
                                subscriber.onNext(commonData as T)
                            } else {
                                try {
                                    val data = baseEntity.data
                                    Logger.i("Fungo Request OK---> sourceUrl ：" + sourceUrl + "\n success response : ---> " + data!!.toString())
                                    /*if (cacheTime != CacheTime.NOT_CACHE) {
                                                ACache.get(BaseApplication.getApplication()).put(new CacheHelper().getCacheKey(sourceUrl, params), data.toString(), cacheTime);
                                            }*/
                                    val bean = GsonUtils.fromJson(data.toString(), clazz)
                                    if (!subscriber.isDisposed) {
                                        subscriber.onNext(bean)
                                    }
                                } catch (e: Exception) {
                                    Logger.e("logout" + " :" + e.message)
                                    //compare to server,the mDataBean's type of client is different
                                    if (!subscriber.isDisposed) {
                                        subscriber.onError(RequestError(ErrorCode.JSON_DATA_ERROR, baseEntity.toString()))
                                    }
                                }
                            }
                        }
                    } else {
                        Logger.e("logout" + " new RequestError: baseEntity.errno = " + baseEntity.errno + ",baseEntity.desc = " + baseEntity.desc)
                        Observable.error(RequestError(baseEntity.errno, baseEntity.desc!!, RequestError.TYPE_SERVER))
                    }
                })
                .onErrorResumeNext(Function<Throwable, ObservableSource<T>> { error ->
                    val e = convertError(error)
                    if (e is RequestError) {
                        //在这里做全局的错误处理
                        Logger.i("Fungo Request Error--->  sourceUrl ：" + sourceUrl + "\n Error Code : ---> " + e.state + "\n Error message : ---> " + e.toString())
                        e.printStackTrace()
                    } else {
                        Logger.i("Fungo Request Error--->  sourceUrl ：" + sourceUrl + "\n Error message : ---> " + e.toString())
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

    private fun isNetworkConnected(): Boolean {
        val s = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (s != null) {
            val cm = s as ConnectivityManager
            val ni = cm.activeNetworkInfo

            if (ni != null && ni.isConnectedOrConnecting) {
                true
            } else {
                Logger.e("网络连接异常，请检查你的网络！")
                false
            }

        } else {
            false
        }
    }
}


