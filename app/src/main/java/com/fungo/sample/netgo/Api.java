package com.fungo.sample.netgo;


import com.fungo.netgo.NetGo;
import com.fungo.netgo.callback.JsonCallBack;
import com.fungo.netgo.callback.StringCallBack;


/**
 * 网络请求的API统一管理
 */
public class Api {

    private static final String API_BASE_URL = "http://gank.io/api/";

    private static NetGo getApi() {
        return NetGo.getInstance().getApi(API_BASE_URL);
    }


    public static void getGankData(JsonCallBack<GankResults> callBack) {
        String url = "data/Android/30/1";
        getApi().<GankResults>get(url).execute(callBack);
    }

    public static void getGankString(StringCallBack callBack) {
        String url = "data/Android/30/1";
        getApi().<String>get(url).execute(callBack);
    }


}
