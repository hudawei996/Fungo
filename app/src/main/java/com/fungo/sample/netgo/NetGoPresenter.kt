package com.fungo.sample.netgo

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.netgo.callback.JsonCallBack
import com.fungo.netgo.exception.ApiException

/**
 * @author Pinger
 * @since 2018/10/16 0:33
 */
class NetGoPresenter(private val netgoView: BaseRecyclerContract.View) : BaseRecyclerContract.Presenter {

    override fun loadData(page: Int) {

        // 发起一个需要解析的请求
        Api.getGankData(object : JsonCallBack<GankResults>() {
            override fun onSuccess(t: GankResults?) {
                netgoView.showContent(t?.results)
            }

            override fun onError(e: ApiException?) {
                netgoView.showPageError(e?.message)

            }

        })
    }
}