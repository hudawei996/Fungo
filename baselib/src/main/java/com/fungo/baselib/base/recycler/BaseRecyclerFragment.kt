package com.fungo.baselib.base.recycler

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fungo.baselib.R
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.baselib.base.recycler.multitype.MultiTypeAdapter
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.baselib.base.recycler.multitype.OneToManyFlow
import com.scwang.smartrefresh.header.BezierCircleHeader
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import kotlinx.android.synthetic.main.base_fragment_recycler.*

/**
 * @author Pinger
 * @since 18-7-25 下午4:35
 * 列表页面布局，封装SmartRefreshLayout，方便替换
 */

abstract class BaseRecyclerFragment : BasePageFragment(), BaseRecyclerContract.View {

    private lateinit var mPresenter: BaseRecyclerContract.Presenter
    private lateinit var mAdapter: MultiTypeAdapter

    private var mPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
    }

    override fun getContentResId(): Int {
        return R.layout.base_fragment_recycler
    }

    override fun initView() {
        initRefreshLayout()
        initRecyclerView()
    }

    override fun initData() {
        mPage = 0
        mPresenter.loadData(mPage)
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

        // 设置相关属性
        smartRefreshLayout.isEnableAutoLoadmore = isEnableAutoLoadmore()
        smartRefreshLayout.isEnableLoadmore = isEnableLoadmore()
        smartRefreshLayout.isEnableRefresh = isEnableRefresh()
        smartRefreshLayout.isEnablePureScrollMode = isEnablePureScrollMode()
        smartRefreshLayout.isEnableOverScrollBounce = isEnableOverScrollBounce()


    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        mAdapter = getAdapter()
        recyclerView.adapter = mAdapter
    }


    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }

    /**
     * 展示所有数据
     */
    override fun <T> showContent(page: Int, datas: List<T>) {
        finishLoading(page)
        if (page == 0) {
            mAdapter.clear()
        }
        mAdapter.addAll(datas)
    }


    /**
     * 更新某一条数据
     */
    override fun <T> updateItem(data: T, position: Int) {
        if (mAdapter.itemCount > 0 && position < mAdapter.itemCount) {
            mAdapter.update(data, position)
        }
    }

    /**
     * 添加一条数据
     */
    override fun <T> addItem(data: T) {
        mAdapter.add(data)
    }


    /**
     * 插入一条数据
     */
    override fun <T> insertItem(position: Int, data: T) {
        if (position < mAdapter.itemCount) {
            mAdapter.insert(data, position)
        }
    }


    /**
     * 结束刷新
     */
    private fun finishLoading(page: Int) {
        if (page == 0) {
            smartRefreshLayout?.finishRefresh()
        } else {
            smartRefreshLayout?.finishLoadmore()
        }
    }


    /**
     * 当前页面是否激活，有View相关的操作时，做好先做一下判断
     */
    override fun isActive(): Boolean {
        return isAdded
    }

    /**
     * 注册展示的Holder
     */
    fun <T> register(clazz: Class<out T>, holder: MultiTypeViewHolder<T, *>) {
        mAdapter.register(clazz, holder)
    }


    fun <T> register(clazz: Class<out T>): OneToManyFlow<T> {
        return mAdapter.register(clazz)
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


    /**
     * 是否可以自动加载更多，默认可以
     */
    protected fun isEnableAutoLoadmore() = true


    /**
     * 是否可以加载更多，默认可以
     */
    protected fun isEnableLoadmore() = true

    /**
     * 是否可以刷新，默认可以
     */
    protected fun isEnableRefresh() = true

    /**
     * 是否是纯净模式，不展示刷新头和底部，默认false
     */
    protected fun isEnablePureScrollMode() = false

    /**
     * 刷新时是否可以越界回弹
     */
    protected fun isEnableOverScrollBounce() = false

}