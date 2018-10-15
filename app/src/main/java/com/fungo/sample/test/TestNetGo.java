package com.fungo.sample.test;

import com.fungo.netgo.ApiSubscriber;
import com.fungo.netgo.NetError;
import com.fungo.netgo.NetGo;
import com.fungo.sample.netgo.Api;
import com.fungo.sample.netgo.GankResults;

/**
 * @author Pinger
 * @since 18-10-15 下午7:24
 */
public class TestNetGo {


    public static void main(String[] args) {
        Api.getGankService().getGankData("Android", 30, 1)
                .compose(NetGo.<GankResults>getApiTransformer())
                .compose(NetGo.<GankResults>getScheduler())
                .subscribe(new ApiSubscriber<GankResults>() {
                    @Override
                    protected void onFail(NetError error) {

                    }

                    @Override
                    public void onNext(GankResults gankResults) {

                    }
                });

    }
}
