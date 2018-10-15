package com.fungo.sample.main

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.sample.R


/**
 * @author Pinger
 * @since 18-10-15 下午6:35
 */
class MainPresenter(private val mainView: BaseRecyclerContract.View) : BaseRecyclerContract.Presenter {


    override fun loadData(page: Int) {
        if (mainView.isActive()) {
            mainView.showContent(MainBean(mainView.getContext()?.getString(R.string.main_netgo)))
        }
    }
}