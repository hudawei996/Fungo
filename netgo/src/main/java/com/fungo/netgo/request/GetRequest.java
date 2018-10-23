package com.fungo.netgo.request;

import com.fungo.netgo.request.base.ApiService;
import com.fungo.netgo.request.base.NoBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Pinger
 * @since 18-10-23 下午2:11
 */
public class GetRequest<T> extends NoBodyRequest<T, GetRequest<T>> {

    public GetRequest(String url, ApiService service) {
        super(url, service);
    }

    @Override
    public RequestMethod getMethod() {
        return RequestMethod.GET;
    }


}
