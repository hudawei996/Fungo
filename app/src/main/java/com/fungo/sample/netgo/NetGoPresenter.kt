package com.fungo.sample.netgo

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.netgo.ApiSubscriber
import com.fungo.netgo.NetError
import com.fungo.netgo.NetGo

/**
 * @author Pinger
 * @since 2018/10/16 0:33
 */
class NetGoPresenter(private val netgoView: BaseRecyclerContract.View) : BaseRecyclerContract.Presenter {

    override fun loadData(page: Int) {
        Api.getGankService().getGankData("Android", 30, page)
                .compose(NetGo.getApiTransformer<GankResults>())
                .compose(NetGo.getScheduler<GankResults>())
                .subscribe(object : ApiSubscriber<GankResults>() {
                    override fun onNext(t: GankResults?) {
                        netgoView.showContent(t?.results)
                    }

                    override fun onFail(error: NetError?) {
                        netgoView.showPageError()
                    }
                })
    }
}