package com.leibo.baseuilib.view.banner.holder

import android.content.Context
import android.view.View

/**
 * Created by zhouwei on 17/5/26.
 */

interface MZViewHolder<in T> {
    /**
     * 创建View
     * @param context
     * @return
     */
    fun createView(context: Context): View

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    fun onBind(context: Context, position: Int, data: T)
}
