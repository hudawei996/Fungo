package com.fungo.baselib.base.recycler

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fungo.baselib.R
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.baselib.base.recycler.multitype.MultiTypeAdapter
import com.scwang.smartrefresh.header.BezierCircleHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.fragment_recycler.*

/**
 * @author Pinger
 * @since 18-7-25 下午4:35
 * 列表页面布局，封装SmartRefreshLayout，方便替换
 */

abstract class BaseRecyclerFragment<T> : BasePageFragment(), BaseRecyclerContract.View<T> {

    private lateinit var mPresenter: BaseRecyclerContract.Presenter
    private lateinit var mAdapter: MultiTypeAdapter

    private var mPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
    }


    override fun getContentResId(): Int {
        return R.layout.fragment_recycler
    }


    override fun initView() {
        initRefreshLayout()
        initRecyclerView()

    }

    private fun initRefreshLayout() {
        smartRefreshLayout.refreshHeader = BezierCircleHeader(context)
        smartRefreshLayout.refreshFooter = ClassicsFooter(context)
        smartRefreshLayout.setDragRate(1f)
        smartRefreshLayout.setHeaderMaxDragRate(1.2f)
        smartRefreshLayout.setOnRefreshListener {
            mPage = 0
            mPresenter.loadData(mPage)
        }
        smartRefreshLayout.setOnLoadmoreListener {
            mPage += 1
            mPresenter.loadData(mPage)
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = layoutManager
        mAdapter = getAdapter()
        recyclerView.adapter = mAdapter
    }


    /**
     * 展示所有数据
     */
    override fun showContent(page: Int, datas: List<T>) {
        if (page == 0) {
            mAdapter.clear()
        }
        mAdapter.addAll(datas)
    }


    /**
     * 更新某一条数据
     */
    override fun updateItem(data: T, position: Int) {
        if (mAdapter.itemCount > 0 && position < mAdapter.itemCount) {
            mAdapter.update(data, position)
        }
    }


    /**
     * 结束刷新
     */
    override fun finishLoading(isRefresh: Boolean) {
        if (isRefresh) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadmore()
        }
    }


    /**
     * 当前页面是否激活，有View相关的操作时，做好先做一下判断
     */
    override fun isActive(): Boolean {
        return isAdded
    }


    /**
     * 获取子类Presenter对象
     */
    protected abstract fun getPresenter(): BaseRecyclerContract.Presenter

    /**
     * 获取子类适配器对象
     */
    private fun getAdapter(): MultiTypeAdapter {
        return MultiTypeAdapter(context)
    }
}