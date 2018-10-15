package com.fungo.sample.main.recycler

import android.view.ViewGroup
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 18-7-26 上午11:35
 *
 */

class RecyclerTextViewHolder : MultiTypeViewHolder<RecyclerTextBean, RecyclerTextViewHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent)
    }

    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<RecyclerTextBean>(parent, R.layout.holder_recycler_text) {
        override fun onBindData(data: RecyclerTextBean) {
            setText(R.id.tvText, data.title)
        }
    }


}