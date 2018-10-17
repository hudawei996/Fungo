package com.fungo.sample.test;

import com.fungo.netgo.subscribe.ApiSubscriber;
import com.fungo.netgo.subscribe.NetError;
import com.fungo.netgo.utils.NetRxUtils;
import com.fungo.sample.netgo.Api;
import com.fungo.sample.netgo.GankResults;

/**
 * @author Pinger
 * @since 18-10-15 下午7:24
 */
public class TestNetGo {


    public static void main(String[] args) {
        Api.getGankService().getGankData("Android", 30, 1)
                .compose(NetRxUtils.<GankResults>getApiTransformer())
                .compose(NetRxUtils.<GankResults>getScheduler())
                .subscribe(new ApiSubscriber<GankResults>() {

                    @Override
                    protected void onSuccess(GankResults gankResults) {

                    }
                });

    }
}
