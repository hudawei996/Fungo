package org.fungo.basenetlib.utils

import org.fungo.basenetlib.config.BaseRequestInfo
import android.net.Uri

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     请求参数拼接
 */
object ParamsUtils {

    fun getPostBody(sourceUrl: String, params: Map<String, Any?>?): BaseRequestInfo {
        val baseRequestInfo = BaseRequestInfo(params)
        Logger.i("Fungo Request Post--->  sourceUrl ：" + sourceUrl + "\n request body : ---> " + GsonUtils.toJson(baseRequestInfo))
        return baseRequestInfo
    }

    fun appendUrlParams(sourceUrl: String, params: Map<String, Any?>?): String {
        var url = sourceUrl

        if (params != null && params.isNotEmpty()) {
            val builder = Uri.parse(sourceUrl).buildUpon()
            for ((k, v) in params) {
                if (v != null) {
                    builder.appendQueryParameter(k, v.toString())
                }
            }
            url = builder.build().toString()
        }
        Logger.i("Fungo Request Get--->  url ：" + url)
        return url
    }
}