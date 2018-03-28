package org.fungo.basenetlib.retrofit

import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.leibo.basenet.api.NetApi
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import org.fungo.basenetlib.cache.ACache
import org.fungo.basenetlib.cache.CacheHelper
import org.fungo.basenetlib.cache.CacheTime
import org.fungo.basenetlib.config.BaseEntity
import org.fungo.basenetlib.config.ErrorCode
import org.fungo.basenetlib.config.ErrorDesc
import org.fungo.basenetlib.config.RequestError
import org.fungo.basenetlib.utils.GsonUtils
import org.fungo.basenetlib.utils.Logger
import org.fungo.basenetlib.utils.ParamsUtils
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
class FungoRequest(private val context: Context, private val fungoApi: NetApi) {

    private companion object {
        const val TYPE_POST = 0
        const val TYPE_GET = 1
    }

    /**
     * POST请求  无返回
     *
     * @param sourceUrl 接口号
     * @param params    请求中data对应的参数，一个json格式数据
     *
     */
    fun postRequest(sourceUrl: String, params: Map<String, Any?>?) {
        postRequestApi(sourceUrl, params)
    }

    /**
     * POST请求   处理返回值成 Observable<T>
     *
     * @param sourceUrl     接口号
     * @param clazz         请求成功对象data里面的实体对象
     * @param params        请求中data对应的参数，一个json格式数据,(可不传，默认null)
     * @param cacheTime     缓存时间，(可不传，默认CacheTime.NOT_CACHE)
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              Observable<T> 返回成功后的数据
    </T> */
    @JvmOverloads
    fun <T> postRequest(sourceUrl: String, clazz: Class<T>, params: Map<String, Any>? = null, cacheTime: Int = CacheTime.NOT_CACHE): Observable<T> {
        return request(TYPE_POST, sourceUrl, params, clazz, cacheTime)
    }

    /**
     * GET请求  无返回
     *
     * @param sourceUrl 接口号
     * @param params    请求中data对应的参数，一个json格式数据
     *
     */
    fun getRequest(sourceUrl: String, params: Map<String, Any?>?) {
        getRequestApi(sourceUrl, params)
    }

    /**
     * GET请求  处理返回值成 Observable<T>
     *
     * @param sourceUrl     接口号
     * @param clazz         请求成功对象data里面的实体对象
     * @param params        请求中data对应的参数，一个json格式数据,(可不传，默认null)
     * @param cacheTime     缓存时间，(可不传，默认CacheTime.NOT_CACHE)
     * @param <T>           请求成功对象data里面的实体对象，对过clazz确定
     * @return              Observable<T> 返回成功后的数据
    </T> */
    @JvmOverloads
    fun <T> getRequest(sourceUrl: String, clazz: Class<T>, params: Map<String, Any>? = null, cacheTime: Int = CacheTime.NOT_CACHE): Observable<T> {
        return request(TYPE_GET, sourceUrl, params, clazz, cacheTime)
    }

    private fun postRequestApi(sourceUrl: String, params: Map<String, Any?>?): Observable<BaseEntity> {
        val encryptionParams = ParamsUtils.getPostBody(sourceUrl, params)

        return if (sourceUrl.contains("http:") || sourceUrl.contains("https:")) {
            fungoApi.postRequestWithFullUrl(sourceUrl, encryptionParams)
        } else {
            fungoApi.postRequest(sourceUrl, encryptionParams)
        }
    }

    private fun getRequestApi(sourceUrl: String, params: Map<String, Any?>?): Observable<BaseEntity> {
        val url = ParamsUtils.appendUrlParams(sourceUrl, params)

        return if (url.contains("http:") || url.contains("https:")) {
            fungoApi.getRequestWithFullUrl(url)
        } else {
            fungoApi.getRequest(url)
        }
    }

    private fun <T> request(requestType: Int, sourceUrl: String, params: Map<String, Any>?, clazz: Class<T>, cacheTime: Int): Observable<T> {
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
                        postRequestApi(url, params)
                    } else {
                        getRequestApi(url, params)
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
                                    if (cacheTime != CacheTime.NOT_CACHE) {
                                        ACache[context].put(CacheHelper<T>(context).getCacheKey(sourceUrl, params), data.toString(), cacheTime)
                                    }
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


