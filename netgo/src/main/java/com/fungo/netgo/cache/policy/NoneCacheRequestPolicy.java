package com.fungo.netgo.cache.policy;

import com.fungo.netgo.request.base.Request;

/**
 * @author Pinger
 * @since 18-10-25 上午10:08
 */
public class NoneCacheRequestPolicy<T> extends BaseCachePolicy<T> {

    public NoneCacheRequestPolicy(Request<T, ? extends Request> request) {
        super(request);
    }
}
