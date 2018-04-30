package com.fungo.baselib.social.pay.listener;

/**
 * @author Pinger
 * @since 2018/4/30 20:05
 */
public interface OnPayRequestListener {
    void onPayInfoRequetStart();

    void onPayInfoRequstSuccess();

    void onPayInfoRequestFailure();
}