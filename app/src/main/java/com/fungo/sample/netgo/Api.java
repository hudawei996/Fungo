package com.fungo.sample.netgo;


import com.fungo.netgo.NetGo;

/**
 * Created by wanglei on 2016/12/31.
 */

public class Api {
    public static final String API_BASE_URL = "http://gank.io/api/";

    private static GankService gankService;

    public static GankService getGankService() {
        if (gankService == null) {
            synchronized (Api.class) {
                if (gankService == null) {
                    gankService = NetGo.getInstance().getRetrofitService(API_BASE_URL, GankService.class);
                }
            }
        }
        return gankService;
    }
}
