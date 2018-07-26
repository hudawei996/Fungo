package com.fungo.repertory.main

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.baselib.base.recycler.BaseRecyclerFragment

/**
 * @author Pinger
 * @since 18-7-24 下午6:01
 *
 */

class NextFragment : BaseRecyclerFragment() {

    override fun getPresenter(): BaseRecyclerContract.Presenter {
        return TestPresenter(this)
    }

    override fun getPageTitle(): String {
        return "我就是下一个Fragment"
    }

    override fun initView() {
        super.initView()

        // 注册holder
        register(TestBean::class.java, TestViewHolder())
        register(AdBean::class.java, AdViewHolder())
    }
}