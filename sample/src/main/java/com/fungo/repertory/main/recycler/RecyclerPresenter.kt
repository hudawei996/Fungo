package com.fungo.repertory.main.recycler

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import java.util.*

/**
 * @author Pinger
 * @since 18-7-25 下午5:47
 *
 */

class RecyclerPresenter(var view: BaseRecyclerContract.View) : BaseRecyclerContract.Presenter {


    override fun loadData(page: Int) {

        val data = ArrayList<Any>()
        for (i in 0..20) {
            data.add(RecyclerTextBean("我是数据$i"))

            if (i == 0 || i == 10) {
                data.add(RecyclerAdBean("我是广告大额", "http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg"))
            }

        }
        view.showContent(page, data)


    }

    override fun onStart() {
        loadData(0)
    }

    override fun onStop() {
    }
}