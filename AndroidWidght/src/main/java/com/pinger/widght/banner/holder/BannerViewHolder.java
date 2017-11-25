package com.pinger.widght.banner.holder;

import android.content.Context;
import android.view.View;

/**
 * @author pinger
 * @since 2017/11/24 14:36
 */
public interface BannerViewHolder<T> {

    /**
     * 创建Banner的ItemView
     */
    View createView(Context context);

    /**
     * 绑定Item数据
     */
    void onBind(Context context, int position, T data);
}
