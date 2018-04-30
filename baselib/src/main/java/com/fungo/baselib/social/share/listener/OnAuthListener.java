package com.fungo.baselib.social.share.listener;


import com.fungo.baselib.social.share.config.PlatformType;

import java.util.Map;

/**
 * Created by tsy on 16/8/4.
 */
public interface OnAuthListener {
    void onComplete(PlatformType platform_type, Map<String, String> map);

    void onError(PlatformType platform_type, String err_msg);

    void onCancel(PlatformType platform_type);
}