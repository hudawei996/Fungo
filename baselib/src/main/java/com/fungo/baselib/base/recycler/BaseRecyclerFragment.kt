package com.fungo.baselib.base.recycler

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fungo.baselib.R
import com.fungo.baselib.base.page.BasePageFragment
import com.fungo.baselib.base.recycler.multitype.MultiTypeAdapter
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.baselib.base.recycler.multitype.OneToManyFlow
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
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

    final override fun getContentResId(): Int {
        return R.layout.base_fragment_recycler
    }

    final override fun initPageView() {
        smartRefreshLayout.refreshHeader = MaterialHeader(context)
        smartRefreshLayout.refreshFooter = BallPulseFooter(context!!)
        smartRefreshLayout.setDragRate(1f)
        smartRefreshLayout.setHeaderMaxDragRate(1.5f)
        smartRefreshLayout.setOnRefreshListener {
            mPage = 0
            mPresenter.loadData(mPage)
        }
        smartRefreshLayout.setOnLoadmoreListener {
            mPage += 1
            mPresenter.loadData(mPage)
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        mAdapter = getAdapter()
        recyclerView.adapter = mAdapter

        setPageErrorRetryListener(View.OnClickListener { initData() })

        initRecyView()
    }

    final override fun initData() {
        mPage = 0

        setSmartLayoutAttrs()

        if (isShowLoadingPage()) {
            showPageLoading()
        }

        if (isShowLoadingDialog()) {
            showPageLoadingDialog()
        }

        mPresenter.loadData(mPage)
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
     * 设置SmartRefreshLayout相关属性
     */
    private fun setSmartLayoutAttrs() {
        smartRefreshLayout?.isEnableAutoLoadmore = isEnableAutoLoadmore()
        smartRefreshLayout?.isEnableLoadmore = isEnableLoadmore()
        smartRefreshLayout?.isEnableRefresh = isEnableRefresh()
        smartRefreshLayout?.isEnableOverScrollBounce = isEnableOverScrollBounce()
        smartRefreshLayout?.isEnablePureScrollMode = isEnablePureScrollMode()
    }

    /**
     * 展示所有数据
     */
    override fun <T> showContent(datas: List<T>?) {
        hideLoading()
        finishLoading()

        // 处理空数据的情况
        if (mPage == 0) {
            mAdapter.clear()
            if (datas == null || datas.isEmpty())
                showPageEmpty()
            else mAdapter.addAll(datas)
        } else {
            if (datas == null || datas.isEmpty()) {
                showToast("暂无更多数据")
            } else mAdapter.addAll(datas)
        }
    }

    /**
     * 展示一个数据
     */
    override fun <T> showContent(data: T?) {
        hideLoading()
        finishLoading()

        // 处理空数据的情况
        if (mPage == 0) {
            mAdapter.clear()
        }
        if (data == null) {
            showPageEmpty()
            return
        }
        mAdapter.add(data)
    }


    /**
     * 更新某一条数据
     */
    override fun <T> updateItem(data: T?, position: Int) {
        if (data != null && mAdapter.itemCount > 0 && position < mAdapter.itemCount) {
            mAdapter.update(data, position)
        }
    }

    /**
     * 添加一条数据
     */
    override fun <T> addItem(data: T?) {
        hideLoading()
        if (data != null) {
            mAdapter.add(data)
        }
    }


    /**
     * 插入一条数据
     */
    override fun <T> insertItem(position: Int, data: T?) {
        if (position < mAdapter.itemCount && data != null) {
            mAdapter.insert(data, position)
        }
    }


    /**
     * 结束刷新
     */
    private fun finishLoading() {
        if (smartRefreshLayout?.isRefreshing == true) {
            smartRefreshLayout?.finishRefresh()
        }
        if (smartRefreshLayout?.isLoading == true) {
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
     * 隐藏正在加载的进度条
     */
    private fun hideLoading() {
        if (isLoadingShowing) {
            hidePageLoading()
        }
        if (isLoadingDialogShowing) {
            hidePageLoadingDialog()
        }
    }

    /**
     * 当展示空视图的时候，可以下拉，但是不可以上拉
     */
    override fun showPageEmpty() {
        smartRefreshLayout?.isEnableLoadmore = false
        super.showPageEmpty()
    }


    override fun showPageError(msg: String?) {
        smartRefreshLayout?.isEnableLoadmore = false

        // 当加载异常时，要手动去关闭加载进度，这里统一处理
        hideLoading()
        finishLoading()

        // 先把前期工作设置完，最后展示占位图
        super.showPageError(msg)
    }


    /**
     * 是否可以自动加载更多，默认可以
     */
    protected open fun isEnableAutoLoadmore() = true


    /**
     * 是否可以加载更多，默认不可以
     */
    protected open fun isEnableLoadmore() = true

    /**
     * 是否可以刷新，默认可以
     */
    protected open fun isEnableRefresh() = true

    /**
     * 是否是纯净模式，不展示刷新头和底部，默认false
     */
    protected open fun isEnablePureScrollMode() = false

    /**
     * 刷新时是否可以越界回弹
     */
    protected open fun isEnableOverScrollBounce() = false

    /**
     * 默认展示加载页面
     */
    protected open fun isShowLoadingPage() = true

    /**
     * 是否展示加载对话框
     */
    protected open fun isShowLoadingDialog() = false

    /**
     * 让子类重写，初始化页面
     */
    protected open fun initRecyView() {}
}