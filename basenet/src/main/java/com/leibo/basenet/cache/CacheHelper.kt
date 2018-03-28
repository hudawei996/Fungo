package org.fungo.basenetlib.cache

import org.fungo.basenetlib.utils.GsonUtils
import org.fungo.basenetlib.utils.Logger
import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     缓存
 */
class CacheHelper<T>(private val context: Context) {

    fun getCache(url: String, params: Map<String, Any>, clazz: Class<T>): CacheEntity<T> {
        var content: String? = null
        try {//因为里面有个方法makeDir失败
            content = ACache[context].getAsString(getCacheKey(url, params))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val cacheEntity = CacheEntity<T>()
        if (!TextUtils.isEmpty(content)) {
            Logger.i("CacheHelper--cacheContent=" + content!!)
            try {//因为出现gson解析异常
                cacheEntity.data = GsonUtils.fromJson(content, clazz)
                cacheEntity.status = 1
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return cacheEntity
    }

    fun getCacheKey(url: String, params: Map<String, Any>?): String {
        var cacheKey = url
        if (params != null) {
            val g = Gson()
            cacheKey = StringBuilder(100).append(url).append(g.toJsonTree(params).asJsonObject.toString()).toString()
        }
        Logger.i("CacheHelper--cacheKey=" + cacheKey)
        return cacheKey
    }

    fun clearCache() {
        val cacheFile = ACache[context].cacheFile
        if (cacheFile != null && cacheFile.exists()) {
            cacheFile.delete()
        }
    }
}
