package com.fungo.baselib.view.banner.holder

/**
 * Created by zhouwei on 17/5/26.
 */

interface MZHolderCreator<out VH : MZViewHolder<*>> {
    /**
     * 创建ViewHolder
     * @return
     */
    fun createViewHolder(): VH
}
