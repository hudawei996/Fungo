package com.fungo.netgo.utils;

import com.fungo.netgo.error.ApiException;
import com.fungo.netgo.error.NetError;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author Pinger
 * @since 18-10-23 下午4:42
 */
public class RxUtils {


    /**
     * 错误的处理方法
     */
    public static Function<Throwable, Publisher<? extends ResponseBody>> getErrorFuntion() {
        return new Function<Throwable, Publisher<? extends ResponseBody>>() {
            @Override
            public Publisher<? extends ResponseBody> apply(Throwable throwable) {
                return Flowable.error(NetError.handleException(throwable));
            }
        };

    }


    /**
     * 内部的错误处理封装
     */
    public static <T> Flowable<T> requestError(String message, int code) {
        return Flowable.error(new ApiException(message, code));
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

}
