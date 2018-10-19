package com.fungo.sample.netgo;


import com.fungo.netgo.NetGo;
import com.fungo.netgo.subscribe.DespiteSubscribe;

import io.reactivex.Flowable;


/**
 * 网络请求的API统一管理
 */
public class Api {

    private static final String API_BASE_URL = "http://gank.io/api/";

    private static NetGo getApi() {
        return NetGo.getInstance().getApi(API_BASE_URL);
    }


    public static Flowable<GankResults> getGankData() {
        String url = API_BASE_URL + "data/Android/30/1";
        return getApi().getRequest(url,GankResults.class);
    }

    public static Flowable<String> getGankString() {
        String url = API_BASE_URL + "data/Android/30/1";
        return getApi().getRequest(url,String.class);
    }


    public static void getGankDespite(){
        String url = API_BASE_URL + "data/Android/30/1";
        getApi().getRequest(url).subscribe(new DespiteSubscribe());
    }


}
