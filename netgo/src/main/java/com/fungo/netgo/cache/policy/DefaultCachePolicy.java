package com.fungo.netgo.cache.policy;

import com.fungo.netgo.request.base.Request;

/**
 * @author Pinger
 * @since 18-10-25 上午10:07
 */
public class DefaultCachePolicy<T> extends BaseCachePolicy<T> {

    public DefaultCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }
}
