package com.fungo.sample.netgo

import android.view.ViewGroup
import com.fungo.baseuilib.recycler.BaseViewHolder
import com.fungo.baseuilib.recycler.multitype.MultiTypeViewHolder
import com.fungo.baselib.web.WebActivity
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 2018/10/16 0:35
 */
class NetGoHolder : MultiTypeViewHolder<GankResults.Item, NetGoHolder.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent)
    }


    class ViewHolder(parent: ViewGroup) : BaseViewHolder<GankResults.Item>(parent, R.layout.holder_netgo) {
        override fun onBindData(data: GankResults.Item) {
            setText(R.id.textView, data.desc)
        }

        override fun onItemClick(data: GankResults.Item) {
            WebActivity.start(getContext()!!, data.url)
        }

    }
}