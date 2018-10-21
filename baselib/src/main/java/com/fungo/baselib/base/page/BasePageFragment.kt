package com.fungo.baselib.base.page

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseFragment
import com.fungo.baselib.theme.UiUtils
import com.fungo.baselib.utils.StatusBarUtils
import com.fungo.baselib.utils.ViewUtils
import kotlinx.android.synthetic.main.base_fragment_page.*
import kotlinx.android.synthetic.main.base_layout_toolbar.*

/**
 * @author Pinger
 * @since 18-7-24 下午5:21
 * 页面Fragment基类，封装ToolBar和StatusBar，可以不用搭理顶部的标题栏。
 * 另外将页面的各种状态进行统一处理，方便直接调用展示，比如加载中试图，空试图，错误试图等。
 * 还有将平时开发中用到的各种工具类进行封装，提供给子类调用。
 */

abstract class BasePageFragment : BaseFragment() {

    private var mLoadingDialog: AlertDialog? = null
    private var mPageTitle: String? = null

    protected var isLoadingShowing = false
    protected var isLoadingDialogShowing = false

    final override fun getLayoutResId(): Int {
        return R.layout.base_fragment_page
    }

    final override fun initView() {
        // 设置状态栏高度
        if (isSetStatusBarHeight()) {
            StatusBarUtils.setStatusBarHeight(statusView)
        }
        ViewUtils.setVisible(toolBarContainer, isShowToolBar())
        // 设置导航栏文字等
        if (isShowToolBar()) {
            // 设置标题
            setPageTitle(getPageTitle())

            // 左侧返回按钮
            if (isShowBackIcon()) {
                toolbar.navigationIcon = UiUtils.getBackIconFont(context!!)
                toolbar.setNavigationOnClickListener {
                    onBackClick()
                }
            }

            // 填充Menu
            if (getMenuResId() != 0) {
                toolbar.inflateMenu(getMenuResId())
                toolbar.setOnMenuItemClickListener {
                    onMenuItemClick(it)
                }
            }
        }

        // 设置填充容器
        if (pageFragmentContainer.childCount > 0) {
            pageFragmentContainer.removeAllViews()
        }
        LayoutInflater.from(context).inflate(getPageLayoutResId(), pageFragmentContainer)

        initPageView()
    }

    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun onBackClick() {
        getPageActivity()?.onBackPressedSupport()
    }

    /**
     * 菜单项点击
     */
    protected open fun onMenuItemClick(item: MenuItem): Boolean = true

    /**
     * 获取页面的Activity
     */
    open fun getPageActivity(): BasePageActivity? {
        if (context !is BasePageActivity) {
            throw IllegalStateException("使用BasePageFragment的Activity必须继承BasePageActivity")
        }
        return context as BasePageActivity
    }

    /**
     * 展示加载对话框
     * 适用于页面UI已经绘制了，需要再加载数据更新的情况
     */
    open fun showPageLoadingDialog(msg: String) {
        if (mLoadingDialog == null) {
            mLoadingDialog = AlertDialog.Builder(context).create()
            mLoadingDialog!!.setCanceledOnTouchOutside(false)
        }

        if (isAdded && !mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.show()
            val dialogView = LayoutInflater.from(context).inflate(R.layout.base_layout_progress, null)
            mLoadingDialog!!.setContentView(dialogView)
            dialogView.findViewById<TextView>(R.id.tvLoadingMessage).text = msg
            isLoadingDialogShowing = true
        }
    }

    open fun showPageLoadingDialog() {
        showPageLoadingDialog(getString(R.string.base_loading))
    }

    /**
     * 隐藏加载对话框
     */
    open fun hidePageLoadingDialog() {
        mLoadingDialog?.dismiss()
        isLoadingDialogShowing = false
    }

    /**
     * 展示加载中的占位图
     */
    open fun showPageLoading() {
        placeholder?.showLoading()
        isLoadingShowing = true
    }

    open fun showPageLoading(msg: String) {
        placeholder?.showLoading()
        placeholder?.setPageLoadingText(msg)
        isLoadingShowing = true
    }

    open fun hidePageLoading() {
        placeholder?.hideLoading()
        isLoadingShowing = false
    }

    /**
     * 展示空数据的占位图
     */
    open fun showPageEmpty() {
        placeholder?.showEmpty()
    }

    /**
     * 展示加载错误的占位图
     */
    open fun showPageError() {
        showPageError(null)
    }

    open fun showPageError(msg: String?) {
        placeholder?.setPageErrorText(msg)
        placeholder?.showError()
    }

    /**
     * 设置页面加载错误重连的监听
     */
    open fun setPageErrorRetryListener(listener: View.OnClickListener) {
        placeholder?.setPageErrorRetryListener(listener)
    }

    /**
     * 展示加载完成，要显示的内容
     */
    open fun showPageContent() {
        placeholder?.showContent()
        isLoadingShowing = false
    }

    /**
     * 主动设置页面标题，给子类调用
     */
    protected open fun setPageTitle(title: String?) {
        if (mPageTitle != title) {
            mPageTitle = title
        }
        toolbar.title = title
    }

    /**
     * 获取标题栏对象，让子类主动去设置样式
     */
    protected open fun getPageToolBar(): Toolbar {
        return toolbar
    }

    /**
     * 获取页面标题，进入页面后会调用该方法获取标题，设置给ToolBar
     * 调用该方法返回Title，则会使用默认的Title样式，如果需要设置样式
     * 请调用setPageTitle()
     */
    protected open fun getPageTitle(): String? = mPageTitle


    /**
     * 是否设置状态栏高度，如果设置的话，默认会自动调整状态栏的高度
     * 默认设置高度
     */
    protected open fun isSetStatusBarHeight(): Boolean = true


    /**
     * 是否展示ToolBar，如果设置为false则不展示，如果导航栏不展示，状态栏的高度不会自动适配，需要自己适配
     * 默认展示ToolBar
     */
    protected open fun isShowToolBar(): Boolean = true

    /**
     * 设置ToolBar的展示状态
     * @param isShow 是否展示
     */
    protected open fun setShowToolBar(isShow: Boolean) {
        ViewUtils.setVisible(toolBarContainer, isShow)
    }

    /**
     * 是否可以返回，如果可以则展示返回按钮，并且设置返回事件
     * 默认可以返回
     */
    protected open fun isShowBackIcon(): Boolean = true

    /**
     * 给子类初始化页面
     */
    protected open fun initPageView() {}


    /**
     * 获取子页面布局
     */
    abstract fun getPageLayoutResId(): Int


    /**
     * 获取填充menu菜单
     */
    protected open fun getMenuResId(): Int = 0

}