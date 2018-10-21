package com.fungo.sample.main

import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.baselib.base.recycler.BaseRecyclerFragment
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 18-10-15 下午6:33
 * 主页，存放各个类库Demo的入口
 */

class MainFragment : BaseRecyclerFragment() {

    override fun getPageTitle(): String? {
        return getString(R.string.app_name)
    }

    override fun initRecyView() {
        register(MainBean::class.java, MainHolder())
    }

    override fun getPresenter(): BaseRecyclerContract.Presenter {
        return MainPresenter(this)
    }

    override fun isShowBackIcon(): Boolean = false


    override fun isSwipeBackEnable(): Boolean = false


    override fun isEnablePureScrollMode(): Boolean = true
}