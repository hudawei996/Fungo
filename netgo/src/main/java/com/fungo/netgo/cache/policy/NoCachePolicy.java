package com.fungo.netgo.cache.policy;

import com.fungo.netgo.cache.CacheFlowable;
import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.subscribe.RxSubscriber;
import com.fungo.netgo.utils.RxUtils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author Pinger
 * @since 18-10-25 上午10:08
 */
public class NoCachePolicy<T> extends BaseCachePolicy<T> {

    public NoCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void requestAsync() {
        prepareAsyncRequestFlowable()
                .flatMap(new Function<CacheFlowable<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(CacheFlowable<T> cacheFlowable) {
                        return Flowable.just(cacheFlowable.data);
                    }
                })
                .compose(RxUtils.<T>getScheduler())
                .onErrorResumeNext(RxUtils.<T>getErrorFunction())
                .subscribe(new RxSubscriber<>(mRequest.getCallBack()));
    }

    @Override
    public T requestSync() {
        return prepareSyncRequest().data;
    }
}
