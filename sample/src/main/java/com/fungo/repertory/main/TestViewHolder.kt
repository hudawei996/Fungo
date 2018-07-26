package com.fungo.repertory.main

import android.view.ViewGroup
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 18-7-26 上午11:35
 *
 */

class TestViewHolder : MultiTypeViewHolder<TestBean, TestViewHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent)
    }


    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<TestBean>(parent, R.layout.holder_test) {
        override fun onBindData(data: TestBean) {
            setText(R.id.tvText, data.title)
        }

    }


}