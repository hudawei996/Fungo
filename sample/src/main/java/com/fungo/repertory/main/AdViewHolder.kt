package com.fungo.repertory.main

import android.view.ViewGroup
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.baselib.utils.ToastUtils
import com.fungo.imagego.loadImage
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 18-7-26 上午11:35
 *
 */

class AdViewHolder : MultiTypeViewHolder<AdBean, AdViewHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent)
    }


    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<AdBean>(parent, R.layout.holder_ad) {
        override fun onBindData(data: AdBean) {
            setText(R.id.adTitle, data.title)
            loadImage(data.img, findView(R.id.adView))
        }

        override fun onItemClick(data: AdBean) {
            ToastUtils.showToast("我是吐司条目$dataPosition")
        }
    }


}