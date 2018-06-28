package com.fungo.socialgo.pay.paystrategy;

import com.fungo.socialgo.pay.PayApi;
import com.fungo.socialgo.pay.entity.PayParams;


/**
 * Author: michaelx
 * Create: 17-3-13.
 * <p>
 * Endcode: UTF-8
 * <p>
 * Blog:http://blog.csdn.net/xiong_it | https://xiong-it.github.io
 * github:https://github.com/xiong-it
 * <p>
 * Description:支付策略类抽象类.
 */

public abstract class BasePayStrategy implements PayStrategyInterf {
    protected PayParams mPayParams;
    protected String mPrePayInfo;
    protected PayApi.PayCallBack mOnPayResultListener;

    public BasePayStrategy(PayParams params, String prePayInfo, PayApi.PayCallBack resultListener) {
        mPayParams = params;
        mPrePayInfo = prePayInfo;
        mOnPayResultListener = resultListener;
    }

    public abstract void doPay();
}