package com.fungo.baselib.base.page

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.fungo.baselib.R
import com.fungo.baselib.base.basic.BaseActivity
import kotlinx.android.synthetic.main.activity_page.*

/**
 * @author Pinger
 * @since 18-7-20 下午5:34
 * 页面的中转Activity的基类
 */

abstract class BasePageActivity : BaseActivity() {


    override val layoutResID: Int
        get() = R.layout.activity_page

    override fun initAfter() {
        super.initAfter()
        // 设置ToolBar
        setSupportActionBar(toolBar)

        // 获取NavigationFragment
        val navHostFragment = fragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
                ?: return
        navHostFragment.setGraph(getGraphResId())
    }


    override fun getStatusBarView(): View {
        return statusBarView
    }

    /**
     * 获取Fragment导航的布局id
     */
    abstract fun getGraphResId(): Int


    /**
     * Fragment返回键的处理
     */
    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.navHostFragment).navigateUp()
    }


}