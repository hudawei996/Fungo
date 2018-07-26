package com.fungo.baselib.view.banner.holder

/**
 * Created by zhouwei on 17/5/26.
 */

interface ViewPagerHolderCreator<out VH : ViewPagerHolder<*>> {
    /**
     * 创建ViewHolder
     * @return
     */
    fun createViewPagerHolder(): VH
}
