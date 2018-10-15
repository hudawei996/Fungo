package com.fungo.sample.main

import android.view.ViewGroup
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.sample.R
import com.fungo.sample.imagego.ImageGoFragment
import com.fungo.sample.netgo.NetGoFragment

/**
 * @author Pinger
 * @since 2018/10/15 23:44
 */
class MainHolder : MultiTypeViewHolder<MainBean, MainHolder.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent)
    }

    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<MainBean>(parent, R.layout.holder_main) {
        override fun onBindData(data: MainBean) {
            setText(R.id.tvName, data.name)
        }

        override fun onItemClick(data: MainBean) {
            when (data.index) {
                1 -> startFragment(NetGoFragment())
                2 -> startFragment(ImageGoFragment())
            }

        }
    }
}