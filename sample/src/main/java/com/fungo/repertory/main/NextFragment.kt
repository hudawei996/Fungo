package com.fungo.repertory.main

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.baselib.base.recycler.BaseRecyclerFragment
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 18-7-24 下午6:01
 *
 */

class NextFragment : BaseRecyclerFragment<TestBean>() {

    override fun getPresenter(): BaseRecyclerContract.Presenter {
        return TestPresenter(this)
    }

    override fun getContentResId(): Int {
        return R.layout.fragment_next
    }


    override fun getPageTitle(): String {
        return "我就是下一个Fragment"
    }
}