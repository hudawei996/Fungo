package com.fungo.netgo.cache.policy;

import com.fungo.netgo.cache.CacheFlowable;
import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.subscribe.RxSubscriber;
import com.fungo.netgo.utils.RxUtils;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;

/**
 * @author Pinger
 * @since 18-10-25 上午10:08
 */
public class FirstCacheRequestPolicy<T> extends BaseCachePolicy<T> {

    public FirstCacheRequestPolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public T requestSync() {




        return super.requestSync();
    }


    @Override
    public void requestAsync() {
        Flowable
                .concat(prepareCacheFlowable(), prepareAsyncRequestFlowable())
                .firstElement()
                .concatMap(new Function<CacheFlowable<T>, MaybeSource<T>>() {
                    @Override
                    public MaybeSource<T> apply(CacheFlowable<T> cacheFlowable) {


                        System.out.println("-----------> 缓存之后，加载之前--------");


                        return Maybe.just(cacheFlowable.data);
                    }
                })
                .toFlowable()
                .compose(RxUtils.<T>getScheduler())
                .onErrorResumeNext(RxUtils.<T>getErrorFunction())
                .subscribe(new RxSubscriber<>(mRequest.getCallBack()));
    }

    @Override
    protected Flowable<CacheFlowable<T>> prepareCacheFlowable() {


        return super.prepareCacheFlowable();
    }
}
