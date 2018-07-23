package com.fungo.repertory.recycler

import com.fungo.baselib.base.page.BasePageActivity
import com.fungo.baselib.base.page.swipeback.SwipeBackLayout
import com.fungo.repertory.R

/**
 * @author Pinger
 * @since 2018/1/13 0013 上午 11:09
 */

class RecyclerPageActivity : BasePageActivity() {
    override val layoutResID: Int
        get() = R.layout.activity_recycler_page


    override fun initView() {
        setEdgeLevel(SwipeBackLayout.EdgeLevel.MED)
    }
}
