package org.fungo.basenetlib.config

import com.google.gson.Gson
import com.google.gson.JsonObject

import java.io.Serializable

/**
 * Author  ZYH
 * Date    2018/1/24
 * Des     请求服务器统一封装实体类,这个需要根据各个项目服务器数据结构而改变
 */
class BaseRequestInfo(params: Map<String, Any?>?) : Serializable {

    var uid: Long = 0//用户ID
    var token: String? = null//Token
    val term: Int = 0//终端类型
    var version: Int = 0//当前版本
    var ts: Long = 0//时间戳
    var udid: String? = null//设备ID
    var nettype: Int = 0//网络类型
    var model: String? = null//手机型号
    var channel: String? = null//渠道
    var data: JsonObject? = null//数据体
    var app: String? = null//包名
    var featureVersion: Long = 20171215//每次发版本的控制字段
    var sign: String? = null//签名

    init {
        data = if (params != null) {
            val g = Gson()
            g.toJsonTree(params).asJsonObject
        } else {
            null
        }
        /* uid = AccountManager.getInstance().getAccount().uid;
        token = AccountManager.getInstance().getAccount().token;
        Logger.i("request token 参数token：" + token);
        version = BaseAppInfoManager.getInstance().version;//app版本

        ts = System.currentTimeMillis();//时间
        udid = BaseAppInfoManager.getInstance().udid;//手机设备相关
        model = Build.MODEL;//手机型号
        channel = BaseAppInfoManager.getInstance().channel;//渠道
        app = BaseAppInfoManager.getInstance().app;//包名
        //0代表wifi ,1 4g（无网络也是4G）
        nettype = NetworkUtil.getNetworkType(BaseApplication.getApplication()) == NetworkUtil.NETTYPE_WIFI ?
                NetworkUtil.NETTYPE_WIFI : NetworkUtil.NETTYPE_MOBILE;
        sign = StringUtils.sha256Hex(new StringBuilder().append(InterceptorUrl.secrectKey).append(uid).append(ts).toString());*/

        //示例，抓包修改即可
        uid = java.lang.Long.parseLong("55885588")
        token = "696b6cab073840449628b0dc718a7cba"
        version = 210
        ts = java.lang.Long.parseLong("1516424241640")
        udid = "3ab408108dc77c1f238579abac216fdb"
        model = "SM-G9350"
        channel = "umeng"
        app = "com.happyfishing.fungo"
        nettype = 0
        sign = "5e975015d887f73c96c6f27b4ad0906ace7d427316b67112660daedd0feca871"
    }
}
