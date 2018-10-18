package com.fungo.sample.netgo

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.netgo.subscribe.ApiException
import com.fungo.netgo.subscribe.ApiSubscriber

/**
 * @author Pinger
 * @since 2018/10/16 0:33
 */
class NetGoPresenter(private val netgoView: BaseRecyclerContract.View) : BaseRecyclerContract.Presenter {

    override fun loadData(page: Int) {
        Api.getGankData(object : ApiSubscriber<GankResults>() {
            override fun onSuccess(t: GankResults?) {
                netgoView.showContent(t?.results)
            }

            override fun onError(e: ApiException?) {
                netgoView.showPageError(e?.message)

            }
        })

    }
}