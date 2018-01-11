package com.pinger.widget.view.banner.holder;

/**
 * @author pinger
 * @since 2017/11/24 16:57
 */

public interface BannerHolderCreator<H extends BannerViewHolder> {
    /**
     * 创建ViewHolder
     */
    H createViewHolder();
}
