package com.fungo.sample.main.recycler

import android.view.ViewGroup
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.baselib.utils.ToastUtils
import com.fungo.imagego.loadImage
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 18-7-26 上午11:35
 */

class RecyclerAdViewHolder : MultiTypeViewHolder<RecyclerAdBean, RecyclerAdViewHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(parent)
    }


    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<RecyclerAdBean>(parent, R.layout.holder_recycler_ad) {
        override fun onBindData(data: RecyclerAdBean) {
            setText(R.id.adTitle, data.title)
            loadImage(data.img, findView(R.id.adView))
        }

        override fun onItemClick(data: RecyclerAdBean) {
            ToastUtils.showToast("我是吐司条目$dataPosition")
        }
    }


}