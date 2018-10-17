package com.fungo.sample.netgo;


import com.fungo.netgo.model.IModel;


/**
 * @author Pinger
 * @since 18-10-14 下午13:01
 * <p>
 * Gank方法API的数据实体基类
 */

public class GankModel implements IModel {

    public boolean error;

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public int getCode() {
        return 0;
    }
}
