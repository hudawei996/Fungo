package com.fungo.netgo.subscribe;

import com.fungo.netgo.model.IModel;


/**
 * @author Pinger
 * @since 18-09-12 下午17:21
 * <p>
 * 请求订阅者，请求的各种状态都进行封装处理
 */
public abstract class ApiSubscriber<T extends IModel> extends ResourceSubscriber<T> {


    /**
     * 请求开始
     */
    @Override
    protected void onStart() {
    }

    /**
     * 请求成功
     */
    protected abstract void onSuccess(T t);

    /**
     * 请求异常
     */
    protected void onError(ApiException e) {
    }

    /**
     * 请求完成
     */
    @Override
    public void onComplete() {
    }

    @Override
    final public void onNext(T t) {
        onSuccess(t);
    }


    /**
     * 分发一下异常
     */
    @Override
    final public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, NetError.UNKNOWN));
        }
    }
}
