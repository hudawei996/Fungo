package com.fungo.netgo.request.base;

import com.fungo.netgo.utils.HttpUtils;

import okhttp3.RequestBody;

/**
 * @author Pinger
 * @since 18-10-23 下午2:12
 * <p>
 * 生成没有请求体的请求,一般为get请求
 */
public abstract class NoBodyRequest<T, R extends NoBodyRequest> extends Request<T, R> {


    public NoBodyRequest(String url, ApiService service) {
        super(url, service);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

}
