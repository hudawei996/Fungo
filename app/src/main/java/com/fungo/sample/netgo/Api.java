package com.fungo.sample.netgo;


import com.fungo.netgo.NetGo;
import com.fungo.netgo.subscribe.ApiSubscriber;

/**
 * Created by wanglei on 2016/12/31.
 */

public class Api<T> {

    private static final String API_BASE_URL = "http://gank.io/api/";

    private static NetGo getApi() {
        return NetGo.getInstance().getApi(API_BASE_URL);
    }


    public static void getGankData(ApiSubscriber<GankResults> apiSubscriber){
        String url = API_BASE_URL + "data/Android/30/1";
        getApi().getRequest(url).subscribe(apiSubscriber);
    }
}
