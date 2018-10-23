package com.fungo.netgo.request;

import com.fungo.netgo.request.base.ApiService;
import com.fungo.netgo.request.base.BodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Pinger
 * @since 18-10-23 下午3:16
 */
public class PostRequest<T> extends BodyRequest<T, PostRequest<T>> {

    public PostRequest(String url, ApiService service) {
        super(url, service);
    }


}
