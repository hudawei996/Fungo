package com.fungo.baselib.base.page

import android.app.AlertDialog
import android.support.annotation.ColorInt
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseFragment
import com.fungo.baselib.utils.StatusBarUtils
import com.fungo.baselib.utils.ViewUtils
import com.fungo.baselib.view.placeholder.PlaceholderView

/**
 * @author Pinger
 * @since 18-7-24 下午5:21
 *
 */

abstract class BasePageFragment : BaseFragment() {


    private var mLoadingDialog: AlertDialog? = null

    protected var isLoadingShowing = false
    protected var isLoadingDialogShowing = false


    override fun getLayoutResId(): Int {
        return R.layout.base_fragment_page
    }

    final override fun initView() {
        // 设置状态栏高度
        if (isSetStatusBarHeight()) {
            StatusBarUtils.setStatusBarHeight(findView(R.id.statusView))
        }

        ViewUtils.setVisible(findView(R.id.toolBarContainer), isShowToolBar())

        // 设置导航栏文字等
        if (isShowToolBar()) {
            findView<TextView>(R.id.baseTvTitle).text = getPageTitle()

            // 返回按钮
            ViewUtils.setVisible(findView(R.id.baseIvBack), isBackEnable())
            if (isBackEnable()) {
                findView<ImageView>(R.id.baseIvBack).setOnClickListener {
                    doBackAction()
                }
            }

            // 分享按钮
            ViewUtils.setVisible(findView(R.id.baseIvShare), isShareEnable())
            if (isShareEnable()) {
                findView<ImageView>(R.id.baseIvShare).setOnClickListener {
                    doShareAction()
                }
            }
        }

        // 设置填充容器
        val container = findView<FrameLayout>(R.id.container)
        if (container.childCount > 0) {
            container.removeAllViews()
        }
        LayoutInflater.from(context).inflate(getContentResId(), container)

        initPageView()
    }


    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun doBackAction() {
        getPageActivity()?.onBackPressedSupport()
    }


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
        findView<PlaceholderView>(R.id.placeholder).showLoading()
        isLoadingShowing = true
    }

    open fun hidePageLoading() {
        findView<PlaceholderView>(R.id.placeholder).hideLoading()
        isLoadingShowing = false
    }

    /**
     * 展示空数据的占位图
     */
    open fun showPageEmpty() {
        findView<PlaceholderView>(R.id.placeholder).showEmpty()
    }

    /**
     * 展示加载错误的占位图
     */
    open fun showPageError() {
        findView<PlaceholderView>(R.id.placeholder).showError()
    }

    /**
     * 展示加载完成，要显示的内容
     */
    open fun showPageContent() {
        findView<PlaceholderView>(R.id.placeholder).showContent()
        isLoadingShowing = false
    }


    /**
     * 主动设置页面标题，给子类调用设置标题
     */
    protected open fun setPageTitle(title: String?) {
        findView<TextView>(R.id.baseTvTitle).text = title
    }

    /**
     * 设置标题字体的颜色
     */
    protected open fun setPageTitleColor(@ColorInt color: Int) {
        findView<TextView>(R.id.baseTvTitle).setTextColor(color)
    }

    /**
     * 设置标题字体的大小
     */
    protected open fun setPageTitleSize(size: Float) {
        findView<TextView>(R.id.baseTvTitle).textSize = size
    }


    /**
     * 获取页面标题，进入页面后会调用该方法获取标题，设置给ToolBar
     * 标题默认为空
     */
    protected open fun getPageTitle(): String? = ""


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
     * 是否可以返回，如果可以则展示返回按钮，并且设置返回事件
     * 默认可以返回
     */
    protected open fun isBackEnable(): Boolean = true

    /**
     * 是否可以分享，如果可以则展示分享按钮
     * 默认不展示
     */
    protected open fun isShareEnable(): Boolean = false


    /**
     * 执行分享动作
     */
    protected open fun doShareAction() {}

    /**
     * 给子类初始化页面
     */
    protected open fun initPageView() {}


    /**
     * 获取子页面布局
     */
    abstract fun getContentResId(): Int
}