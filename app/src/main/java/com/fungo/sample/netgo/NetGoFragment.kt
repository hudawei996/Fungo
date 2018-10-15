package com.fungo.sample.netgo

import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.netgo.ApiSubscriber
import com.fungo.netgo.NetError
import com.fungo.netgo.NetGo
import com.fungo.sample.R
import kotlinx.android.synthetic.main.fragment_netgo.*

/**
 * @author Pinger
 * @since 18-10-15 下午7:00
 * 网络类库[NetGo]的Demo页面
 */
class NetGoFragment : BasePageFragment() {

    override fun getPageTitle(): String? {
        return getString(R.string.main_netgo)
    }

    override fun initPageView() {

        Api.getGankService().getGankData("Android", 30, 0)
                .subscribe(object : ApiSubscriber<GankResults>() {
                    override fun onNext(t: GankResults?) {
                        textView?.text = t?.results.toString()
                    }

                    override fun onFail(error: NetError?) {
                    }


                })
    }

    override fun getContentResId(): Int {
        return R.layout.fragment_netgo
    }


}