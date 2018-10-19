package com.fungo.sample.netgo

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.netgo.error.ApiException
import com.fungo.netgo.subscribe.ApiSubscriber

/**
 * @author Pinger
 * @since 2018/10/16 0:33
 */
class NetGoPresenter(private val netgoView: BaseRecyclerContract.View) : BaseRecyclerContract.Presenter {

    override fun loadData(page: Int) {

        // 发起一个需要解析的请求
        Api.getGankData().subscribe(object : ApiSubscriber<GankResults>() {
            override fun onSuccess(t: GankResults?) {
                netgoView.showContent(t?.results)
            }

            override fun onError(e: ApiException?) {
                netgoView.showPageError(e?.message)

            }

        })


        // 发起一个不需要结果的请求
        // Api.getGankDespite()

        // 发起一个返回String的请求
//        Api.getGankString().subscribe(object : StringSubscriber() {
//        })

    }
}