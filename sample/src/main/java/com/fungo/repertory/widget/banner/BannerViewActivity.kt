package com.fungo.repertory.widget.banner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.baselib.view.banner.MZBannerView
import com.fungo.baselib.view.banner.holder.MZHolderCreator
import com.fungo.baselib.view.banner.holder.MZViewHolder
import com.fungo.repertory.R
import java.util.*

/**
 * @author pinger
 * @since 2017/11/25 20:45
 */

class BannerViewActivity : BaseActivity() {

    private val mRes = arrayOf(R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4, R.mipmap.banner5)
    private val mBannerView: MZBannerView<Int> by lazy {
        findViewById<MZBannerView<Int>>(R.id.banner_view)
    }

    override val layoutResID: Int
        get() = R.layout.activity_banner_view

    override fun initView() {
        mBannerView.duration = 3000
        mBannerView.setDelayedTime(3000)
        mBannerView.setIndicatorVisible(false)
        mBannerView.setUseDefaultDuration(false)
    }

    override fun initEvent() {

    }

    override fun initData() {
        val datas = ArrayList<Int>()
        Collections.addAll(datas, *mRes)
        mBannerView.setPages(datas, object : MZHolderCreator<AdViewHolder> {
            override fun createViewHolder(): AdViewHolder {
                return AdViewHolder()
            }
        })
        mBannerView.start()
    }

    private inner class AdViewHolder : MZViewHolder<Int> {

        private lateinit var imageView: ImageView

        override fun createView(context: Context): View {
            val view = LayoutInflater.from(context).inflate(R.layout.banner_item, null)
            imageView = view.findViewById(R.id.imageView)
            return view
        }

        override fun onBind(context: Context, position: Int, data: Int) {
            imageView.setImageResource(data)
        }
    }
}
