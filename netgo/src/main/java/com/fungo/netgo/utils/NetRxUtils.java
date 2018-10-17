package com.fungo.netgo.utils;

import com.fungo.netgo.model.IModel;
import com.fungo.netgo.NetError;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Pinger
 * @since 18-10-17 上午11:23
 */
public class NetRxUtils {

    /**
     * 线程切换
     *
     * @return
     */
    public static <T extends IModel> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 异常处理变换
     *
     * @return
     */
    public static <T extends IModel> FlowableTransformer<T, T> getApiTransformer() {

        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.flatMap(new Function<T, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(T model) throws Exception {

                        if (model == null || model.isNull()) {
                            return Flowable.error(new NetError(model.getErrorMsg(), NetError.NoDataError));
                        } else if (model.isAuthError()) {
                            return Flowable.error(new NetError(model.getErrorMsg(), NetError.AuthError));
                        } else if (model.isBizError()) {
                            return Flowable.error(new NetError(model.getErrorMsg(), NetError.BusinessError));
                        } else {
                            return Flowable.just(model);
                        }
                    }
                });
            }
        };
    }


}
