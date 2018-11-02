package com.fungo.sample.main

import com.fungo.baseuilib.recycler.BaseRecyclerContract
import com.fungo.sample.R


/**
 * @author Pinger
 * @since 18-10-15 下午6:35
 */
class MainPresenter(private val mainView: BaseRecyclerContract.View) : BaseRecyclerContract.Presenter {


    override fun loadData(page: Int) {
        if (mainView.isActive()) {
            mainView.addItem(MainBean(1,mainView.getContext()?.getString(R.string.main_netgo)))
            mainView.addItem(MainBean(2,mainView.getContext()?.getString(R.string.main_imagego)))
        }
    }
}