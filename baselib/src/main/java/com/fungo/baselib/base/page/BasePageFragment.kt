package com.fungo.baselib.base.page

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseFragment
import com.fungo.baselib.utils.StatusBarUtils
import kotlinx.android.synthetic.main.fragment_page.*

/**
 * @author Pinger
 * @since 18-7-24 下午5:21
 *
 */

abstract class BasePageFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_page
    }


    override fun initView() {
        // 设置状态栏高度
        StatusBarUtils.setStatusBarHeight(statusView)

        // 设置导航栏文字等
        toolBar.title = getPageTitle()

        // 设置填充容器
        LayoutInflater.from(context).inflate(getContentResId(), container, false)
    }

    /**
     * 获取页面标题
     */
    protected open fun getPageTitle(): String = ""

    /**
     * 获取页面布局
     */
    abstract fun getContentResId(): Int


    /**
     * 设置页面标题
     */
    protected open fun setPageTitle(title: String?) {
        toolBar?.title = title
    }

}