package com.fungo.sample.netgo;


import com.fungo.netgo.NetGo;

import io.reactivex.Flowable;

/**
 * Created by wanglei on 2016/12/31.
 */

public class Api {

    private static final String API_BASE_URL = "http://gank.io/api/";

    private static NetGo getApi() {
        return NetGo.getInstance().getApi(API_BASE_URL);
    }


    public static Flowable<GankResults> getGankData() {
        String url = API_BASE_URL + "data/Android/30/1";
        return getApi().getRequest(url);
    }
}
