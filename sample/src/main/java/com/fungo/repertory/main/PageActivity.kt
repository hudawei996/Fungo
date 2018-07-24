package com.fungo.repertory.main

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.repertory.R
import kotlinx.android.synthetic.main.activity_page2.*

/**
 * @author Pinger
 * @since 18-7-24 上午11:54
 *
 */

class PageActivity : BaseActivity() {

    override val layoutResID: Int
        get() = R.layout.activity_page2

    override fun initAfter() {
        super.initAfter()
        // 设置ToolBar
        setSupportActionBar(toolBar)

        // 获取NavigationFragment
        val navHostFragment = fragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
                ?: return
//        navHostFragment.setGraph(getGraphResId())
    }


    override fun getStatusBarView(): View {
        return statusBarView
    }


    /**
     * Fragment返回键的处理
     */
    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.navHostFragment).navigateUp()
    }

}