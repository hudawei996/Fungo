package com.leibo.baselib.net.base

import com.google.gson.JsonElement

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     服务器返回数据结构
 */
class BaseEntity {
    /**
     * {
     * "desc": "OK",
     * "errno": 0
     * "data": {
     * "auth": 0,
     * "balance": 0,
     * "expire": 2592000,
     * "head": "",
     * "netid": "d2d64c9a7d4a4f62bf87d6aaf5a6e884",
     * "nettoken": "783a265adecd4d33a34315b33d062d7f",
     * "nickname": "手机用户8467",
     * "privdata": "49d57d5109af40398127182d9f2e6260",
     * "qnettoken": "eJxlj11PgzAUhu-5FYTbGW1LC53JLhi4j0xiNhQXb0il7dLgAFk3Poz-3Q2XSOK5fZ73Ped8GaZpWs*P0S1L0*KY60S3pbDMe9MC1s0fLEvFE6YTu*L-oGhKVYmESS2qHkJCCAJg6Cgucq2kuhoccQenY*ZyzLB00LukLncYk4Q5glI8SB54lvTrf6vxuReOEUZDRe16GD6s-WUQtBAUjTgn9Kmx27WYZdsFxE-h6*gtWpE63gbSj9UxfPHUtO54HH1sullDNZzPl-WUdGKf3fmZF*0Cvfh0qLsa1bqW3mQyWKnVXlwPotAFlNrDb0*iOqgi7wUEIIHIBpexjG-jB5*eZPA_",
     * "test": 0,
     * "token": "3aa3bcfc821b4c5b83c3483a2ed43e8c",
     * "uid": 14365973
     * }
     * }
     *
     * }
     */

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
