package com.fungo.sample.netgo

import android.graphics.Typeface
import android.view.Gravity
import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.baselib.base.recycler.BaseRecyclerFragment
import com.fungo.netgo.NetGo
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 18-10-15 下午7:00
 * 网络类库[NetGo]的Demo页面
 */
class NetGoFragment : BaseRecyclerFragment() {

    override fun getPresenter(): BaseRecyclerContract.Presenter {
        return NetGoPresenter(this)
    }


    override fun initRecyView() {
        setPageTitle(getString(R.string.main_netgo))
        getPageTitleView().gravity = Gravity.START or Gravity.CENTER_VERTICAL
        register(GankResults.Item::class.java, NetGoHolder())
    }
}