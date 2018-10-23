package com.fungo.netgo.request.base;

import com.fungo.netgo.request.RequestMethod;

import okhttp3.RequestBody;

/**
 * @author Pinger
 * @since 18-10-23 下午3:16
 * <p>
 * 有请求体的请求，一般为post
 */
public abstract class BodyRequest<T,R extends Request> extends Request<T,R> {

    public BodyRequest(String url, ApiService service) {
        super(url, service);
    }

    @Override
    public RequestMethod getMethod() {
        return RequestMethod.POST;
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

}
