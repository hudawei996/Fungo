package com.fungo.sample.main

import android.graphics.Typeface
import android.view.Gravity
import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.baselib.base.recycler.BaseRecyclerFragment
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 18-10-15 下午6:33
 * 主页，存放各个类库Demo的入口
 */

class MainFragment : BaseRecyclerFragment() {

    override fun initRecyView() {
        setPageTitle(getString(R.string.app_name))
        getPageTitleView().gravity = Gravity.START or Gravity.CENTER_VERTICAL
        getPageTitleView().typeface = Typeface.defaultFromStyle(Typeface.BOLD)

        register(MainBean::class.java, MainHolder())



    }

    override fun getPresenter(): BaseRecyclerContract.Presenter {
        return MainPresenter(this)
    }

    override fun isShowBackIcon(): Boolean = false


    override fun isSwipeBackEnable(): Boolean = false


    override fun isEnablePureScrollMode(): Boolean = true
}