package com.fungo.repertory.main.banner

import android.animation.LayoutTransition
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.fungo.baselib.view.banner.holder.ViewPagerHolder
import com.fungo.imagego.loadImage
import com.fungo.repertory.R
import com.fungo.repertory.R.id.imageView
import com.fungo.repertory.R.id.text

/**
 * @author Pinger
 * @since 2018/7/26 下午11:01
 */
class BannerHolder : ViewPagerHolder<BannerBean> {

    private var itemView: View? = null

    override fun createView(context: Context): View {
        itemView = LayoutInflater.from(context).inflate(R.layout.holder_banner, null)
        return itemView!!
    }

    override fun onBindData(context: Context, position: Int, data: BannerBean) {
        loadImage(data.url, itemView?.findViewById(R.id.imageView))
        itemView?.findViewById<TextView>(R.id.textView)?.text = data.title

    }
}