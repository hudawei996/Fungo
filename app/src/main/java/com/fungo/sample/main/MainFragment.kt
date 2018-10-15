package com.fungo.sample.main

import android.view.ViewGroup
import com.fungo.baselib.base.recycler.BaseRecyclerContract
import com.fungo.baselib.base.recycler.BaseRecyclerFragment
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.sample.R
import com.fungo.sample.netgo.NetGoFragment

/**
 * @author Pinger
 * @since 18-10-15 下午6:33
 * 主页，存放各个类库Demo的入口
 */

class MainFragment : BaseRecyclerFragment() {

    override fun getPageTitle(): String? {
        return getString(R.string.app_name)
    }

    override fun initRecyclerView() {
        register(MainBean::class.java, MainHolder())
    }

    override fun getPresenter(): BaseRecyclerContract.Presenter {
        return MainPresenter(this)
    }


    inner class MainHolder : MultiTypeViewHolder<MainBean, MainHolder.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            return ViewHolder(parent)
        }

        inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<MainBean>(parent, R.layout.holder_main) {
            override fun onBindData(data: MainBean) {
                setText(R.id.tvName, data.name)
            }

            override fun onItemClick(data: MainBean) {
                getPageActivity()?.start(NetGoFragment())
            }
        }
    }

    override fun isBackEnable(): Boolean = false


    override fun isSwipeBackEnable(): Boolean = false


    override fun isEnablePureScrollMode(): Boolean = true
}