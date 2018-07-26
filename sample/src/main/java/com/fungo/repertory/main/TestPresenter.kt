package com.fungo.repertory.main

import com.fungo.baselib.base.recycler.BaseRecyclerContract

/**
 * @author Pinger
 * @since 18-7-25 下午5:47
 *
 */

class TestPresenter(var view: BaseRecyclerContract.View<TestBean>) : BaseRecyclerContract.Presenter {




    override fun loadData(page: Int) {

        val data= ArrayList<TestBean>()
        for (i in 0..20){
            data.add(TestBean("我是数据$i"))
        }

        view.showContent(page,data)

    }

    override fun onStart() {
        loadData(0)
    }

    override fun onStop() {
    }
}