package org.fungo.basenetlib.config

import com.google.gson.JsonElement

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     服务器返回统一数据结构,这个需要根据各个项目服务器数据结构而改变
 */
class BaseEntity {





    var desc: String? = null
    var errno: Int = 0
    var data: JsonElement? = null

    override fun toString(): String {
        return "BaseEntity{" +
                "errno=" + errno +
                ", desc='" + desc +
                '}'
    }

}
