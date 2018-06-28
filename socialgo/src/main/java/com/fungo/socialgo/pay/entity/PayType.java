package com.fungo.socialgo.pay.entity;

/**
 * @author Pinger
 * @since 2018/4/30 20:06
 */
public enum PayType {
    WxPay(0),
    AliPay(1);

    int payType;

    PayType(int way) {
        this.payType = way;
    }

    @Override
    public String toString() {
        return String.valueOf(payType);
    }
}
