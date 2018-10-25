package com.fungo.netgo.subscribe;

import com.fungo.netgo.callback.CallBack;
import com.fungo.netgo.exception.ApiException;
import com.fungo.netgo.exception.NetError;

import okhttp3.ResponseBody;

/**
 * @author Pinger
 * @since 18-09-12 下午17:21
 * <p>
 * 请求订阅者，请求的各种状态都交给CallBack去处理
 */
public class RxSubscriber<T> extends BaseSubscriber<T> {

    private CallBack<T> mCallBack;

    public RxSubscriber(CallBack<T> callBack) {
        this.mCallBack = callBack;
    }

    @Override
    protected void onError(ApiException e) {
        mCallBack.onError(e);
    }

    @Override
    protected void onStart() {
        mCallBack.onStart();
    }


    @Override
    public void onNext(T t) {
//        try {
//            mCallBack.onSuccess(mCallBack.convertResponse(responseBody));
//        } catch (Exception e) {
//            mCallBack.onError(new ApiException(NetError.MSG_PARSE_ERROR, NetError.PARSE_ERROR));
//            e.printStackTrace();
//        }

        mCallBack.onSuccess(t);
    }

    @Override
    public void onComplete() {
        mCallBack.onFinish();
    }
}
