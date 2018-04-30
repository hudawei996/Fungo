package com.fungo.baselib.social.pay.entity;

import android.app.Activity;

/**
 * @author Pinger
 * @since 2018/4/30 20:09
 */

public class PayParams {

    private Activity mActivity;
    private String mWechatAppID;
    private PayType mPayType;
    private int mGoodsPrice;
    private String mGoodsName;
    private String mGoodsIntroduction;
    private String mApiUrl;

    public Activity getActivity() {
        return mActivity;
    }

    private void setActivity(Activity activity) {
        mActivity = activity;
    }

    public String getWeChatAppID() {
        return mWechatAppID;
    }

    private void setWechatAppID(String id) {
        mWechatAppID = id;
    }

    public PayType getPayWay() {
        return mPayType;
    }

    private void setPayWay(PayType mPayWay) {
        this.mPayType = mPayWay;
    }

    public int getGoodsPrice() {
        return mGoodsPrice;
    }

    private void setGoodsPrice(int mGoodsPrice) {
        this.mGoodsPrice = mGoodsPrice;
    }

    public String getGoodsName() {
        return mGoodsName;
    }

    private void setGoodsName(String mGoodsTitle) {
        this.mGoodsName = mGoodsTitle;
    }

    public String getGoodsIntroduction() {
        return mGoodsIntroduction;
    }

    private void setGoodsIntroduction(String mGoodsIntroduction) {
        this.mGoodsIntroduction = mGoodsIntroduction;
    }

    public String getApiUrl() {
        return mApiUrl;
    }

    private void setApiUrl(String mApiUrl) {
        this.mApiUrl = mApiUrl;
    }

    public static class Builder {
        Activity mActivity;
        String wechatAppId;
        PayType payWay;
        int goodsPrice;
        String goodsName;
        String goodsIntroduction;
        String apiUrl;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public PayParams.Builder wechatAppID(String appid) {
            wechatAppId = appid;
            return this;
        }

        public PayParams.Builder goodsPrice(int price) {
            goodsPrice = price;
            return this;
        }

        public PayParams.Builder goodsName(String name) {
            goodsName = name;
            return this;
        }

        public PayParams.Builder goodsIntroduction(String introduction) {
            goodsIntroduction = introduction;
            return this;
        }

        public PayParams.Builder requestBaseUrl(String url) {
            apiUrl = url;
            return this;
        }

        public PayParams build() {
            PayParams params = new PayParams();

            params.setActivity(mActivity);
            params.setWechatAppID(wechatAppId);
            params.setPayWay(payWay);
            params.setGoodsPrice(goodsPrice);
            params.setGoodsName(goodsName);
            params.setGoodsIntroduction(goodsIntroduction);
            params.setApiUrl(apiUrl);

            return params;
        }

    }

}