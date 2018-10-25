package com.fungo.netgo.utils;

import com.fungo.netgo.cache.CacheFlowable;
import com.fungo.netgo.cache.CacheManager;
import com.fungo.netgo.callback.CallBack;
import com.fungo.netgo.exception.NetError;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Pinger
 * @since 18-10-23 下午4:42
 */
public class RxUtils {


    /**
     * 获取转换结果
     */
    public static <T> Function<Response, Publisher<T>> getResultFunction(final CallBack<T> callBack) {
        return new Function<Response, Publisher<T>>() {
            @Override
            public Publisher<T> apply(Response response) throws Exception {

                System.out.println("-----------> 请求网络数据成功--------");

                return Flowable.just(callBack.convertResponse(response));
            }
        };
    }


    /**
     * 错误的处理方法
     */
    public static <T> Function<Throwable, Publisher<T>> getErrorFunction() {
        return new Function<Throwable, Publisher<T>>() {
            @Override
            public Publisher<T> apply(Throwable throwable) {
                return Flowable.error(NetError.handleException(throwable));
            }
        };

    }


    /**
     * 线程切换
     */
    public static <T> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 网络数据转成缓存Flowable统一处理
     */
    public static <T> Function<T, Publisher<CacheFlowable<T>>> getCacheFunction() {
        return new Function<T, Publisher<CacheFlowable<T>>>() {
            @Override
            public Publisher<CacheFlowable<T>> apply(T t) {
                return Flowable.just(new CacheFlowable<>(false, t));
            }
        };
    }
}
