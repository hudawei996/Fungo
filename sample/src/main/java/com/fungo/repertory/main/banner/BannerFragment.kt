package com.fungo.repertory.main.banner

import android.view.View
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.baselib.utils.ToastUtils
import com.fungo.baselib.view.banner.BannerView
import com.fungo.baselib.view.banner.holder.ViewPagerHolderCreator
import com.fungo.repertory.R
import com.fungo.repertory.main.FragmentFactory
import kotlinx.android.synthetic.main.fragment_banner.*

/**
 * @author Pinger
 * @since 2018/7/26 下午10:45
 */
class BannerFragment : BasePageFragment() {

    override fun getContentResId(): Int {
        return R.layout.fragment_banner
    }

    override fun getPageTitle(): String? {
        return arguments?.getString(FragmentFactory.FRAGMENT_KEY_TITLE)
    }

    private val bannerView by lazy {
        findView<BannerView<BannerBean>>(R.id.bannerView)
    }

    override fun initEvent() {
        bannerView.setBannerPageClickListener(object : BannerView.BannerPageClickListener<BannerBean> {
            override fun onPageClick(view: View, position: Int, data: BannerBean) {
                ToastUtils.showToast(data.title)
            }

        })
    }


    override fun initData() {

        val data = ArrayList<BannerBean>()
        for (i in 0..5) {
            data.add(BannerBean("我是Banner标题$i", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532628464134&di=b3aa02630ce090b5773b53fe1b1205b1&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0173eb59080ec0a801214550fd7500.jpg%401280w_1l_2o_100sh.jpg"))
        }

        bannerView.setPages(data, object : ViewPagerHolderCreator<BannerHolder> {
            override fun createViewPagerHolder(): BannerHolder {
                return BannerHolder()
            }
        })

        bannerView.mScrollDuration = 3000
//        bannerView.isUseDefaultScrollDuration = true



        bannerView.start()

    }
}