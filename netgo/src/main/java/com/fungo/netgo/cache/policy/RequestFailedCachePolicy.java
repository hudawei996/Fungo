package com.fungo.netgo.cache.policy;

import com.fungo.netgo.request.base.Request;

/**
 * @author Pinger
 * @since 18-10-25 上午10:09
 */
public class RequestFailedCachePolicy<T> extends BaseCachePolicy<T> {

    public RequestFailedCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }
}
